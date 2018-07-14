package com.maoding.mybatis.typeHandler;

import org.apache.ibatis.type.MappedTypes;

import java.sql.Connection;

/**
 * Map PostgreSQL array of doubles to java Double[] array.
 */
@MappedTypes(Double[].class)
public class ArrayDoubleTypeHandler extends ArrayTypeHandler<Double[]> {

    @Override
    protected String getDbTypeName(Connection connection) {
        // Now support only PostgreSQL types
        return "float8";
    }
}
