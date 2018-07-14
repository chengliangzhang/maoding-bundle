package com.maoding.im.module.imGroup.dto;

import java.util.List;

public class ImGroupDTO {

    /** 组织ID */
    private String orgId;

    /** 自定义群组所属公司id */
    private String rootOrgId;

    /*****groupId = orgId = id*****/
    private String groupId;

    /** 环信群ID */
    private String groupNo;

    /** 群名 */
    private String groupName;

    /** 群类型：0-公司群 1-部门群 2-自定义群 3-项目群 */
    private Integer groupType;

    /** 群主ID，逗号分隔 */
    private String groupOwner;

    /** 群状态，0-建立中 1-已启用 2-已禁用 */
    private Integer groupStatus;

    /** 群头像，从grouppath开始 */
    private String groupImg;

    /** 成员账号Id，AccountId **/
    private List<ImGroupMemberDTO> members;

    private String nodeId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRootOrgId() {
        return rootOrgId;
    }

    public void setRootOrgId(String rootOrgId) {
        this.rootOrgId = rootOrgId;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public Integer getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(Integer groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getGroupImg() {
        return groupImg;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<ImGroupMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<ImGroupMemberDTO> members) {
        this.members = members;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
