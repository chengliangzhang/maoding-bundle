package com.maoding.corp.module.corpserver.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.maoding.constDefine.corp.RKey;
import com.maoding.constDefine.corp.SyncCmd;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.corp.module.corpserver.dao.*;
import com.maoding.corp.module.corpserver.dto.*;
import com.maoding.corp.module.corpserver.model.*;
import com.maoding.utils.MD5Helper;
import com.maoding.utils.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Idccapp21 on 2017/2/8.
 */
@Service("collaborationService")
public class CollaborationServiceImpl extends BaseService implements CollaborationService {

    private static final Logger log = LoggerFactory.getLogger(CollaborationServiceImpl.class);

    @Autowired
    private CollaborationDAO collaborationDao;

    @Autowired
    private ProjectMemberDAO projectMemberDAO;

    @Autowired
    private MyTaskDAO myTaskDao;

    @Autowired
    private ProcessNodeDAO processNodeDao;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ProjectTaskDAO projectTaskDao;

    @Autowired
    private SyncCompanyDAO syncCompanyDao;

    @Override
    public List<CoCompanyDTO> listCompanyByIds(List<String> companyIds) {
        return collaborationDao.listCompanyByIds(companyIds);
    }

    @Override
    public List<ProjectDTO> listProjectByCompanyId(String companyId, String syncDate) throws Exception {
        List<ProjectDTO> projects = collaborationDao.listProjectByCompanyId(companyId, null);
        return projects;
    }

    @Override
    public List<CoUserDTO> listUserByCompanyId(String companyId) {
        List<CoUserDTO> coUsers = collaborationDao.listUserByCompanyId(companyId);
        return coUsers;
    }


    @Override
    public ProjectDTO getProjectById(String projectId) {
        return collaborationDao.getProjectById(projectId);
    }


    /**
     * 获取项目节点（含任务成员状态信息）
     */
    @Override
    public List<CoProjectPhaseDTO> listNode(String projectId) throws Exception {

        //读取协同组织
        Set<String> syncCompanies = Sets.newHashSet();
        try {
            RReadWriteLock cLock = redissonClient.getReadWriteLock(RKey.LOCK_CORP_EP_C);
            RLock r = cLock.readLock();
            r.lock(5, TimeUnit.SECONDS);
            RSet<String> set_companies = redissonClient.getSet(RKey.CORP_EP_C);
            syncCompanies = set_companies.readAll();
            r.unlock();
        } catch (Exception e) {
            log.error("读取协同组织发生异常", e);
        }

        List<CoProjectPhaseDTO> result = Lists.newArrayList();
        List<ProjectTaskDO> tasks = collaborationDao.listProjectTask(projectId, null);
        if (tasks.size() > 0) {
            //筛选出阶段
            List<ProjectTaskDO> phases = tasks.stream().filter(t -> t.getTaskType() == 1).sorted(Comparator.comparingInt(ProjectTaskDO::getSeq)).collect(Collectors.toList());

            for (ProjectTaskDO phase : phases) {
                //项目阶段
                CoProjectPhaseDTO coPhase = new CoProjectPhaseDTO();
                coPhase.setStatus(phase.getTaskStatus().equals("0") ? 1 : 0);
                coPhase.setProjectPhaseId(phase.getId());
                coPhase.setProjectId(projectId);
                coPhase.setPhaseName(phase.getTaskName());
                coPhase.setLevel(phase.getTaskLevel());
                coPhase.setSeq(phase.getSeq());

                coPhase.setTasks(Lists.newArrayList());
                result.add(coPhase);

                //根据路径筛选阶段子任务(按层级排序）
                /*List<ProjectTaskDO> childTasks = tasks.stream().filter(t -> t.getTaskType() != 1 && StringUtils.startsWithIgnoreCase(t.getTaskPath(), phase.getId())).sorted(Comparator.comparingLong(c -> c.getTaskLevel() * 100000L + Timestamp.valueOf(c.getCreateDate()).getTime())).collect(Collectors.toList());*/

                //根据路径筛选阶段子任务(按层级排序）
                List<ProjectTaskDO> childTasks = tasks.stream().filter(t -> t.getTaskType() != 1 && StringUtils.startsWithIgnoreCase(t.getTaskPath(), phase.getId())).sorted(Comparator.comparingLong(c -> c.getTaskLevel() * 100000L + (c.getSeq() == 0 ? Timestamp.valueOf(c.getCreateDate()).getTime() : c.getSeq()))).collect(Collectors.toList());

                if (childTasks.size() <= 0)
                    continue;

                for (ProjectTaskDO ct : childTasks) {
                    //创建子任务
                    CoTaskDTO coTask = new CoTaskDTO();
                    coTask.setStatus(ct.getTaskStatus().equals("0") ? 1 : 0);
                    coTask.setId(ct.getId());
                    coTask.setProjectPhaseID(phase.getId());
                    coTask.setCode(ct.getCompanyId());
                    coTask.setName(ct.getTaskName());
                    coTask.setLevel(ct.getTaskLevel());
                    coTask.setCompanyId(ct.getCompanyId());
                    coTask.setSeq(ct.getSeq());
                    if (ct.getTaskPid() != null && !ct.getTaskPid().equalsIgnoreCase(phase.getId()))
                        coTask.setParentID(ct.getTaskPid());

                    coTask.setMembers(Lists.newArrayList());
                    coPhase.addTask(coTask);

                    //填充同步组织任务成员
                    if (syncCompanies.stream().anyMatch(c -> StringUtils.endsWithIgnoreCase(c, ":" + ct.getCompanyId())))
                        fillMember(coTask);
                }
            }
        }

        return result;
    }

    //填充任务成员
    private void fillMember(CoTaskDTO coTask) {
        //处理设计人员
        List<CoProjectProcessNodeDTO> ppNodes = processNodeDao.listProcessNodeByTaskId(coTask.getId());
        for (CoProjectProcessNodeDTO node : ppNodes) {

            CoTaskMemberDTO coMember = new CoTaskMemberDTO();
            coMember.setId(node.getAccountId());
            coMember.setRole(node.getNodeName());
            coMember.setPeerID(node.getId());
            coMember.setState(StringUtils.isBlank(node.getCompleteTime()) ? 0 : 1);

            coTask.addMember(coMember);
        }

        //处理任务负责人
        ProjectMemberDO taskPrincipal = projectMemberDAO.getTaskPrincipal(coTask.getId());
        if (taskPrincipal != null) {
            CoTaskMemberDTO coMember = new CoTaskMemberDTO();
            coMember.setId(taskPrincipal.getAccountId());
            coMember.setRole("任务负责人");
            coMember.setPeerID("");
            coMember.setState(0);

            coTask.addMember(coMember);
        }
    }

    @Override
    public ApiResult login(Map<String, Object> map) throws Exception {
        String loginname = map.get("loginName").toString();
        String password = MD5Helper.getMD5For32(map.get("password").toString());
        AccountDO user = collaborationDao.getAccountByCellphone(loginname);
        if (user == null) {
            return ApiResult.failed("用户不存在！", null);
        }
        if (user.getPassword().equals(password)) {
//            String userId = user.getId();
//            String token = TokenProcessor.getInstance().generateTokeCode(request);
//            Map<String,Object> resultMap=new HashMap<String,Object>();
//            resultMap.put()
            return ApiResult.success("登录成功！", null);

        } else {
            return ApiResult.failed("用户名或密码错误！", null);
        }
    }

    /**
     * 完成我的任务
     */
    @Override
    public ApiResult finishMyTask(String processNodeId) {
        CoProjectProcessNodeDTO node = processNodeDao.getProcessNodeById(processNodeId);
        if (node == null)
            return ApiResult.failed("设校审节点不存在！", null);
        if (!StringUtils.isBlank(node.getCompleteTime()))
            return ApiResult.success("设校审节点原来已是完成状态！", null);

        MyTaskDO myTask = myTaskDao.getMyTaskByProcessNodeId(processNodeId);
        if (myTask == null)
            return ApiResult.failed("设校审任务不存在！", null);
        if (myTask.getStatus().equals("1"))
            return ApiResult.success("设校审任务原来已是完成状态！", null);

        int r1 = myTaskDao.updateMyTaskAsFinished(myTask.getId());
        int r2 = processNodeDao.updateProcessNodeAsFinished(processNodeId);

        return ApiResult.success("节点成功设为已完成！", null);
    }

    /**
     * 激活我的任务
     */
    @Override
    public ApiResult activeMyTask(String processNodeId) {
        CoProjectProcessNodeDTO node = processNodeDao.getProcessNodeById(processNodeId);
        if (node == null)
            return ApiResult.failed("设校审节点不存在！", null);
        if (StringUtils.isBlank(node.getCompleteTime()))
            return ApiResult.success("设校审节点原来已是未完成状态！", null);

        MyTaskDO myTask = myTaskDao.getMyTaskByProcessNodeId(processNodeId);
        if (myTask == null)
            return ApiResult.failed("设校审任务不存在！", null);
        if (myTask.getStatus().equals("0"))
            return ApiResult.success("设校审任务原来已是未完成状态！", null);

        ProjectTaskDO pTask = projectTaskDao.selectByPrimaryKey(myTask.getParam1());
        if (pTask == null)
            return ApiResult.failed("设校审任务所在生产节点不存在！", null);

        ProjectTaskDO pTaskParent = projectTaskDao.selectByPrimaryKey(pTask.getTaskPid());
        if (pTaskParent != null && pTaskParent.getCompleteDate() != null)
            return ApiResult.failed("父生产节点已处于已完成状态，不允许激活！", null);

        //任务务负责人
        ProjectMemberDO taskPrincipal = projectMemberDAO.getTaskPrincipal(pTask.getId());

        //是否任务负责人
        if (taskPrincipal != null && taskPrincipal.getAccountId().equals(node.getAccountId())) {

            if (pTask.getCompleteDate() != null) {

                //生产根任务
                if (pTask.getTaskType() == 2 && pTaskParent != null) {
                    MyTaskDO mt = new MyTaskDO();
                    mt.setTargetId(pTaskParent.getId());
                    mt.setStatus("0");
                    mt.setTaskType(22);
                    MyTaskDO rootDesignTask = myTaskDao.selectOne(mt);
                    if (rootDesignTask != null) {
                        //无效化设计负责人的任务
                        myTaskDao.updateMyTaskAsInvalid(rootDesignTask.getId());
                    }
                }

                //激活生产任务
                projectTaskDao.updateProcessTaskAsActived(pTask.getId());
            }

            //激活设校审节点
            processNodeDao.updateProcessNodeAsActived(processNodeId);

            //激活任务负责人任务
            MyTaskDO principalTask = myTaskDao.getPrincipalTaskByProjectTaskId(pTask.getId());
            if (principalTask != null)
                myTaskDao.updateMyTaskAsActived(principalTask.getId());

            //激活设校审任务
            myTaskDao.updateMyTaskAsActived(myTask.getId());

        } else {
            if (pTask.getCompleteDate() != null) {
                return ApiResult.failed("生产任务已完成，不允许激活！", null);
            }

            //激活设校审节点
            processNodeDao.updateProcessNodeAsActived(processNodeId);

            //激活设校审任务
            myTaskDao.updateMyTaskAsActived(myTask.getId());
        }

        //推送同步指令
        pushSyncCMD_PT(pTask.getProjectId(), pTask.getTaskPath(), SyncCmd.PT2);
        return ApiResult.success("节点成功设为未完成！", null);
    }

    private List<String> listMatchEndpoint(String[] companyIds) {
        Set<String> endpoints = Sets.newHashSet();
        //读取协同团队
        RReadWriteLock cLock = redissonClient.getReadWriteLock(RKey.LOCK_CORP_EP_C);
        try {

            RLock r = cLock.readLock();
            r.lock(5, TimeUnit.SECONDS);
            RSet<String> set_companies = redissonClient.getSet(RKey.CORP_EP_C);
            Set<String> companies = set_companies.readAll();
            r.unlock();

            //匹配团队
            companies.forEach(c -> {
                String[] splits = StringUtils.split(c, ":");
                if (splits.length == 2 && Arrays.stream(companyIds).anyMatch(id -> id.equalsIgnoreCase(splits[1]))) {
                    endpoints.add(splits[0]);
                }
            });
        } catch (Exception ex) {
            log.error("getMatchesByCompanyId 发生异常", ex);
        }

        return Lists.newArrayList(endpoints);
    }

    private List<String> listCompanyIdByProjectId(String projectId) {
        List<String> companyIds = collaborationDao.listCompanyIdByProjectId(projectId);
        return companyIds;
    }

    /**
     * 推送同步指令强制同步一个Endpoint下面的所有
     */
    @Override
    public void pushSyncCMD_SyncAllByEndpoint(String endpoint) {
        if (StringUtils.isBlank(endpoint)) {
            log.error("参数 endpoint 不能为空");
            return;
        }

        Example example = new Example(SyncCompanyDO.class);
        example.createCriteria()
                .andCondition("corp_endpoint = ", endpoint);

        List<SyncCompanyDO> scs = syncCompanyDao.selectByExample(example);
        if (scs != null && scs.size() > 0) {
            scs.forEach(sc -> {
                syncAll(sc.getCorpEndpoint(), sc.getCompanyId());
            });
        }
    }

    /**
     * 推送同步指令强制同步一个组织下面的所有
     */
    @Override
    public void pushSyncCMD_SyncAllByCompany(String syncCompanyId) {
        if (StringUtils.isBlank(syncCompanyId)) {
            log.error("参数 syncCompanyId 不能为空");
            return;
        }

        SyncCompanyDO sc = syncCompanyDao.selectByPrimaryKey(syncCompanyId);
        if (sc == null)
            return;

        syncAll(sc.getCorpEndpoint(), sc.getCompanyId());
    }

    private void syncAll(String endpoint, String companyId) {

        String lockKey = String.format(RKey.LOCK_CORP_EP_SYNC_C_PATTERN, endpoint).toUpperCase();
        String key = String.format(RKey.CORP_EP_SYNC_C_PATTERN, endpoint).toUpperCase();
        try {
            RReadWriteLock cLock = redissonClient.getReadWriteLock(lockKey);
            RLock r = cLock.readLock();
            r.lock(5, TimeUnit.SECONDS);

            RSet<String> changes = redissonClient.getSet(key);
            changes.add(SyncCmd.CU + ":" + companyId);
            changes.add(SyncCmd.CD + ":" + companyId);
            r.unlock();
            log.info("端点 {} 添加协同变更: {}", endpoint, SyncCmd.CU + ":" + companyId);
            log.info("端点 {} 添加协同变更: {}", endpoint, SyncCmd.CD + ":" + companyId);
        } catch (Exception ex) {
            log.error("syncAll 发生异常#1", ex);
        }

        List<String> projectIds = collaborationDao.listProjectIdByCompanyId(companyId);
        projectIds.forEach(projectId -> {
            String lockKey2 = String.format(RKey.LOCK_CORP_EP_SYNC_P_ID_PATTERN, endpoint, projectId).toUpperCase();
            String key2 = String.format(RKey.CORP_EP_SYNC_P_ID_PATTERN, endpoint, projectId).toUpperCase();
            try {
                RReadWriteLock cLock = redissonClient.getReadWriteLock(lockKey2);
                RLock r = cLock.readLock();
                r.lock(5, TimeUnit.SECONDS);
                RSet<String> changes = redissonClient.getSet(key2);
                changes.add(SyncCmd.PALL);
                r.unlock();
                log.info("端点 {} 项目 {} 添加协同变更: {}", endpoint, projectId, SyncCmd.PALL);
            } catch (Exception ex) {
                log.error("syncAll 发生异常#2", ex);
            }

            try {
                lockKey2 = String.format(RKey.LOCK_CORP_EP_SYNC_P_PATTERN, endpoint).toUpperCase();
                key2 = String.format(RKey.CORP_EP_SYNC_P_PATTERN, endpoint).toUpperCase();
                RReadWriteLock cLock = redissonClient.getReadWriteLock(lockKey2);
                RLock r = cLock.readLock();
                r.lock(5, TimeUnit.SECONDS);
                RSet<String> changes = redissonClient.getSet(key2);
                changes.add(projectId);
                r.unlock();
            } catch (Exception ex) {
                log.error("syncAll 发生异常#3", ex);
            }
        });
    }


    /**
     * 推送同步指令PT（触发条件：阶段变动（PT0）、签发变动（PT1）、生产变动（PT2））
     */
    @Override
    public void pushSyncCMD_PT(String projectId, String taskPath, String syncCmd) {
        if (StringUtils.isBlank(projectId) || StringUtils.isBlank(taskPath)) {
            log.error("pushSyncCMD_PT 的参数 projectId 和 taskPath 不能为空");
            return;
        }

        List<String> companyIds = listCompanyIdByProjectId(projectId);

        String rootNodeId = getRootNodeIdByTaskPath(taskPath);

        CompletableFuture.runAsync(() -> {
            List<String> endpoints = listMatchEndpoint(companyIds.toArray(new String[companyIds.size()]));
            if (endpoints == null || endpoints.size() == 0)
                return;

            //写入变更
            endpoints.forEach(ep -> {
                try {
                    String lockKey = String.format(RKey.LOCK_CORP_EP_SYNC_P_ID_PATTERN, ep, projectId).toUpperCase();
                    String key = String.format(RKey.CORP_EP_SYNC_P_ID_PATTERN, ep, projectId).toUpperCase();
                    RReadWriteLock cLock = redissonClient.getReadWriteLock(lockKey);
                    RLock r = cLock.readLock();
                    r.lock(5, TimeUnit.SECONDS);
                    RSet<String> changes = redissonClient.getSet(key);
                    changes.add(syncCmd + ":" + rootNodeId);

                    r.unlock();
                    log.info("端点 {} 项目 {} 添加协同变更: {}", ep, projectId, syncCmd + ":" + rootNodeId);
                } catch (Exception ex) {
                    log.error("pushSyncCMD_PT 发生异常#1", ex);
                }

                try {
                    String lockKey = String.format(RKey.LOCK_CORP_EP_SYNC_P_PATTERN, ep).toUpperCase();
                    String key = String.format(RKey.CORP_EP_SYNC_P_PATTERN, ep).toUpperCase();
                    RReadWriteLock cLock = redissonClient.getReadWriteLock(lockKey);
                    RLock r = cLock.readLock();
                    r.lock(5, TimeUnit.SECONDS);
                    RSet<String> changes = redissonClient.getSet(key);
                    changes.add(projectId);
                    r.unlock();
                } catch (Exception ex) {
                    log.error("pushSyncCMD_PT 发生异常#2", ex);
                }
            });
        });
    }

    /**
     * 根据TaskPath截取根节点ID
     */
    private String getRootNodeIdByTaskPath(String taskPath) {
        int index = taskPath.indexOf("-");
        if (index == -1)
            return taskPath;
        return taskPath.substring(0, index);
    }
}
