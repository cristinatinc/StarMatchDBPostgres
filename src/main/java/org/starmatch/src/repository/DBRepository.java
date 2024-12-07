package org.starmatch.src.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.starmatch.src.model.HasId;
import org.starmatch.src.repository.Repository;

public abstract class DBRepository<T extends HasId> implements Repository<T>, AutoCloseable {

    protected final Connection connection;

    public DBRepository(String dbUrl, String dbUser, String dbPassword) {
        try {
            // Register PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to connect to PostgreSQL database", e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
