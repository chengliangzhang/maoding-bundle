package com.maoding.admin.module.system.dao;

import com.maoding.admin.module.system.dto.ViewOperatorDTO;
import com.maoding.admin.module.system.model.ViewDO;
import com.maoding.core.base.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ViewDAO extends BaseDao<ViewDO> {

    Integer getMaxSeq();

    String getMaxViewCode(@Param("pid") String pid);

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
