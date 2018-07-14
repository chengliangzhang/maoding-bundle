package com.maoding.mybatis.typeHandler;

import org.apache.ibatis.type.MappedTypes;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Map PostgreSQL array of boolean to java Boolean[] array.
 */
@MappedTypes(Boolean[].class)
public class ArrayBooleanTypeHandler extends ArrayTypeHandler<Boolean[]> {

    @Override
    protected String getDbTypeName(Connection connection) throws SQLException {
        // Now support only PostgreSQL types
        return "boolean";
    }

}
