package com.maoding.mybatis.typeHandler;

import org.apache.ibatis.type.MappedTypes;

import java.sql.Connection;

/**
 * Map PostgreSQL array of integers(int4) to java Integer[] array.
 */
@MappedTypes(Integer[].class)
public class ArrayIntegerTypeHandler extends ArrayTypeHandler<Integer[]> {

    @Override
    protected String getDbTypeName(Connection connection) {
        // Now support only PostgreSQL types
        return "int4";
    }

}
