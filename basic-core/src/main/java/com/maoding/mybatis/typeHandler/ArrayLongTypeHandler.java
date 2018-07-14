package com.maoding.mybatis.typeHandler;

import org.apache.ibatis.type.MappedTypes;

import java.sql.Connection;

/**
 * Map PostgreSQL array of longs(int8) to java Long[] array.
 */
@MappedTypes(Long[].class)
public class ArrayLongTypeHandler extends ArrayTypeHandler<Long[]> {

    @Override
    protected String getDbTypeName(Connection connection) {
        // Now support only PostgreSQL types
        return "bigint";
    }
}
