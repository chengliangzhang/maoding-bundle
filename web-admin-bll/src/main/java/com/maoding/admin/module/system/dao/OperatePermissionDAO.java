package com.maoding.admin.module.system.dao;

import com.maoding.admin.module.system.dto.ViewOperatorDTO;
import com.maoding.admin.module.system.model.OperatePermissionDO;
import com.maoding.core.base.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OperatePermissionDAO extends BaseDao<OperatePermissionDO> {

    OperatePermissionDO getMaxPermission(@Param("viewCode") String viewCode);

    OperatePermissionDO getMaxPermissionByPid(@Param("pid") String pid);

    Integer getMaxSeq(@Param("pid") String pid);

    /**
     * 获取菜单及权限操作
     */
    List<ViewOperatorDTO> getViewList();

    /**
     * 获取所有以及菜单(pid is null)
     */
    List<ViewOperatorDTO> getParentView();

    /**
     * 获取所有菜单
     */
    List<ViewOperatorDTO> getAllViewByRoleCode(@Param("roleCode") String roleCode);
}
