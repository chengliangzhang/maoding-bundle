package com.maoding.mybatis.mapper;

import com.maoding.mybatis.provider.BatchProvider;
import org.apache.ibatis.annotations.InsertProvider;

import java.util.List;

/**
 * Created by Wuwq on 2017/2/15.
 */
public interface BatchMapper<T> {

    /** 批量插入 **/
    @InsertProvider(type = BatchProvider.class, method = "dynamicSQL")
    int BatchInsert(List<T> recordList);
}
