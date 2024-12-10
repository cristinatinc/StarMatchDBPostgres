package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.exceptions.DatabaseException;
import org.starmatch.src.model.Element;
import org.starmatch.src.model.Trait;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for managing relationships between Star Signs and Traits in the database.
 * Extends {@link DBRepository} to provide additional functionality for the "StarSign_Trait" table.
 */
public class StarSign_TraitDBRepository extends DBRepository<Trait> {

    /**
     * Constructs a {@link StarSign_TraitDBRepository} with the specified database connection parameters.
     *
     * @param dbUrl      the URL of the PostgreSQL database.
     * @param dbUser     the username for database authentication.
     * @param dbPassword the password for database authentication.
     */
    public StarSign_TraitDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    /**
     * Adds a trait to a star sign by inserting into the "StarSign_Trait" table.
     *
     * @param starSignId the ID of the star sign.
     * @param traitId    the ID of the trait.
     * @throws DatabaseException if a SQL error occurs.
     */
    public void addTraitToStarSign(Integer starSignId, Integer traitId) {
        String sql = "INSERT INTO \"StarSign_Trait\" (starSignId, traitId) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, starSignId);
            statement.setInt(2, traitId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Removes all traits associated with a star sign by deleting from the "StarSign_Trait" table.
     *
     * @param starSignId the ID of the star sign.
     * @throws DatabaseException if a SQL error occurs.
     */
    public void removeTraitsFromStarSign(Integer starSignId) {
        String sql = "DELETE FROM \"StarSign_Trait\" WHERE starSignId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, starSignId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves all traits associated with a specific star sign by joining the "Trait" and "StarSign_Trait" tables.
     *
     * @param starSignId the ID of the star sign.
     * @return a list of {@link Trait} objects associated with the star sign.
     * @throws DatabaseException if a SQL error occurs.
     */
    public List<Trait> getTraitsForStarSign(Integer starSignId) {
        String sql = "SELECT t.* FROM \"Trait\" t " +
                "JOIN \"StarSign_Trait\" st ON t.id = st.traitId " +
                "WHERE st.starSignId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, starSignId);

            ResultSet resultSet = statement.executeQuery();
            List<Trait> traits = new ArrayList<>();
            while (resultSet.next()) {
                Integer traitId = resultSet.getInt("id");
                String traitName = resultSet.getString("traitName");
                Element element= Element.valueOf(resultSet.getString("element"));
                Trait trait = new Trait(element,traitName, traitId);
                traits.add(trait);
            }

            return traits;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Not implemented, not neeeded.
     * @param obj The object to create.
     */
    @Override
    public void create(Trait obj) {
    }

    /**
     * Not implemented, not needed.
     * @param id The unique identifier of the object to retrieve.
     * @return
     */
    @Override
    public Trait get(Integer id) {
        return null;
    }

    /**
     * Not implemneted, not needed.
     * @param obj The object to update.
     */
    @Override
    public void update(Trait obj) {
    }

    /**
     * Not implemented, not needed.
     * @param id The unique identifier of the object to delete.
     */
    @Override
    public void delete(Integer id) {
    }

    /**
     * Not implemented, not needed.
     * @return
     */
    @Override
    public List<Trait> getAll() {
        return null;
    }
}
