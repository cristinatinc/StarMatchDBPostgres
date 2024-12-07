package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.model.Trait;
import org.starmatch.src.model.Element;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TraitDBRepository extends DBRepository<Trait> {

    public TraitDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(Trait obj) {
        String sql = "INSERT INTO \"Trait\" (element, traitName) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getElement().name());  // Storing the enum name as a string
            statement.setString(2, obj.getTraitName());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Trait obj) {
        String sql = "UPDATE \"Trait\" SET element = ?, traitName = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getElement().name());  // Store the enum as a string
            statement.setString(2, obj.getTraitName());
            statement.setInt(3, obj.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM \"Trait\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
            throw new RuntimeException(e);
        }
    }

    public static Trait extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Trait(
                Element.valueOf(resultSet.getString("element")),  // Convert string back to enum
                resultSet.getString("traitName"),
                resultSet.getInt("id")
        );
    }
}
