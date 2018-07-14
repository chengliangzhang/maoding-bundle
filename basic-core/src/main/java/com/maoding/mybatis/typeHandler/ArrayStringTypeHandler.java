package com.maoding.mybatis.typeHandler;

import org.apache.ibatis.type.MappedTypes;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Map PostgreSQL array of strings to java String[] array.
 */
@MappedTypes(String[].class)
public class ArrayStringTypeHandler extends ArrayTypeHandler<String[]> {

    @Override
    protected String getDbTypeName(Connection connection) throws SQLException {
        String db = connection.getMetaData().getDatabaseProductName();
        return getTypeForDb(db);
    }

    /**
     * Return array element type for database product name.
     * Now support only PostgreSQL types.
     */
    public static String getTypeForDb(String dbname) {
        switch (dbname) {
            case "PostgreSQL":
                return "text";
            default:
                throw new UnsupportedOperationException("Unsupported DB vendor: " + dbname);
        }
    }
}
