package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.model.Element;
import org.starmatch.src.model.Trait;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StarSign_TraitDBRepository extends DBRepository<Trait> {

    public StarSign_TraitDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    // Add a trait to a StarSign
    public void addTraitToStarSign(Integer starSignId, Integer traitId) {
        String sql = "INSERT INTO \"StarSign_Trait\" (starSignId, traitId) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, starSignId);
            statement.setInt(2, traitId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding trait to StarSign", e);
        }
    }

    // Remove all traits from a StarSign
    public void removeTraitsFromStarSign(Integer starSignId) {
        String sql = "DELETE FROM \"StarSign_Trait\" WHERE starSignId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, starSignId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing traits from StarSign", e);
        }
    }

    // Get all traits for a StarSign
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
            throw new RuntimeException("Error fetching traits for StarSign", e);
        }
    }

    @Override
    public void create(Trait obj) {
        // Implementation for creating a Trait, if needed
    }

    @Override
    public Trait get(Integer id) {
        // Implementation for getting a specific Trait, if needed
        return null;
    }

    @Override
    public void update(Trait obj) {
        // Implementation for updating a Trait, if needed
    }

    @Override
    public void delete(Integer id) {
        // Implementation for deleting a Trait, if needed
    }

    @Override
    public List<Trait> getAll() {
        // Implementation for getting all Traits, if needed
        return null;
    }
}
