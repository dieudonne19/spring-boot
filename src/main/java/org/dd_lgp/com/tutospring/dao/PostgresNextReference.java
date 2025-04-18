package org.dd_lgp.com.tutospring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresNextReference {
    public long nextID(String tableName, Connection connection) throws SQLException {
        final String columnName = "id";

        String sequenceQuery = "SELECT pg_get_serial_sequence('" + tableName + "', '" + columnName + "')";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sequenceQuery);
             ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                String sequenceName = rs.getString(1);
                String nextValQuery = "SELECT nextval('" + sequenceName + "')";
                try (PreparedStatement nextValStm = connection.prepareStatement(nextValQuery);
                     ResultSet nextValRs = nextValStm.executeQuery()) {
                    if (nextValRs.next()) {
                        return nextValRs.getLong(1);
                    }
                }
            }
        }
        throw new SQLException("Unable to find sequence for table ");
    }
}
