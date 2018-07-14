package com.maoding.im.module.imGroup.service;

import com.google.common.collect.Lists;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.im.easemob.api.ChatGroupApi;
import com.maoding.im.module.imGroup.dao.ImGroupDAO;
import com.maoding.im.module.imGroup.dao.ImGroupMemberDAO;
import com.maoding.im.module.imGroup.dto.ImGroupDTO;
import com.maoding.im.module.imGroup.dto.ImGroupMemberDTO;
import com.maoding.im.module.imGroup.model.ImGroupDO;
import com.maoding.im.module.imGroup.model.ImGroupMemberDO;
import com.maoding.utils.JsonUtils;
import com.maoding.utils.StringUtils;
import io.swagger.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service("imGroupService")
public class ImGroupServiceImpl extends BaseService implements ImGroupService {
    private static final Logger logger = LoggerFactory.getLogger(ImGroupServiceImpl.class);

    @Autowired
    private ChatGroupApi chatGroupApi;

    @Autowired
    private ImGroupDAO imGroupDAO;

    @Autowired
    private ImGroupMemberDAO imGroupMemberDAO;

    /** 创建环信群组 */
    @Override
    public ApiResult create(ImGroupDTO dto) {
        Group group = new Group();
        group.setGroupname(dto.getGroupName());
        group.setOwner(dto.getGroupOwner());
        group.setDesc(StringUtils.isBlank(dto.getGroupImg()) ? null : dto.getGroupImg());
        group.setPublic(true);
        group.setMaxusers(2000);
        if (dto.getMembers() != null && dto.getMembers().size() > 0) {
            List<String> ids = Lists.newArrayList();
            dto.getMembers().forEach(c -> ids.add(c.getAccountId()));
            UserName memberIds = new UserName();
            memberIds.addAll(ids);
            group.setMembers(memberIds);
        }
        //group.setApproval(true);
        return chatGroupApi.createChatGroup(group);
    }

    /** 删除群 */
    @Override
    public ApiResult delete(String groupNo) {
        return chatGroupApi.deleteChatGroup(groupNo);
    }

    /** 修改环信群组名 */
    @Override
    public ApiResult modifyGroupName(ImGroupDTO dto) {
        ModifyGroup group = new ModifyGroup();
        group.setGroupname(dto.getGroupName());
        return chatGroupApi.modifyChatGroup(dto.getGroupNo(), group);
    }

    /** 移交环信群主 */
    @Override
    public ApiResult transferGroupOwner(ImGroupDTO dto) {
        NewOwner newOwner = new NewOwner();
        newOwner.newowner(dto.getGroupOwner());
        return chatGroupApi.transferChatGroupOwner(dto.getGroupNo(), newOwner);
    }

    /** 增加群成员 */
    @Override
    public ApiResult addMembers(ImGroupDTO dto) {
        UserName memberIds = new UserName();
        List<String> ids = Lists.newArrayList();
        dto.getMembers().forEach(c -> ids.add(c.getAccountId()));
        memberIds.addAll(ids);
        UserNames userNames = new UserNames().usernames(memberIds);
        return chatGroupApi.addBatchUsersToChatGroup(dto.getGroupNo(), userNames);
    }

    /** 移除群成员 */
    @Override
    public ApiResult deleteMembers(ImGroupDTO dto) {
        if (dto.getMembers() == null || dto.getMembers().size() == 0)
            return ApiResult.success(null);

        List<String> ids = Lists.newArrayList();
        dto.getMembers().forEach(c -> ids.add(c.getAccountId()));
        String[]  memberIds = ids.toArray(new String[ids.size()]);

        return chatGroupApi.removeBatchUsersFromChatGroup(dto.getGroupNo(), memberIds);
    }

    @Override
    public void groupSyncIm() {
        List<ImGroupDTO> list = imGroupDAO.selectAllCompany();
        createGroup(list,imGroupDAO,imGroupMemberDAO);
    }


    private String createGroup(List<ImGroupDTO> groupList, ImGroupDAO imGroupDAO,ImGroupMemberDAO imGroupMemberDAO) {
        String msg = "";
//        int threadNum = 8;
//        final CountDownLatch cdl = new CountDownLatch(threadNum);
//        long starttime = System.currentTimeMillis();
//        for (int k = 1; k <= threadNum; k++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        for (int i = 0; i < groupList.size(); i++) {
//                            ImGroupDTO group = groupList.get(i);
//                            if(group.getGroupId()==null){
//                                ApiResult api = create(group);
//                                if(api.getCode().equals("0")){
//                                    ImGroupDO imGroup = new ImGroupDO();
//                                    imGroup.setLastQueueNo(0L);
//                                    imGroup.setGroupName(group.getGroupName());
//                                    imGroup.setDeleted(false);
//                                    imGroup.setGroupOwner(group.getGroupOwner());
//                                    imGroup.setGroupStatus(1);
//                                    imGroup.setOrgId(group.getOrgId());
//                                    imGroup.setId(group.getOrgId());
//                                    imGroup.setUpVersion(0L);
//                                    imGroup.setGroupType(0);
//                                    imGroupDAO.insert(imGroup);
//                                    List<ImGroupMemberDTO> memberList = imGroupDAO.selectGroupMemberByOrgId(group.getOrgId());
//                                    for(ImGroupMemberDTO member:memberList) {
//                                        ImGroupMemberDO im = new ImGroupMemberDO();
//                                        im.initEntity();
//                                        im.setGroupId(imGroup.getId());
//                                        im.setOrgId(imGroup.getOrgId());
//                                        im.setAccountId(member.getAccountId());
//                                        im.setCompanyUserId(member.getCompanyUserId());
//                                        imGroupMemberDAO.insert(im);
//                                    }
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        cdl.countDown();
//                    }
//                }
//            }).start();
//        }
//        try {
//            cdl.await();
//            long spendtime = System.currentTimeMillis() - starttime;
//            System.out.println(threadNum + "个线程花费时间:" + spendtime);
//            msg = "更新成功";
//        } catch (InterruptedException e) {
//            msg = "更新失败";
//        }

        for (int i = 0; i < groupList.size(); i++) {
            ImGroupDTO group = groupList.get(i);
            if(group.getGroupId()==null){
                ApiResult api = create(group);
                if(api.getCode().equals("0")){
                    ImGroupDO imGroup = new ImGroupDO();
                    imGroup.setLastQueueNo(0L);
                    imGroup.setGroupName(group.getGroupName());
                    imGroup.setDeleted(false);
                    imGroup.setGroupOwner(group.getGroupOwner());
                    imGroup.setGroupStatus(1);
                    try {
                      String  groupNo = JsonUtils.readTree(api.getData().toString()).findValue("groupid").asText();
                        imGroup.setGroupNo(groupNo);
                    } catch (IOException e) {
                        throw new NullPointerException("groupNo不能为空，原json：" + api.getData());
                    }
                    imGroup.setOrgId(group.getOrgId());
                    imGroup.setId(group.getOrgId());
                    imGroup.setUpVersion(0L);
                    imGroup.setGroupType(0);
                    imGroupDAO.insert(imGroup);
                    List<ImGroupMemberDTO> memberList = imGroupDAO.selectGroupMemberByOrgId(group.getOrgId());
                    group.setMembers(memberList);
                    group.setGroupNo(imGroup.getGroupNo());
                    addMembers(group);
                    for(ImGroupMemberDTO member:memberList) {
                        ImGroupMemberDO im = new ImGroupMemberDO();
                        im.initEntity();
                        im.setGroupId(imGroup.getId());
                        im.setOrgId(imGroup.getOrgId());
                        im.setAccountId(member.getAccountId());
                        im.setCompanyUserId(member.getCompanyUserId());
                        imGroupMemberDAO.insert(im);
                    }

                }
            }
        }
        return msg;
    }
}
