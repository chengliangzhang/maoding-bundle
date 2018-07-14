package com.maoding.im.module.imQueue.service;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.im.constDefine.*;
import com.maoding.im.easemob.api.SendMessageApi;
import com.maoding.im.module.imAccount.dao.ImAccountDAO;
import com.maoding.im.module.imAccount.dto.ImAccountDTO;
import com.maoding.im.module.imAccount.model.ImAccountDO;
import com.maoding.im.module.imAccount.service.ImAccountService;
import com.maoding.im.module.imGroup.dao.ImGroupDAO;
import com.maoding.im.module.imGroup.dao.ImGroupMemberDAO;
import com.maoding.im.module.imGroup.dto.ImGroupDTO;
import com.maoding.im.module.imGroup.dto.ImGroupMemberDTO;
import com.maoding.im.module.imGroup.model.ImGroupDO;
import com.maoding.im.module.imGroup.model.ImGroupMemberDO;
import com.maoding.im.module.imGroup.service.ImGroupService;
import com.maoding.im.module.imQueue.dao.ImQueueDAO;
import com.maoding.hxIm.dto.ImQueueDTO;
import com.maoding.im.module.imQueue.dto.ImSendMessageDTO;
import com.maoding.im.module.imQueue.model.ImQueueDO;
import com.maoding.utils.BeanUtils;
import com.maoding.utils.JsonUtils;
import com.maoding.utils.StringUtils;
import io.swagger.client.model.Msg;
import io.swagger.client.model.MsgContent;
import io.swagger.client.model.UserName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.List;

@Service("imQueueService")
public class ImQueueServiceImpl extends BaseService implements ImQueueService {
    private static final Logger logger = LoggerFactory.getLogger(ImQueueServiceImpl.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ImAccountService imAccountService;

    @Autowired
    private ImGroupService imGroupService;

    @Autowired
    private ImAccountDAO imAccountDAO;

    @Autowired
    private ImGroupDAO imGroupDAO;

    @Autowired
    private ImGroupMemberDAO imGroupMemberDAO;

    @Autowired
    private ImQueueDAO imQueueDAO;

    @Autowired
    private SendMessageApi sendMessageApi;


    /**
     * 处理IM账号消息
     */
    @Override
    public ApiResult handleAccountMessage(ImQueueDTO queue) {
        String operation = queue.getOperation();
        if (StringUtils.isNullOrEmpty(operation))
            throw new NullPointerException("IMQueue.operation不能为空");

        //-----批量创建
        if (operation.equalsIgnoreCase(ImOperation.ACCOUNT_CREATE_BATCH)) {
            List<ImAccountDTO> list = listImAccountFromQueue(queue);
            List<ImAccountDTO> validList = Lists.newArrayList();
            List<ImAccountDO> validDOList = Lists.newArrayList();
            for (ImAccountDTO a : list) {
                ImAccountDO accountDO = imAccountDAO.selectByPrimaryKey(a.getAccountId());
                //如果过期，则跳过
                if (queue.getQueueNo() <= accountDO.getLastQueueNo())
                    continue;

                //如果早已删除或已创建好，则跳过
                if (accountDO.getDeleted() || accountDO.getAccountStatus().compareTo(ImAccountStatus.WAIT_CREATE) > 0)
                    continue;

                validList.add(a);
                validDOList.add(accountDO);
            }

            ApiResult apiResult = imAccountService.createBatch(validList);
            if (apiResult.isSuccessful()) {
                for (ImAccountDO accountDO : validDOList) {
                    accountDO.setAccountStatus(ImAccountStatus.ENABLED);
                    accountDO.setLastQueueNo(queue.getQueueNo());
                    accountDO.resetUpdateDate();
                    imAccountDAO.updateWithOptimisticLock(accountDO);
                }
            }
            postProcess(queue, apiResult);
            return apiResult;
        }

        String targetId = queue.getTargetId();
        if (StringUtils.isNullOrEmpty(targetId))
            throw new NullPointerException("IMQueue.targetId不能为空");

        ImAccountDO accountDO = imAccountDAO.selectByPrimaryKey(targetId);
        if (accountDO == null){
            //如果accountDO不存在，则查询是否存在账号
            //ImAccountDO
            throw new NullPointerException("指定的IMAcount不存在，accountId:" + targetId);
        }


        //判断消息是否过期
        if (queue.getQueueNo() <= accountDO.getLastQueueNo()) {
            //所有过期消息视作成功
            postProcess(queue, ApiResult.success(null));
            return ApiResult.success(null);
        }

        //-----删除
        if (operation.equalsIgnoreCase(ImOperation.ACCOUNT_DELETE)) {
            ApiResult apiResult = imAccountService.delete(targetId);
            if (apiResult.isSuccessful()) {
                //标记ImAccount为删除
                accountDO.setDeleted(true);
                accountDO.setLastQueueNo(queue.getQueueNo());
                accountDO.resetUpdateDate();
                imAccountDAO.updateWithOptimisticLock(accountDO);
            }
            postProcess(queue, apiResult);
            return apiResult;
        }

        //-----创建
        if (operation.equalsIgnoreCase(ImOperation.ACCOUNT_CREATE)) {
            //如果早已删除或已创建好，则视作成功消息处理
            if (accountDO.getDeleted() || accountDO.getAccountStatus().compareTo(ImAccountStatus.ENABLED) == 0) {
                postProcess(queue, ApiResult.success(null));
                return ApiResult.success(null);
            }

            ImAccountDTO imAccountDTO = getImAccountFromQueue(queue);
            ApiResult apiResult = imAccountService.create(imAccountDTO);
            if (apiResult.isSuccessful()) {
                //更新ImAccount状态
                accountDO.setAccountStatus(ImAccountStatus.ENABLED);
                accountDO.setLastQueueNo(queue.getQueueNo());
                accountDO.resetUpdateDate();
                imAccountDAO.updateWithOptimisticLock(accountDO);
            }
            postProcess(queue, apiResult);
            return apiResult;
        }

        //-----修改
        //如果已删除，则所有修改都视作成功消息处理
        if (accountDO.getDeleted()) {
            postProcess(queue, ApiResult.success(null));
            return ApiResult.success(null);
        }
        //如果修改时账号仍没创建好，则视作失败消息处理
        if (accountDO.getAccountStatus().compareTo(ImAccountStatus.WAIT_CREATE) == 0) {
            postProcess(queue, ApiResult.failed(null));
            return ApiResult.failed(null);
        }
        ApiResult apiResult = null;
        ImAccountDTO dto = getImAccountFromQueue(queue);
        if (operation.equalsIgnoreCase(ImOperation.ACCOUNT_MODIFY_NICKNAME)) {
            apiResult = imAccountService.modifyNickname(dto);
            if (apiResult.isSuccessful()) {
                accountDO.setLastQueueNo(queue.getQueueNo());
                accountDO.resetUpdateDate();
                imAccountDAO.updateWithOptimisticLock(accountDO);
            }
        } else if (operation.equalsIgnoreCase(ImOperation.ACCOUNT_MODIFY_PASSWORD)) {
            apiResult = imAccountService.modifyPassword(dto);
            if (apiResult.isSuccessful()) {
                accountDO.setLastQueueNo(queue.getQueueNo());
                accountDO.resetUpdateDate();
                imAccountDAO.updateWithOptimisticLock(accountDO);
            }
        }
        postProcess(queue, apiResult);
        return apiResult;
    }

    /**
     * 处理IM群组消息
     */
    @Override
    public ApiResult handleGroupMessage(ImQueueDTO queue) {
        String operation = queue.getOperation();
        if (StringUtils.isNullOrEmpty(operation))
            throw new NullPointerException("IMQueue.operation不能为空");

        String targetId = queue.getTargetId();
        if (StringUtils.isNullOrEmpty(targetId))
            throw new NullPointerException("IMQueue.targetId不能为空");

        ImGroupDO groupDO = imGroupDAO.selectByPrimaryKey(targetId);
        if (groupDO == null)
            throw new NullPointerException("指定的IMGroup不存在，groupId:" + targetId);

        //判断消息是否过期
        if (queue.getQueueNo() <= groupDO.getLastQueueNo()) {
            //所有过期消息视作成功
            postProcess(queue, ApiResult.success(null));
            return ApiResult.success(null);
        }

        //-----删除
        if (operation.equalsIgnoreCase(ImOperation.GROUP_DELETE)) {
            ApiResult apiResult = imGroupService.delete(groupDO.getGroupNo());
            if (apiResult.isSuccessful()) {
                //标记ImGroup为删除
                groupDO.setDeleted(true);
                groupDO.setLastQueueNo(queue.getQueueNo());
                groupDO.resetUpdateDate();
                imGroupDAO.updateWithOptimisticLock(groupDO);
            }
            postProcess(queue, apiResult);
            return apiResult;
        }

        //-----创建
        if (operation.equalsIgnoreCase(ImOperation.GROUP_CREATE)) {
            //如果早已删除或已创建好，则视作成功消息处理
            if (groupDO.getDeleted() || groupDO.getGroupStatus().compareTo(ImGroupStatus.ENABLED) == 0) {
                postProcess(queue, ApiResult.success(null));
                return ApiResult.success(null);
            }

            ImGroupDTO imGroupDTO = getImGroupFromQueue(queue);
            ApiResult apiResult = imGroupService.create(imGroupDTO);
            if (apiResult.isSuccessful()) {
                String groupNo;
                try {
                    groupNo = JsonUtils.readTree(apiResult.getData().toString()).findValue("groupid").asText();
                } catch (IOException e) {
                    throw new NullPointerException("groupNo不能为空，原json：" + apiResult.getData());
                }
                //更新ImGroup状态
                groupDO.setGroupNo(groupNo);
                groupDO.setGroupStatus(ImGroupStatus.ENABLED);
                groupDO.setLastQueueNo(queue.getQueueNo());
                groupDO.resetUpdateDate();
                imGroupDAO.updateWithOptimisticLock(groupDO);

                if (imGroupDTO.getMembers() != null && imGroupDTO.getMembers().size() > 0) {
                    insertMemberBatch(imGroupDTO.getGroupId(), imGroupDTO.getMembers());
                } else {
                    List<ImGroupMemberDTO> dtos = Lists.newArrayList();
                    ImGroupMemberDTO memberDTO = new ImGroupMemberDTO();
                    memberDTO.setAccountId(imGroupDTO.getGroupOwner());
                    dtos.add(memberDTO);
                    insertMemberBatch(imGroupDTO.getGroupId(), dtos);
                }
            } else {
                //非补偿消息 环信调用失败需要删除IMGROUP记录 如自定义群
                if (queue.getIgnoreFix() != null & queue.getIgnoreFix()) {
                    groupDO.setDeleted(true);
                    groupDO.resetUpdateDate();
                    imGroupDAO.updateWithOptimisticLock(groupDO);
                }
            }
            postProcess(queue, apiResult);
            return apiResult;
        }

        //-----修改
        //如果已删除，则所有修改都自动视作成功处理
        if (groupDO.getDeleted()) {
            postProcess(queue, ApiResult.success(null));
            return ApiResult.success(null);
        }
        //如果修改时群组仍没创建好，则视作失败消息处理
        if (groupDO.getGroupStatus().compareTo(ImGroupStatus.WAIT_CREATE) == 0) {
            postProcess(queue, ApiResult.failed(null));
            return ApiResult.failed(null);
        }
        ApiResult apiResult = null;
        ImGroupDTO dto = getImGroupFromQueue(queue);
        if (StringUtils.isNullOrEmpty(dto.getGroupNo()))
            dto.setGroupNo(groupDO.getGroupNo());
        if (operation.equalsIgnoreCase(ImOperation.GROUP_MODIFY_GROUP_NAME)) {
            apiResult = imGroupService.modifyGroupName(dto);
            if (apiResult.isSuccessful()) {
                groupDO.setGroupName(dto.getGroupName());
                groupDO.setLastQueueNo(queue.getQueueNo());
                groupDO.resetUpdateDate();
                imGroupDAO.updateWithOptimisticLock(groupDO);
            }
        } else if (operation.equalsIgnoreCase(ImOperation.GROUP_TRANSFER_GROUP_OWNER)) {
            apiResult = imGroupService.transferGroupOwner(dto);
            if (apiResult.isSuccessful()) {
                groupDO.setGroupOwner(dto.getGroupOwner());
                groupDO.setLastQueueNo(queue.getQueueNo());
                groupDO.resetUpdateDate();
                imGroupDAO.updateWithOptimisticLock(groupDO);
            }
        }
        postProcess(queue, apiResult);
        return apiResult;
    }

    /**
     * 处理IM群组成员消息
     */
    @Override
    public ApiResult handleGroupMemberMessage(ImQueueDTO queue) {
        String operation = queue.getOperation();
        if (StringUtils.isNullOrEmpty(operation))
            throw new NullPointerException("IMQueue.operation不能为空");

        String targetId = queue.getTargetId();
        if (StringUtils.isNullOrEmpty(targetId))
            throw new NullPointerException("IMQueue.targetId不能为空");

        ImGroupDO groupDO = imGroupDAO.selectByPrimaryKey(targetId);
        if (groupDO == null)
            throw new NullPointerException("指定的IMGroup不存在，groupId:" + targetId);

        //如果已删除，则所有成员修改都自动视作成功处理
        if (groupDO.getDeleted()) {
            postProcess(queue, ApiResult.success(null));
            return ApiResult.success(null);
        }
        //如果修改成员时群组仍没创建好，则视作失败消息处理
        if (groupDO.getGroupStatus().compareTo(ImGroupStatus.WAIT_CREATE) == 0) {
            postProcess(queue, ApiResult.failed(null));
            return ApiResult.failed(null);
        }
        ApiResult apiResult = ApiResult.success(null);
        ImGroupDTO dto = getImGroupFromQueue(queue);
        if (StringUtils.isNullOrEmpty(dto.getGroupNo()))
            dto.setGroupNo(groupDO.getGroupNo());
        if (operation.equalsIgnoreCase(ImOperation.GROUP_MEMBER_ADD)) {
            if (dto.getMembers() != null && dto.getMembers().size() > 0) {
                apiResult = imGroupService.addMembers(dto);
                if (apiResult.isSuccessful()) {
                    groupDO.setLastQueueNo(queue.getQueueNo());
                    groupDO.resetUpdateDate();
                    imGroupDAO.updateWithOptimisticLock(groupDO);

                    insertMemberBatch(dto.getGroupId(), dto.getMembers());
                }
            }
        } else if (operation.equalsIgnoreCase(ImOperation.GROUP_MEMBER_DELETE)) {
            if (dto.getMembers() != null && dto.getMembers().size() > 0) {
                apiResult = imGroupService.deleteMembers(dto);
                if (apiResult.isSuccessful()) {
                    groupDO.setLastQueueNo(queue.getQueueNo());
                    groupDO.resetUpdateDate();
                    imGroupDAO.updateWithOptimisticLock(groupDO);

                    deleteMembersBatch(dto.getGroupId(), dto.getMembers());
                }
            }
        }
        postProcess(queue, apiResult);
        return apiResult;
    }

    private void insertMemberBatch(String groupId, List<ImGroupMemberDTO> list) {
        List<ImGroupMemberDO> dos = Lists.newArrayList();
        for (ImGroupMemberDTO dto : list) {
            //判断数据库中是否已经存在该记录

            ImGroupMemberDO memberDO = new ImGroupMemberDO();
            memberDO.initEntity();
            memberDO.setGroupId(groupId);
            if(dto.getOrgId()==null){ //防止组织群没有传递orgId
                memberDO.setOrgId(groupId);
            }else {
                memberDO.setOrgId(dto.getOrgId());
            }
            memberDO.setAccountId(dto.getAccountId());
            memberDO.setCompanyUserId(dto.getCompanyUserId());
            if(StringUtils.isNullOrEmpty(dto.getCompanyUserId())){//从数据库中重新获取，此处为了防止创建组织的时候，companyUserId没有传递过来
                memberDO.setCompanyUserId(imGroupDAO.getCompanyUserId(memberDO));
            }
            if(!StringUtils.isNullOrEmpty(memberDO.getCompanyUserId()) && !this.isExistGroupMember(groupId,memberDO.getCompanyUserId())){//如果不为null的时候再处理
                dos.add(memberDO);
            }
        }
        if(!CollectionUtils.isEmpty(dos)){
            imGroupMemberDAO.BatchInsert(dos);
        }
    }

    /**
     * 查询该群中是否已经存在该成员
     */
    private boolean isExistGroupMember(String groupId,String companyUserId){
        Example example = new Example(ImGroupMemberDO.class);
        example.createCriteria()
                .andCondition("group_id = ", groupId)
                .andCondition("company_user_id = ", companyUserId);
        List<ImGroupMemberDO> list = imGroupMemberDAO.selectByExample(example);
        if(!CollectionUtils.isEmpty(list)){
            return true;
        }
        return false;
    }

    private void deleteMembersBatch(String groupId, List<ImGroupMemberDTO> list) {
        List<ImGroupMemberDO> dos = Lists.newArrayList();
        for (ImGroupMemberDTO dto : list) {
            ImGroupMemberDO memberDO = new ImGroupMemberDO();
            memberDO.setGroupId(groupId);
            memberDO.setAccountId(dto.getAccountId());
            memberDO.setCompanyUserId(dto.getCompanyUserId());
            dos.add(memberDO);
        }
        for (ImGroupMemberDO m : dos)
            imGroupMemberDAO.delete(m);
    }


    /**
     * 处理IM发送消息
     * @param queue
     */
    @Override
    public ApiResult handleSendMessage(ImQueueDTO queue) {
        String operation = queue.getOperation();
        if (StringUtils.isNullOrEmpty(operation))
            throw new NullPointerException("IMQueue.operation不能为空");

        if (!operation.equalsIgnoreCase(ImOperation.SEND_MESSAGE))
            throw new UnsupportedOperationException("未知的IM操作类型");

        ImSendMessageDTO dto = getImSendMessageFromQueue(queue);

        Msg msg = new Msg();
        msg.from(dto.getFromUser());
        UserName userName = new UserName();
        userName.addAll(Lists.newArrayList(dto.getToUsers()));
        msg.target(userName);
        msg.targetType(dto.getTargetType());
        msg.ext(dto.getExt());

        MsgContent msgContent = new MsgContent();
        ApiResult apiResult = null;
        try {
            msgContent.type(MsgContent.TypeEnum.TXT).msg((String)dto.getMsgMap().get("msg"));
            logger.debug(new GsonBuilder().create().toJson(msg));
            msg.setMsg(msgContent);
            apiResult = sendMessageApi.sendMessage(msg);
        } catch (Exception e) {
            logger.error("handleSendMessage发生异常", e);
            apiResult = ApiResult.failed("handleSendMessage发生异常");
        }
        postProcess(queue, apiResult);
        return apiResult;
    }

    /**
     * 拉取并推送修复消息
     */
    @Override
    public void pullAndProcessImQueue(int countPerTime) {
        List<ImQueueDO> list = imQueueDAO.listWaitingQueue(countPerTime);
        if (list != null && list.size() > 0) {
            logger.info("发现IM修复消息 {} 条", list.size());
            ImQueueService srv = (ImQueueService) AopContextCurrentProxy();
            for (ImQueueDO queue : list)
                srv.processImQueue(queue);
        }
    }

    /**
     * 处理修复消息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void processImQueue(ImQueueDO queue) {
        try {
            String operation = queue.getOperation();
            ImQueueDTO dto = new ImQueueDTO();
            BeanUtils.copyProperties(queue, dto);
            if (StringUtils.startsWithIgnoreCase(operation, ImDestination.ACCOUNT + ":")) {
                jmsTemplate.convertAndSend(ImDestination.ACCOUNT, dto);
            } else if (StringUtils.startsWithIgnoreCase(operation, ImDestination.GROUP + ":")) {
                jmsTemplate.convertAndSend(ImDestination.GROUP, dto);
            } else if (StringUtils.startsWithIgnoreCase(operation, ImDestination.GROUP_MEMBER + ":")) {
                jmsTemplate.convertAndSend(ImDestination.GROUP_MEMBER, dto);
            }else if(StringUtils.startsWithIgnoreCase(operation, ImDestination.SEND_MESSAGE)){
                jmsTemplate.convertAndSend(ImDestination.SEND_MESSAGE, dto);
            }
            //此处的更新动作应该要屏蔽，因为在执行中，都执行了postProcess（已经执行了update操作）
//            queue.setQueueStatus(ImQueueStatus.PROCESSING);
//            imQueueDAO.updateWithOptimisticLock(queue);
        } catch (Exception e) {
            logger.error("pullAndPushImQueue 发生异常", e);
        }
    }

    private List<ImAccountDTO> listImAccountFromQueue(ImQueueDTO queue) {
        List<ImAccountDTO> dto = null;
        try {
            dto = JsonUtils.json2list(queue.getContent(), ImAccountDTO.class);
        } catch (Exception e) {

        }
        return dto;
    }

    private ImAccountDTO getImAccountFromQueue(ImQueueDTO queue) {
        ImAccountDTO dto = null;
        try {
            dto = JsonUtils.json2pojo(queue.getContent(), ImAccountDTO.class);
        } catch (Exception e) {
            logger.error("getImAccountFromQueue 发生异常", e);
        }
        return dto;
    }

    private ImGroupDTO getImGroupFromQueue(ImQueueDTO queue) {
        ImGroupDTO dto = null;
        try {
            dto = JsonUtils.json2pojo(queue.getContent(), ImGroupDTO.class);
        } catch (Exception e) {
            logger.error("getImGroupFromQueue 发生异常", e);
        }
        return dto;
    }

    private ImSendMessageDTO getImSendMessageFromQueue(ImQueueDTO queue) {
        ImSendMessageDTO dto = null;
        try {
            dto = JsonUtils.json2pojo(queue.getContent(), ImSendMessageDTO.class);
        } catch (Exception e) {
            logger.error("getImSendMessageFromQueue 发生异常", e);
        }
        return dto;
    }

    /** 后期处理 **/
    private void postProcess(ImQueueDTO queue, ApiResult apiResult) {
        if (queue.getIgnoreFix() != null && queue.getIgnoreFix())
            return;
        if (apiResult.isSuccessful()) {
            if (queue.isFixMode()) {
                //标记删除并更新纪录到IM_QUEUE
                ImQueueDO queueDO = imQueueDAO.selectByPrimaryKey(queue.getId());
                queueDO.setQueueStatus(ImQueueStatus.END);
                queueDO.setDeleted(true);
                queueDO.resetUpdateDate();
                imQueueDAO.updateWithOptimisticLock(queueDO);
            }
        } else {
            //判断是否补偿修正
            if (queue.isFixMode()) {
                //更新纪录到IM_QUEUE
                ImQueueDO queueDO = imQueueDAO.selectByPrimaryKey(queue.getId());
                queueDO.setReason(apiResult.getMsg());
                queueDO.setRetry(queue.getRetry() + 1);
                queueDO.setQueueStatus(ImQueueStatus.Waiting);
                queueDO.resetUpdateDate();
                imQueueDAO.updateWithOptimisticLock(queueDO);
            } else {
                //插入新纪录到IM_QUEUE
                ImQueueDO queueDO = new ImQueueDO();
                queueDO.initEntity();
                queueDO.setQueueNo(queue.getQueueNo());
                queueDO.setOperation(queue.getOperation());
                queueDO.setTargetId(queue.getTargetId());
                queueDO.setContent(queue.getContent());
                queueDO.setReason(apiResult.getMsg());
                queueDO.setRetry(queue.getRetry() == null ? 0 : queue.getRetry() + 1);
                queueDO.setQueueStatus(ImQueueStatus.Waiting);
                queueDO.setDeleted(false);
                queueDO.setUpVersion(0L);
                imQueueDAO.insert(queueDO);
            }
        }
    }
}

