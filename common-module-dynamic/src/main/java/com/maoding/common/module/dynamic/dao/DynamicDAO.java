package com.maoding.common.module.dynamic.dao;

import com.maoding.common.module.dynamic.model.DynamicDO;
import com.maoding.core.base.BaseDao;
import com.maoding.common.module.dynamic.dto.DynamicDTO;
import com.maoding.common.module.dynamic.dto.QueryDynamicDTO;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/6/5.
 */
public interface DynamicDAO extends BaseDao<DynamicDO> {
    /**
     * 查找动态信息
     */
    List<DynamicDTO> listDynamic(QueryDynamicDTO query);
    /**
     * 获取最后一次查询找到的记录数
     */
    Integer getLastQueryCount();
}
