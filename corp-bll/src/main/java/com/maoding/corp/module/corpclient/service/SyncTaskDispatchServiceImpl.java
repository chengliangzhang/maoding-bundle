package com.maoding.corp.module.corpclient.service;

import com.google.common.collect.Lists;
import com.maoding.constDefine.corp.SyncCmd;
import com.maoding.constDefine.corp.SyncPriority;
import com.maoding.constDefine.corp.TaskStatus;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.corp.module.corpclient.dao.SyncTaskDAO;
import com.maoding.corp.module.corpclient.model.SyncTaskDO;
import com.maoding.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("syncTaskDispatchService")
public class SyncTaskDispatchServiceImpl extends BaseService implements SyncTaskDispatchService {

    private static final Logger log = LoggerFactory.getLogger(SyncTaskDispatchServiceImpl.class);

    @Autowired
    private SyncTaskDAO syncTaskDAO;

    @Autowired
    private SyncTaskService syncTaskService;

    /**
     * 启动时把任务状态为“执行中”的重置为“等待执行”
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void resetTaskStatusOnStartup() {
        syncTaskDAO.updateRunngingAsWaitRunningStatus();
    }

    /**
     * 执行指定端点下同步任务
     */
    @Override
    public void runTasksByEndpoint(String endpoint) throws Exception {
        if (StringUtils.isBlank(endpoint))
            throw new RuntimeException("拉取变更失败，endpoint为空");

        //查询未完成任务
        List<SyncTaskDO> tasks = syncTaskDAO.listUnfinishedTask(endpoint);
        List<SyncTaskDO> cuTasks = tasks.stream().filter(t -> t.getSyncCmd().equalsIgnoreCase(SyncCmd.CU) || t.getSyncCmd().equalsIgnoreCase(SyncCmd.CD)).collect(Collectors.toList());
        if (!runTasksByPriority(endpoint, SyncPriority.L1, cuTasks))
            return;

        List<SyncTaskDO> puTasks = tasks.stream().filter(t -> t.getSyncCmd().equalsIgnoreCase(SyncCmd.PALL) || t.getSyncCmd().equalsIgnoreCase(SyncCmd.PU)).collect(Collectors.toList());
        if (!runTasksByPriority(endpoint, SyncPriority.L2, puTasks))
            return;

        List<SyncTaskDO> ptTasks = tasks.stream().filter(t -> t.getSyncCmd().equalsIgnoreCase(SyncCmd.PT)).collect(Collectors.toList());
        runTasksByPriority(endpoint, SyncPriority.L3, ptTasks);
    }


    /**
     * 执行指定优先级的任务
     */
    private boolean runTasksByPriority(String endpoint, int syncPriority, List<SyncTaskDO> tasks) {
        if (tasks == null || tasks.size() <= 0)
            return true;

        log.info("端点 {} 开始 {} 级任务", endpoint, syncPriority);

        //已处理的任务（不含重复）
        List<SyncTaskDO> handledTasks = Lists.newArrayList();

        //阻塞数
        final int[] awaitCount = {0};
        CountDownLatch latch;
        Semaphore sp;

        //判断是否3级任务
        if (syncPriority == SyncPriority.L3) {

            //按项目ID分组
            Map<String, List<SyncTaskDO>> groups = tasks.stream().collect(Collectors.groupingBy(SyncTaskDO::getProjectId));

            //过虑重复任务
            Map<String, List<SyncTaskDO>> distinctGroups = new HashMap<>();
            groups.forEach((k, v) -> {
                List<SyncTaskDO> distinctTasks = distinctTasks(tasks);
                distinctGroups.put(k, distinctTasks);
                awaitCount[0] += distinctTasks.size();
            });

            latch = new CountDownLatch(awaitCount[0]);
            sp = new Semaphore(3);

            //并发执行同步逻辑
            distinctGroups.forEach((k, v) -> {
                CompletableFuture.runAsync(() -> {
                    try {
                        sp.acquire();

                        v.forEach(t -> {
                            try {
                                ApiResult apiResult = syncTaskService.runOneTask(t.getId());
                                handledTasks.add((SyncTaskDO) apiResult.getData());
                            } catch (Exception e) {
                                log.error(String.format("端点 %s 执行 %s 级任务发生异常（t：%s）", endpoint, syncPriority, t.getId()), e);
                            } finally {
                                latch.countDown();
                            }
                        });

                    } catch (InterruptedException e) {
                        log.error(String.format("端点 %s 执行 %s 级任务发生异常（sp.acquire）", endpoint, syncPriority), e);
                    } finally {
                        sp.release();
                    }
                });
            });

        } else {

            //过虑重复任务
            List<SyncTaskDO> distinctTasks = distinctTasks(tasks);
            awaitCount[0] = distinctTasks.size();

            latch = new CountDownLatch(awaitCount[0]);
            sp = new Semaphore(3);

            //并发执行同步逻辑
            distinctTasks.forEach(t -> CompletableFuture.runAsync(() -> {
                try {
                    sp.acquire();
                    ApiResult apiResult = syncTaskService.runOneTask(t.getId());
                    handledTasks.add((SyncTaskDO) apiResult.getData());
                } catch (InterruptedException e) {
                    log.error(String.format("端点 %s 执行 %s 级任务发生异常（t：%s）", endpoint, syncPriority, t.getId()), e);
                } finally {
                    sp.release();
                }
                latch.countDown();
            }));
        }

        //阻塞
        try {
            latch.await(awaitCount[0] * 2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error(String.format("端点 %s 执行 %s 级任务发生异常（latch.await）", endpoint, syncPriority), e);
        }

        //判断同步任务是否还存在未完成任务，若存在则返回等待下一次重试
        long unfinished = handledTasks.stream().filter(t -> t.getTaskStatus() < TaskStatus.Finished).count();
        if (unfinished > 0) {
            log.info(String.format("端点 %s 结束 %s 级任务（未完成：%s/%s，await：%s）", endpoint, syncPriority, unfinished, handledTasks.size(), awaitCount[0]));
            return false;
        } else {
            log.info(String.format("端点 %s 结束 %s 级任务（%s，await：%s）", endpoint, syncPriority, handledTasks.size(), awaitCount[0]));
            return true;
        }
    }

    /**
     * 过滤重复任务
     */
    private List<SyncTaskDO> distinctTasks(List<SyncTaskDO> tasks) {
        List<SyncTaskDO> distinctTasks = Lists.newArrayList();
        for (SyncTaskDO t : tasks) {
            List<SyncTaskDO> sameTasks = distinctTasks.stream().filter(x -> isSameTask(x, t)).collect(Collectors.toList());
            if (sameTasks != null && sameTasks.size() > 0) {
                if (syncTaskService.skipOneTask(t).isSuccessful())
                    log.info(String.format("端点 %s 执行 %s 级任务发现重复，自动跳过（t1：%s，t2：%s）", t.getCorpEndpoint(), t.getSyncPriority(), sameTasks.get(0).getId(), t.getId()));
            } else
                distinctTasks.add(t);
        }
        return distinctTasks;
    }

    /**
     * 是否同一种任务
     */
    private boolean isSameTask(SyncTaskDO t1, SyncTaskDO t2) {
        return StringUtils.equalsIgnoreCase(t1.getCorpEndpoint(), t2.getCorpEndpoint())
                && StringUtils.equalsIgnoreCase(t1.getCompanyId(), t2.getCompanyId())
                && StringUtils.equalsIgnoreCase(t1.getProjectId(), t2.getProjectId())
                && StringUtils.equalsIgnoreCase(t1.getProjectPhaseId(), t2.getProjectPhaseId())
                && t1.getSyncPriority().compareTo(t2.getSyncPriority()) == 0
                && StringUtils.equalsIgnoreCase(t1.getSyncCmd(), t2.getSyncCmd());
    }

    /**
     * 执行指定协同端下的任务
     */
    private void runTasks(String corpEndpoint) {
        /*//判断状态是否为执行中
        if (group.getTaskGroupStatus() == TaskGroupStatus.Running) {
            long seconds = Duration.between(group.getLastEntry(), LocalDateTime.now()).getSeconds();
            long minutes = seconds / 60;
            if (minutes >= group.getTaskAmount() * 3) {
                log.info("{}-{} 任务组：{} 之前执行了 {} 分钟，已超时", corpEndpoint, companyId, group.getId(), minutes);
                if (syncTaskGroupDao.updateAsWaitRunningStatus(group.getId(), group.getTaskGroupStatus()) > 0) {
                    group.setTaskGroupStatus(TaskGroupStatus.WaitRuning);
                    log.info("{}-{} 任务组：{} 更新状态为：待执行", corpEndpoint, companyId, group.getId());
                }
            } else
                log.info("{}-{} 任务组：{} 之前执行了 {} 分钟", corpEndpoint, companyId, group.getId(), minutes);
            return;
        }*/
    }
}
