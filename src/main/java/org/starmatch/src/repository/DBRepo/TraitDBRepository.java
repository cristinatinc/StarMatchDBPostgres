package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.exceptions.DatabaseException;
import org.starmatch.src.model.Trait;
import org.starmatch.src.model.Element;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for managing Traits in the database.
 * Extends {@link DBRepository} to handle CRUD operations for {@link Trait}.
 */
public class TraitDBRepository extends DBRepository<Trait> {

    /**
     * Constructs a {@link TraitDBRepository} with the specified database connection parameters.
     *
     * @param dbUrl      the URL of the PostgreSQL database.
     * @param dbUser     the username for database authentication.
     * @param dbPassword the password for database authentication.
     */
    public TraitDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    /**
     * Creates a new {@link Trait} in the database.
     *
     * @param obj the {@link Trait} object to create.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void create(Trait obj) {
        String sql = "INSERT INTO \"Trait\" (element, traitName) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getElement().name());  // Storing the enum name as a string
            statement.setString(2, obj.getTraitName());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves a {@link Trait} by its ID.
     *
     * @param id the ID of the {@link Trait}.
     * @return the {@link Trait} object, or {@code null} if not found.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public Trait get(Integer id) {
        String sql = "SELECT * FROM \"Trait\" WHERE id = ?";

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
     * Updates an existing {@link Trait} in the database.
     *
     * @param obj the {@link Trait} object with updated values.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void update(Trait obj) {
        String sql = "UPDATE \"Trait\" SET element = ?, traitName = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getElement().name());  // Store the enum as a string
            statement.setString(2, obj.getTraitName());
            statement.setInt(3, obj.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not update", e);
        }
    }

    /**
     * Deletes a {@link Trait} from the database.
     *
     * @param id the ID of the {@link Trait} to delete.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM \"Trait\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves all {@link Trait} entries from the database.
     *
     * @return a list of {@link Trait} objects.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public List<Trait> getAll() {
        String sql = "SELECT * FROM \"Trait\"";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            List<Trait> traits = new ArrayList<>();
            while (resultSet.next()) {
                traits.add(extractFromResultSet(resultSet));
            }

            return traits;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Extracts a {@link Trait} object from the current row of the {@link ResultSet}.
     *
     * @param resultSet the {@link ResultSet} from which to extract data.
     * @return the {@link Trait} object.
     * @throws SQLException if a SQL error occurs.
     */
    public static Trait extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Trait(
                Element.valueOf(resultSet.getString("element")),  // Convert string back to enum
                resultSet.getString("traitName"),
                resultSet.getInt("id")
        );
    }
}
