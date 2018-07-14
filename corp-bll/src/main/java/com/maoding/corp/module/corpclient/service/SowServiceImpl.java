package com.maoding.corp.module.corpclient.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maoding.constDefine.corp.CorpServer;
import com.maoding.constDefine.corp.SowServer;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.corp.module.corpclient.dto.*;
import com.maoding.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuwq on 2017/06/09.
 * 面向协同推送的服务
 */
@Service("sowService")
public class SowServiceImpl extends BaseService implements SowService {

    private static final Logger log = LoggerFactory.getLogger(SowServiceImpl.class);

    @Autowired
    private SyncService syncService;

    /**
     * 根据组织Id获取人员信息
     */
    @Override
    public ApiResult pushUserByCompanyId(String companyId, LocalDateTime syncDate) throws Exception {
        Map<String, Object> param = Maps.newHashMap();
        param.put("companyId", companyId);
        if (syncDate != null)
            param.put("syncDate", syncDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ApiResult apiResult = syncService.pullFromCorpServer(param, CorpServer.URL_LIST_USER_BY_COMPANY_ID);
        if (apiResult.isSuccessful()) {

            // SetUsers(int[] statuss, string[] ids, string[] names, string[] loginIds, string[] passwords = null, string[] descriptions = null, string[] specialtyIds = null)

            List<CoUserDTO> users = JsonUtils.json2list(JsonUtils.obj2json(apiResult.getData()), CoUserDTO.class);

            if (users.size() == 0)
                return ApiResult.success(null, null);


            List<String> statuss = Lists.newArrayList();
            List<String> ids = Lists.newArrayList();
            List<String> names = Lists.newArrayList();
            List<String> loginIds = Lists.newArrayList();

            for (int i = 0; i < users.size(); i++) {
                CoUserDTO user = users.get(i);
                if (user.getStatus() != null)
                    statuss.add(user.getStatus().equals("0") ? "1" : "0");
                else
                    statuss.add("0");

                ids.add(user.getAccountId());
                names.add(user.getAccountName());
                loginIds.add(user.getCellphone());
            }

            CoUsersDTO coUsers = new CoUsersDTO();
            coUsers.setStatuss(statuss);
            coUsers.setIds(ids);
            coUsers.setNames(names);
            coUsers.setLoginIds(loginIds);

            //推送到对方协同服务端
            PushResult pushResult = syncService.postToSOWServer(coUsers, SowServer.URL_SET_USERS);
            if (!pushResult.isSuccessful()) {
                log.error("setUsers 失败 ，postData:{} pushResult:{}", JsonUtils.json2map(JsonUtils.obj2json(coUsers)), JsonUtils.obj2json(pushResult));
            }

            return ApiResult.success(null, null);
        }
        return ApiResult.failed(null, null);
    }

    /**
     * 通过组织Id获取组织的项目
     */
    @Override
    public ApiResult pushProjectByCompanyId2(String companyId, LocalDateTime syncDate) throws Exception {
        /*Map<String, Object> param = Maps.newHashMap();
        param.put("companyId", companyId);
        if (syncDate != null)
            param.put("syncDate", syncDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ApiResult apiResult = syncService.pullFromCorpServer(param, CorpServer.URL_LIST_PROJECT_BY_COMPANY_ID);
        if (apiResult.isSuccessful()) {
            List<ProjectDTO> projects = JsonUtils.json2list(JsonUtils.obj2json(apiResult.getData()), ProjectDTO.class);

            for (ProjectDTO p : projects) {
                CoProjectDTO coProject = new CoProjectDTO();
                coProject.setId(p.getProjectId());
                coProject.setCode(p.getPstatus() + "nnn");
                coProject.setName(p.getProjectName());
                //coProject.setOwerUserId(p.getCreateBy());
                coProject.setStatus(p.getPstatus().equals("0") ? 1 : 0);

                //推送到对方协同服务端
                PushResult pushResult = syncService.postToSOWServer(coProject, SowServer.URL_SET_PROJECT);
                if (pushResult.isSuccessful()) {

                    //判断项目是否有效，推送子节点信息
                    if (coProject.getStatus() == 1)
                        setProjectNodes(p.getProjectId(), syncDate);

                } else {
                    log.error("setProject 失败 ，postData:{} pushResult:{}", JsonUtils.obj2json(coProject), JsonUtils.obj2json(pushResult));
                }
            }

            return ApiResult.success(null, null);
        }*/
        return ApiResult.failed(null, null);
    }

    /**
     * 设置项目
     */
    @Override
    public ApiResult setProject(String projectId, LocalDateTime syncDate, Boolean pushAllNodes) throws Exception {
        ApiResult apiResult = syncService.pullFromCorpServer(CorpServer.URL_GET_PROJECT_BY_ID + '/' + projectId);
        if (!apiResult.isSuccessful()) {
            log.error("拉取项目（{}）失败：{}", projectId, apiResult.getMsg());
            return ApiResult.failed(null, null);
        }

        ProjectDTO resultDTO = JsonUtils.obj2pojo(apiResult.getData(), ProjectDTO.class);

        CoProjectDTO coProject = new CoProjectDTO();
        coProject.setId(resultDTO.getProjectId());
        coProject.setCode(resultDTO.getPstatus() + "nnn");
        coProject.setName(resultDTO.getProjectName());
        coProject.setStatus(resultDTO.getPstatus().equals("0") ? 1 : 0);

        //推送到对方协同服务端
        PushResult pushResult = syncService.postToSOWServer(coProject, SowServer.URL_SET_PROJECT);
        if (pushResult.isSuccessful()) {

            //判断项目是否有效，推送子节点信息
            if (coProject.getStatus() == 1 && pushAllNodes)
                return setProjectNodes(projectId, syncDate, null);

            return ApiResult.success(null, null);

        } else {
            log.error("setProject 失败 ，postData:{} pushResult:{}", JsonUtils.obj2json(coProject), JsonUtils.obj2json(pushResult));
            return ApiResult.failed(null, null);
        }
    }


    /**
     * 设置项目节点（含任务成员状态信息）
     * @param phaseId 设为NULL则表示所有阶段
     * @throws Exception
     */
    @Override
    public ApiResult setProjectNodes(String projectId, LocalDateTime syncDate, String phaseId) throws Exception {
        Map<String, Object> param = Maps.newHashMap();
        param.put("projectId", projectId);
        if (syncDate != null)
            param.put("syncDate", syncDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ApiResult apiResult = syncService.pullFromCorpServer(param, CorpServer.URL_LIST_NODE);
        if (apiResult.isSuccessful()) {
            List<CoProjectPhaseDTO> nodes = JsonUtils.json2list(JsonUtils.obj2json(apiResult.getData()), CoProjectPhaseDTO.class);
            if (nodes.size() > 0) {
                for (CoProjectPhaseDTO node : nodes) {

                    if (phaseId != null && !node.getProjectPhaseId().equalsIgnoreCase(phaseId))
                        continue;

                    //项目阶段
                    CoProjectPhaseDTO coPhase = new CoProjectPhaseDTO();
                    BeanUtils.copyProperties(node, coPhase);
                    coPhase.setTasks(null);

                    //推送阶段
                    PushResult pushResult = syncService.postToSOWServer(coPhase, SowServer.URL_SET_PROJECT_PHASE);
                    if (!pushResult.isSuccessful()) {
                        //有可能因为项目推送失败导致空引用，所以这里重推项目
                        //TODO 后期要求对方先判断
                        setProject(projectId, syncDate, false);
                        pushResult = syncService.postToSOWServer(coPhase, SowServer.URL_SET_PROJECT_PHASE);
                    }

                    if (pushResult.isSuccessful()) {

                        //判断阶段是否有效，推送子任务
                        if (coPhase.getStatus() == 1)
                            setTasks(projectId, node.getTasks());

                    } else {
                        log.error("setProjectPhase 失败 ，postData:{} pushResult:{}", JsonUtils.obj2json(coPhase), JsonUtils.obj2json(pushResult));
                    }
                }

                //发布项目（同步任务文件夹）
                return publicProject(projectId);
            }
        }
        return ApiResult.failed(null, null);
    }

    /**
     * 设置任务
     */
    private ApiResult setTasks(String projectId, List<CoTaskDTO> coTasks) throws Exception {
        if (coTasks == null || coTasks.size() == 0)
            return ApiResult.success(null, null);

        Map<String, Object> param = Maps.newHashMap();
        param.put("tasks", coTasks);

        //推送到对方协同服务端
        PushResult pushResult = syncService.postToSOWServer(param, SowServer.URL_SET_PROJECT_TASKS);
        if (pushResult.isSuccessful()) {
            return ApiResult.success(null, null);
        } else {
            log.error("setTasks 失败 ，projectId:{} postData:{} pushResult:{}", projectId, JsonUtils.obj2json(param), JsonUtils.obj2json(pushResult));
        }

        return ApiResult.failed(null, null);
    }

    /**
     * 发布项目（同步任务文件夹）
     */
    public ApiResult publicProject(String projectId) throws Exception {
        Map<String, Object> param = Maps.newHashMap();
        param.put("projectId", projectId);

        //推送到对方协同服务端
        PushResult pushResult = syncService.postToSOWServer(param, SowServer.URL_PUBLIC_PROJECT);
        if (pushResult.isSuccessful()) {
            return ApiResult.success(null, null);
        } else {
            log.error("publicProject 失败 ，projectId:{} pushResult:{}", projectId, JsonUtils.obj2json(pushResult));
        }

        return ApiResult.failed(null, null);
    }

    /**
     * 根据组织ID获取协同占用空间
     */
    @Override
    public ApiResult getCorpSizeByCompanyId(String companyId) throws Exception {
        Map<String, Object> param = Maps.newHashMap();
        param.put("teamId", companyId);

        //推送到对方协同服务端
        PushResult pushResult = syncService.postToSOWServer(param, SowServer.URL_GET_SUM_BY_TEAM_ID);
        if (pushResult.isSuccessful()) {
            return ApiResult.success(null, pushResult.getAppendData());
        } else {
            log.error("getCorpSizeByCompanyI 失败 ，companyId:{} pushResult:{}", companyId, JsonUtils.obj2json(pushResult));
        }

        return ApiResult.failed(null, null);
    }
}
