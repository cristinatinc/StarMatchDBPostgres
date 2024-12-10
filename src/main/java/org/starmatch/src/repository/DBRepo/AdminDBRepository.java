package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.exceptions.DatabaseException;
import org.starmatch.src.model.Admin;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for managing {@link Admin} entities in the database.
 * Extends {@link DBRepository} to provide CRUD operations for the "Admin" table.
 */
public class AdminDBRepository extends DBRepository<Admin> {

    /**
     * Constructs an {@link AdminDBRepository} with the specified database connection parameters.
     *
     * @param dbUrl      the URL of the PostgreSQL database.
     * @param dbUser     the username for database authentication.
     * @param dbPassword the password for database authentication.
     */
    public AdminDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    /**
     * Inserts a new {@link Admin} entity into the database.
     *
     * @param obj the {@link Admin} entity to be inserted.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void create(Admin obj) {
        String sql = "INSERT INTO \"Admin\" (name, email, password) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getEmail());
            statement.setString(3, obj.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves an {@link Admin} entity from the database by its ID.
     *
     * @param id the ID of the {@link Admin} entity to retrieve.
     * @return the {@link Admin} entity, or {@code null} if not found.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public Admin get(Integer id) {
        String sql = "SELECT * FROM \"Admin\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Updates an existing {@link Admin} entity in the database.
     *
     * @param obj the {@link Admin} entity with updated values.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void update(Admin obj) {
        String sql = "UPDATE \"Admin\" SET name = ?, email = ?, password = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getEmail());
            statement.setString(3, obj.getPassword());
            statement.setInt(4, obj.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Deletes an {@link Admin} entity from the database by its ID.
     *
     * @param id the ID of the {@link Admin} entity to delete.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM \"Admin\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves all {@link Admin} entities from the database.
     *
     * @return a list of all {@link Admin} entities.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public List<Admin> getAll() {
        String sql = "SELECT * FROM \"Admin\"";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            List<Admin> admins = new ArrayList<>();
            while (resultSet.next()) {
                admins.add(extractFromResultSet(resultSet));
            }

            return admins;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Extracts an {@link Admin} entity from the current row of the given {@link ResultSet}.
     *
     * @param resultSet the {@link ResultSet} containing the database records.
     * @return an {@link Admin} entity with the data from the current row.
     * @throws SQLException if a SQL error occurs while accessing the result set.
     */
    public static Admin extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Admin(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
