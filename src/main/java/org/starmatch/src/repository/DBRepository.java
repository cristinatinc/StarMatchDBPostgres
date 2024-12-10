package org.starmatch.src.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.starmatch.src.exceptions.DatabaseException;
import org.starmatch.src.model.HasId;
import org.starmatch.src.repository.Repository;

/**
 * An abstract class for a database repository, managing the connection to a PostgreSQL database.
 * Provides a base for implementing repositories for different entity types.
 *
 * @param <T> the type of entities handled by the repository, must extend {@link HasId}.
 */
public abstract class DBRepository<T extends HasId> implements Repository<T>, AutoCloseable {

    /**
     * The connection to the PostgreSQL database.
     */
    protected final Connection connection;

    /**
     * Creates a new database repository and establishes a connection to the database.
     *
     * @param dbUrl      the URL of the PostgreSQL database.
     * @param dbUser     the username for database authentication.
     * @param dbPassword the password for database authentication.
     * @throws DatabaseException if the database connection fails or the JDBC driver is not found.
     */
    public DBRepository(String dbUrl, String dbUser, String dbPassword) {
        try {
            // Register PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException | ClassNotFoundException e) {
            throw new DatabaseException("Failed to connect to PostgreSQL database", e);
        }
    }

    /**
     * Closes the database connection.
     *
     * @throws Exception if an error occurs while closing the connection.
     */
    @Override
    public void close() throws Exception {
        connection.close();
    }
}
