package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.exceptions.DatabaseException;
import org.starmatch.src.model.Element;
import org.starmatch.src.model.StarSign;
import org.starmatch.src.model.Trait;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for managing Star Signs in the database.
 * Extends {@link DBRepository} to handle CRUD operations for {@link StarSign}.
 */
public class StarSignDBRepository extends DBRepository<StarSign> {

    private final StarSign_TraitDBRepository starSignTraitRepository;

    /**
     * Constructs a {@link StarSignDBRepository} with the specified database connection parameters.
     *
     * @param dbUrl      the URL of the PostgreSQL database.
     * @param dbUser     the username for database authentication.
     * @param dbPassword the password for database authentication.
     */
    public StarSignDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
        this.starSignTraitRepository = new StarSign_TraitDBRepository(dbUrl, dbUser, dbPassword);
    }

    /**
     * Creates a new {@link StarSign} in the database.
     * Automatically associates the traits provided with the star sign in the "StarSign_Trait" table.
     *
     * @param obj the {@link StarSign} object to create.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void create(StarSign obj) {
        String sql = "INSERT INTO \"StarSign\" (starName, element) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, obj.getStarName());
            statement.setString(2, obj.getElement().name());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));  // Set generated ID
                }

                // Add traits to the StarSign_Trait table
                for (Trait trait : obj.getTraits()) {
                    starSignTraitRepository.addTraitToStarSign(obj.getId(), trait.getId());
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves a {@link StarSign} by its ID.
     *
     * @param id the ID of the {@link StarSign}.
     * @return the {@link StarSign} object, or {@code null} if not found.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public StarSign get(Integer id) {
        String sql = "SELECT * FROM \"StarSign\" WHERE id = ?";

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
     * Updates an existing {@link StarSign} in the database.
     * Clears and re-adds the associated traits for the star sign.
     *
     * @param obj the {@link StarSign} object with updated values.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void update(StarSign obj) {
        String sql = "UPDATE \"StarSign\" SET starName = ?, element = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getStarName());
            statement.setString(2, obj.getElement().name());
            statement.setInt(3, obj.getId());

            statement.executeUpdate();

            starSignTraitRepository.removeTraitsFromStarSign(obj.getId());  // Remove previous traits
            for (Trait trait : obj.getTraits()) {
                starSignTraitRepository.addTraitToStarSign(obj.getId(), trait.getId());  // Add new traits
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Deletes a {@link StarSign} from the database.
     *
     * @param id the ID of the {@link StarSign} to delete.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM \"StarSign\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves all {@link StarSign} entries from the database.
     * Includes associated traits for each star sign.
     *
     * @return a list of {@link StarSign} objects.
     * @throws DatabaseException if a SQL error occurs.
     */
    @Override
    public List<StarSign> getAll() {
        String sql = "SELECT * FROM \"StarSign\"";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            List<StarSign> starSigns = new ArrayList<>();
            while (resultSet.next()) {
                starSigns.add(extractFromResultSet(resultSet));
            }

            return starSigns;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Extracts a {@link StarSign} object from the current row of the {@link ResultSet}.
     * Fetches associated traits for the star sign.
     *
     * @param resultSet the {@link ResultSet} from which to extract data.
     * @return the {@link StarSign} object.
     * @throws SQLException if a SQL error occurs.
     */
    private StarSign extractFromResultSet(ResultSet resultSet) throws SQLException {
        String starName = resultSet.getString("starName");
        Element element = Element.valueOf(resultSet.getString("element"));
        Integer id = resultSet.getInt("id");

        List<Trait> traits = starSignTraitRepository.getTraitsForStarSign(id);

        return new StarSign(starName, element, traits, id);
    }
}
