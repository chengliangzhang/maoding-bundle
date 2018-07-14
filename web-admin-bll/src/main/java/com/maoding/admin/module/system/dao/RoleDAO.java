package com.maoding.admin.module.system.dao;

import com.maoding.admin.module.system.dto.RoleDTO;
import com.maoding.admin.module.system.model.RoleDO;
import com.maoding.core.base.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDAO extends BaseDao<RoleDO> {

    /**
     * 获取组织类型的角色
     */
    List<RoleDTO> getRoleByType(@Param("roleType") String roleType);

    RoleDTO getRoleByCode(@Param("roleCode")  String roleCode);

    List<RoleDTO> getRoleByType2(@Param("roleType") String roleType,@Param("ruleId") String ruleId);
}
