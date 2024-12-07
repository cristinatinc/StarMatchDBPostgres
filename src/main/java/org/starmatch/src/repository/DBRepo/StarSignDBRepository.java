package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.model.Element;
import org.starmatch.src.model.StarSign;
import org.starmatch.src.model.Trait;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StarSignDBRepository extends DBRepository<StarSign> {

    private final StarSign_TraitDBRepository starSignTraitRepository;

    public StarSignDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
        this.starSignTraitRepository = new StarSign_TraitDBRepository(dbUrl, dbUser, dbPassword);
    }

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
            throw new RuntimeException("Error creating StarSign", e);
        }
    }

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
            throw new RuntimeException("Error reading StarSign", e);
        }
    }

    @Override
    public void update(StarSign obj) {
        String sql = "UPDATE \"StarSign\" SET starName = ?, element = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getStarName());
            statement.setString(2, obj.getElement().name());
            statement.setInt(3, obj.getId());

            statement.executeUpdate();

            // Remove old traits and add new ones
            starSignTraitRepository.removeTraitsFromStarSign(obj.getId());  // Remove previous traits
            for (Trait trait : obj.getTraits()) {
                starSignTraitRepository.addTraitToStarSign(obj.getId(), trait.getId());  // Add new traits
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating StarSign", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM \"StarSign\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting StarSign", e);
        }
    }

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
            throw new RuntimeException("Error fetching all StarSigns", e);
        }
    }

    private StarSign extractFromResultSet(ResultSet resultSet) throws SQLException {
        String starName = resultSet.getString("starName");
        Element element = Element.valueOf(resultSet.getString("element"));
        Integer id = resultSet.getInt("id");

        // Fetch the traits associated with the StarSign
        List<Trait> traits = starSignTraitRepository.getTraitsForStarSign(id);

        return new StarSign(starName, element, traits, id);
    }
}
