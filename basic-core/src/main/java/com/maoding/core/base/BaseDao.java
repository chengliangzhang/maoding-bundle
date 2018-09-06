package com.maoding.core.base;

import com.maoding.mybatis.mapper.BatchMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by Wuwq on 2016/12/14.
 * 数据库访问层接口（自带通用方法）
 */
public interface BaseDao<T> extends Mapper<T>, MySqlMapper<T>, BatchMapper<T> {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
}
