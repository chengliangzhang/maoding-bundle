package com.maoding.admin.module.system.service;

import com.maoding.admin.module.system.dao.OperatePermissionDAO;
import com.maoding.admin.module.system.dao.RoleDAO;
import com.maoding.admin.module.system.dao.RolePermissionDAO;
import com.maoding.admin.module.system.dto.*;
import com.maoding.admin.module.system.model.OperatePermissionDO;
import com.maoding.admin.module.system.model.RolePermissionDO;
import com.maoding.admin.module.system.model.ViewRoleDO;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.utils.BeanUtils;
import com.maoding.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service("systemService")
public class SystemServiceImpl extends BaseService implements SystemService {

    @Autowired
    private OperatePermissionDAO operatePermissionDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private RolePermissionDAO rolePermissionDAO;

    @Override
    public ApiResult saveView(SaveViewDTO dto) throws Exception {
        OperatePermissionDO viewDO = new OperatePermissionDO();
        BeanUtils.copyProperties(dto,viewDO);
        int i = 0;
        if(StringUtils.isNullOrEmpty(dto.getId())){
            viewDO.initEntity();
            //设置code
            viewDO.setCode(this.getMaxViewCode(dto.getPid()));
            viewDO.setSeq(this.operatePermissionDAO.getMaxSeq(dto.getPid()));
            viewDO.setModuleType(1);
            i = operatePermissionDAO.insert(viewDO);
        }else {
            i = operatePermissionDAO.updateByPrimaryKeySelective(viewDO);
        }
        return i>0?ApiResult.success("操作成功"):ApiResult.failed("操作失败");
    }

    @Override
    public ApiResult saveOperatePermission(SaveOperatePermissionDTO dto) throws Exception {
        OperatePermissionDO permissionDO = new OperatePermissionDO();
        BeanUtils.copyProperties(dto,permissionDO);
        int i = 0;
        if(StringUtils.isNullOrEmpty(dto.getId())){
            permissionDO.initEntity();
            permissionDO.setPid(dto.getViewId());
            permissionDO.setCode(this.getMaxViewCode(dto.getViewId()));//此处的id通过code 拼接
            permissionDO.setSeq(this.operatePermissionDAO.getMaxSeq(dto.getViewId()));
            permissionDO.setModuleType(2);
            i = operatePermissionDAO.insert(permissionDO);
            //保存
        }else {
            i = operatePermissionDAO.updateByPrimaryKeySelective(permissionDO);
        }
        return i>0?ApiResult.success("操作成功"):ApiResult.failed("操作失败");
    }

    @Override
    public synchronized  String getMaxOperateId(String viewCode) {
        OperatePermissionDO max = operatePermissionDAO.getMaxPermission(viewCode);
        if(max==null){
            return viewCode+"01";
        }else {
            Integer maxId = new Integer(max.getId());
            return (maxId+1)+"";
        }
    }

    @Override
    public String getMaxViewCode(String pid) {
        OperatePermissionDO max = operatePermissionDAO.getMaxPermissionByPid(pid);
        if(StringUtils.isNullOrEmpty(pid)){
            if(max==null){
                return "1000";
            }else {
                return (new Integer(max.getCode())+1000)+"";
            }
        }else {
            if(max==null){
                OperatePermissionDO operatePermission = operatePermissionDAO.selectByPrimaryKey(pid);
                return operatePermission.getCode()+"01";
            }else {
                return (new Integer(max.getCode())+1)+"";
            }
        }
    }

    @Override
    public List<ViewOperatorDTO> getViewList() {
        return operatePermissionDAO.getViewList();
    }

    @Override
    public List<ViewOperatorDTO> getParentViewList() {
        List<ViewOperatorDTO> list = operatePermissionDAO.getParentView();
        return list;
    }

    @Override
    public ApiResult saveViewRole(SaveViewRoleDTO dto) throws Exception {
        //删除当前roleCode的所有对应的视图
        deleteViewByRoleCode(dto.getRoleCode());
        for(String viewId:dto.getViewIdList()){
            ViewRoleDO viewRole = new ViewRoleDO();
            viewRole.initEntity();
            viewRole.setViewId(viewId);
            viewRole.setRoleCode(dto.getRoleCode());
            // todo 重新设计，插入新的表中
           // viewRoleDAO.insert(viewRole);
        }
        return ApiResult.success("操作成功",null);
    }

    @Override
    public ApiResult savePermissionRoleBatch(SavePermissionRoleDTO dto) throws Exception {
        //删除当前roleCode的所有对应的视图
        deleteViewByRoleCode(dto.getRoleCode());
        for(String permissionId:dto.getPermissionIdList()){
            OperatePermissionDO permissionDO = operatePermissionDAO.selectByPrimaryKey(permissionId);
            RolePermissionDO rolePermission = new RolePermissionDO();
            rolePermission.initEntity();
            rolePermission.setPermissionCode(permissionDO.getCode());
            rolePermission.setRoleCode(dto.getRoleCode());
            rolePermissionDAO.insert(rolePermission);
        }
        return ApiResult.success("操作成功",null);
    }

    @Override
    public ApiResult savePermissionRole(SavePermissionRoleDTO dto) throws Exception {
        OperatePermissionDO permissionDO = operatePermissionDAO.selectByPrimaryKey(dto.getPermissionId());
        if(permissionDO==null){
            return ApiResult.failed("参数错误");
        }
        deleteRolePermission(dto.getRoleCode(),permissionDO.getCode());
        if(dto.getIsSelect()==1){
            RolePermissionDO rolePermission = new RolePermissionDO();
            rolePermission.initEntity();
            rolePermission.setPermissionCode(permissionDO.getCode());
            rolePermission.setRoleCode(dto.getRoleCode());
            rolePermissionDAO.insert(rolePermission);
        }
        return ApiResult.success("操作成功",null);
    }

    @Override
    public List<RoleDTO> getRoleForOrg() {
        return roleDAO.getRoleByType("0") ;
    }

    @Override
    public List<RoleDTO> getDefaultRole() {
        List<RoleDTO> list = roleDAO.getRoleByType("2") ;
        return list;
    }

    @Override
    public List<RoleDTO> getRoleForProject() {
        List<RoleDTO> list = roleDAO.getRoleByType("2") ;
        return list;
    }

    @Override
    public List<RoleDTO> getRoleForProject2(String ruleId) {
        return roleDAO.getRoleByType2("1",ruleId) ;
    }

    @Override
    public List<ViewOperatorDTO> getAllViewByRoleCode(String roleCode) {
        return operatePermissionDAO.getAllViewByRoleCode(roleCode);
    }

    @Override
    public RoleDTO getRoleByCode(String roleCode) {
        return roleDAO.getRoleByCode(roleCode);
    }

    private void deleteViewByRoleCode(String roleCode){
        Example example = new Example(ViewRoleDO.class);
        example.createCriteria()
                .andCondition("role_code = ", roleCode);
        //viewRoleDAO.deleteByExample(example);
    }


    private void deleteRolePermission(String roleCode,String permissionCode){
        Example example = new Example(RolePermissionDO.class);
        example.createCriteria()
                .andCondition("role_code = ", roleCode)
                .andCondition("permission_code = ", permissionCode);
        rolePermissionDAO.deleteByExample(example);
    }
}
