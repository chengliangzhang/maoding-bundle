package com.maoding.mybatis.typeHandler;

import org.apache.ibatis.type.MappedTypes;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Multi dimensional array handler.
 * <p>
 * PostgreSQL require that such arrays must be as matrix - all rows must have same amount of elements.
 */
@MappedTypes(String[][].class)
public class ArrayString2dTypeHandler extends ArrayTypeHandler<String[][]> {

    @Override
    protected String getDbTypeName(Connection connection) throws SQLException {
        String db = connection.getMetaData().getDatabaseProductName();
        return ArrayStringTypeHandler.getTypeForDb(db);
    }

    @Override
    protected String[][] toEmptyValue(Object[] value) {
        return new String[0][0];
    }
}
