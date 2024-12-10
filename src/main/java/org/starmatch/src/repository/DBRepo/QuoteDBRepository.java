package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.exceptions.DatabaseException;
import org.starmatch.src.model.Quote;
import org.starmatch.src.model.Element;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for managing {@link Quote} entities in the database.
 * Extends {@link DBRepository} to provide CRUD operations for the "Quote" table.
 */
public class QuoteDBRepository extends DBRepository<Quote> {

    /**
     * Constructs a {@link QuoteDBRepository} with the specified database connection parameters.
     *
     * @param dbUrl      the URL of the PostgreSQL database.
     * @param dbUser     the username for database authentication.
     * @param dbPassword the password for database authentication.
     */
    public QuoteDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    /**
     * Inserts a new {@link Quote} entity into the database.
     *
     * @param obj the {@link Quote} entity to be inserted.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void create(Quote obj) {
        String sql = "INSERT INTO \"Quote\" (element, quoteText) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getElement().name());  // Storing enum name as a string
            statement.setString(2, obj.getQuoteText());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves a {@link Quote} entity from the database by its ID.
     *
     * @param id the ID of the {@link Quote} entity to retrieve.
     * @return the {@link Quote} entity, or {@code null} if not found.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public Quote get(Integer id) {
        String sql = "SELECT * FROM \"Quote\" WHERE id = ?";

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
     * Updates an existing {@link Quote} entity in the database.
     *
     * @param obj the {@link Quote} entity with updated values.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void update(Quote obj) {
        String sql = "UPDATE \"Quote\" SET element = ?, quoteText = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getElement().name());  // Store enum name as a string
            statement.setString(2, obj.getQuoteText());
            statement.setInt(3, obj.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Deletes a {@link Quote} entity from the database by its ID.
     *
     * @param id the ID of the {@link Quote} entity to delete.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM \"Quote\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves all {@link Quote} entities from the database.
     *
     * @return a list of all {@link Quote} entities.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public List<Quote> getAll() {
        String sql = "SELECT * FROM \"Quote\"";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            List<Quote> quotes = new ArrayList<>();
            while (resultSet.next()) {
                quotes.add(extractFromResultSet(resultSet));
            }

            return quotes;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Extracts a {@link Quote} entity from the current row of the given {@link ResultSet}.
     *
     * @param resultSet the {@link ResultSet} containing the database records.
     * @return a {@link Quote} entity with the data from the current row.
     * @throws SQLException if a SQL error occurs while accessing the result set.
     */
    public static Quote extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Quote(
                resultSet.getInt("id"),
                Element.valueOf(resultSet.getString("element")),  // Convert stored string back to enum
                resultSet.getString("quoteText")
        );
    }
}
