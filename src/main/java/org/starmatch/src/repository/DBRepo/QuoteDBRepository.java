package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.exceptions.DatabaseException;
import org.starmatch.src.model.Quote;
import org.starmatch.src.model.Element;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuoteDBRepository extends DBRepository<Quote> {

    public QuoteDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

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

    public static Quote extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Quote(
                resultSet.getInt("id"),
                Element.valueOf(resultSet.getString("element")),  // Convert stored string back to enum
                resultSet.getString("quoteText")
        );
    }
}
