package com.maoding.common.module.dynamic.service;

import com.maoding.common.module.dynamic.dao.DynamicDAO;
import com.maoding.common.module.dynamic.dao.ZInfoDAO;
import com.maoding.common.module.dynamic.dto.DynamicDTO;
import com.maoding.common.module.dynamic.dto.ProjectDynamicDTO;
import com.maoding.common.module.dynamic.dto.QueryDynamicDTO;
import com.maoding.common.module.dynamic.model.CompanyUserEntity;
import com.maoding.common.module.dynamic.model.DynamicDO;
import com.maoding.constDefine.dynamic.DynamicConst;
import com.maoding.constDefine.netFile.NetFileType;
import com.maoding.core.base.BaseService;
import com.maoding.common.module.dynamic.model.NetFileDO;
import com.maoding.utils.BeanUtils;
import com.maoding.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Created by Chengliang.zhang on 2017/6/5.
 */
@Service("dynamicService")
public class DynamicServiceImpl extends BaseService implements DynamicService{
    @Autowired
    private DynamicDAO dynamicDAO;

    @Autowired
    private ZInfoDAO zInfoDAO;

    /**
     * 获取动态
     *
     * @param query
     */
    @Override
    public ProjectDynamicDTO getProjectDynamic(QueryDynamicDTO query) {
        if ((query.getStartLine() == null) && (query.getPageIndex() != null) && (query.getPageSize() != null)){
            query.setStartLine(query.getPageIndex()*query.getPageSize());
        }
        List<DynamicDTO> dataList = dynamicDAO.listDynamic(query);
        Integer total = dynamicDAO.getLastQueryCount();

        ProjectDynamicDTO dto = new ProjectDynamicDTO();
        dto.setTotal(total);
        if (dataList.size() > 0){
            for (DynamicDTO data : dataList){
                if (data.getTemplate() != null) {
                    String[] params = (data.getExtra() != null) ? data.getExtra().split(DynamicConst.SEPARATOR,3) : null;
                    data.setContent(StringUtils.format(data.getTemplate(),
                            ((data.getOperatorName()!=null)?data.getOperatorName():""),
                            ((data.getNodeName()!=null)?data.getNodeName():""),
                            ((params!=null && params.length>0)?params[0]:""),
                            ((params!=null && params.length>1)?params[1]:""),
                            ((params!=null && params.length>2)?params[2]:"")));
                } else {
                    data.setContent(data.getOperatorName() + DynamicConst.SEPARATOR +
                            data.getNodeName() + DynamicConst.SEPARATOR + data.getExtra());
                }
            }
            dto.setDynamicList(dataList);
        }
        return dto;
    }

    /**
     *
     */
    /**
     * 通用的添加动态方法
     *
     * @param origin 原始数据
     * @param target 修改后的数据
     * @param projectId 操作者针对的项目的编号
     * @param companyId 操作者所在的公司的编号
     * @param userId 操作者的用户编号
     * @param companyUserId 操作者的雇员编号
     */
    @Override
    public <T> void addDynamic(T origin, T target, String projectId, String companyId, String userId, String companyUserId) {
        if ((origin == null) && (target == null)) return;
        //补填缺失参数
        //项目编号
        if ((projectId == null) && (target != null)) projectId = (String)BeanUtils.getProperty(target,"projectId");
        if ((projectId == null) && (origin != null)) projectId = (String)BeanUtils.getProperty(origin,"projectId");

        //操作者公司编号和用户编号
        if (((companyId == null) || (userId == null)) && (companyUserId != null)) {
            CompanyUserEntity cue = zInfoDAO.getCompanyUserByCompanyUserId(companyUserId);
            if (cue != null) {
                companyId = cue.getCompanyId();
                userId = cue.getUserId();
            }
        }
        if ((companyId == null) && (target != null)) companyId = (String)BeanUtils.getProperty(target,"currentCompanyId");
        if ((companyId == null) && (origin != null)) companyId = (String)BeanUtils.getProperty(origin,"currentCompanyId");
        if ((companyId == null) && (target != null)) companyId = (String)BeanUtils.getProperty(target,"companyId");
        if ((companyId == null) && (origin != null)) companyId = (String)BeanUtils.getProperty(origin,"companyId");

        if ((userId == null) && (target != null)) userId = (String)BeanUtils.getProperty(target,"accountId");
        if ((userId == null) && (target != null)) userId = (String)BeanUtils.getProperty(target,"userId");
        if ((userId == null) && (target != null)) userId = (String)BeanUtils.getProperty(target,"updateBy");
        if ((userId == null) && (target != null)) userId = (String)BeanUtils.getProperty(target,"createBy");
        if ((userId == null) && (origin != null)) userId = (String)BeanUtils.getProperty(origin,"accountId");
        if ((userId == null) && (origin != null)) userId = (String)BeanUtils.getProperty(origin,"userId");
        if ((userId == null) && (origin != null)) userId = (String)BeanUtils.getProperty(origin,"updateBy");
        if ((userId == null) && (origin != null)) userId = (String)BeanUtils.getProperty(origin,"createBy");

        //操作者雇员编号
        if ((companyUserId == null) && (companyId != null) && (userId != null)){
            companyUserId = zInfoDAO.getCompanyUserIdByCompanyIdAndUserId(companyId,userId);
        }
        if ((companyUserId == null) && (target != null)) companyUserId = (String)BeanUtils.getProperty(target,"currentCompanyUserId");
        if ((companyUserId == null) && (target != null)) companyUserId = (String)BeanUtils.getProperty(target,"companyUserId");
        if ((companyUserId == null) && (origin != null)) companyUserId = (String)BeanUtils.getProperty(origin,"currentCompanyUserId");
        if ((companyUserId == null) && (origin != null)) companyUserId = (String)BeanUtils.getProperty(origin,"companyUserId");

        //调用相应创建日志方法
        if ((origin instanceof NetFileDO) || (target instanceof NetFileDO)){
            addDynamic(createDynamicFrom((NetFileDO)origin,(NetFileDO)target,projectId,companyId,userId,companyUserId));
        }
    }

    /**
     * 更改文档相关结果
     */
    private DynamicDO createDynamicFrom(NetFileDO origin,NetFileDO target,String projectId,String companyId,String userId,String companyUserId) {
        if ((origin == null) && (target == null)) return null;

        String id = (target != null) ? target.getId() : null;
        if ((id == null) && (origin != null)) id = origin.getId();
        
        DynamicDO dyn = new DynamicDO();
        dyn.setCommon(DynamicConst.TARGET_TYPE_SKY_DRIVE,id,projectId,companyId,userId,companyUserId);

        if (origin == null) {
            dyn.setNodeName(target.getFileName());
            dyn.setType((Objects.equals(target.getType(), NetFileType.FILE)) ?
                    DynamicConst.DYNAMIC_TYPE_UPLOAD_FILE :
                    DynamicConst.DYNAMIC_TYPE_CREATE_DIRECTORY);
        } else if (target == null){
            dyn.setNodeName(origin.getFileName());
            dyn.setType((Objects.equals(origin.getType(), NetFileType.FILE)) ?
                    DynamicConst.DYNAMIC_TYPE_DELETE_FILE :
                    DynamicConst.DYNAMIC_TYPE_DELETE_DIRECTORY);
        } else {
            dyn.setNodeName(origin.getFileName());
            dyn.setType((Objects.equals(origin.getType(), NetFileType.FILE)) ?
                    DynamicConst.DYNAMIC_TYPE_UPDATE_FILE :
                    DynamicConst.DYNAMIC_TYPE_UPDATE_DIRECTORY);
            dyn.setContent(target.getFileName());
        }
        return dyn;
    }

    /**
     * 记录项目动态
     *
     * @param dynamic 项目动态记录
     */
    @Override
    public void addDynamic(DynamicDO dynamic) {
        if (dynamic == null) return;
        if (dynamic.getId() == null) dynamic.setId(StringUtils.buildUUID());
        dynamicDAO.insert(dynamic);
    }
    /**
     * 连续记录多个项目动态
     *
     * @param dynamicList 项目动态记录列表
     */
    @Override
    public void addDynamic(List<DynamicDO> dynamicList) {
        dynamicList.forEach(this::addDynamic);
    }
    /**
     * 处理难以获取某些参数的情况，保持兼容性
     */
    @Override
    public <T> void addDynamic(T origin, T target, String projectId, String companyId, String userId) {
        addDynamic(origin,target,projectId,companyId,userId,null);
    }

    @Override
    public <T> void addDynamic(T origin, T target, String companyId, String userId) {
        addDynamic(origin,target,null,companyId,userId,null);
    }

    @Override
    public <T> void addDynamic(T origin, T target, String companyUserId) {
        addDynamic(origin,target,null,null,null,companyUserId);
    }

    @Override
    public <T> void addDynamic(T origin, T target) {
        addDynamic(origin,target,null,null,null,null);
    }

    /**
     * 方便使用的添加删除方法
     * @param target 要添加的对象
     * @param projectId 操作项目ID
     * @param companyId 操作者公司ID
     * @param userId 操作者用户ID
     * @param companyUserId 操作者雇员ID
     */
    @Override
    public <T> void addCreateDynamic(T target, String projectId, String companyId, String userId, String companyUserId) {
        addDynamic(null,target,projectId,companyId,userId,companyUserId);
    }
    @Override
    public <T> void addCreateDynamic(T target, String projectId, String companyId, String userId) {
        addDynamic(null,target,projectId,companyId,userId,null);
    }
    @Override
    public <T> void addCreateDynamic(T target, String companyId, String userId) {
        addDynamic(null,target,null,companyId,userId,null);
    }
    @Override
    public <T> void addCreateDynamic(T target, String companyUserId) {
        addDynamic(null,target,null,null,null,companyUserId);
    }
    @Override
    public <T> void addCreateDynamic(T target) {
        addDynamic(null,target,null,null,null,null);
    }
    @Override
    public <T> void addDeleteDynamic(T origin, String projectId, String companyId, String userId, String companyUserId) {
        addDynamic(origin,null,projectId,companyId,userId,companyUserId);
    }
    @Override
    public <T> void addDeleteDynamic(T origin, String projectId, String companyId, String userId) {
        addDynamic(origin,null,projectId,companyId,userId,null);
    }

    @Override
    public <T> void addDeleteDynamic(T origin, String companyId, String userId) {
        addDynamic(origin,null,null,companyId,userId,null);
    }

    @Override
    public <T> void addDeleteDynamic(T origin, String companyUserId) {
        addDynamic(origin,null,null,null,null,companyUserId);
    }

    @Override
    public <T> void addDeleteDynamic(T origin) {
        addDynamic(origin,null,null,null,null,null);
    }
}
