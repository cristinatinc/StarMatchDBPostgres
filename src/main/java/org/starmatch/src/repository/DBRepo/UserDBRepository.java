package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.model.User;
import org.starmatch.src.repository.DBRepository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDBRepository extends DBRepository<User> {

    public UserDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(User obj) {
        String sql = "INSERT INTO \"User\" (name, birthDate, birthTime, birthPlace, email, password) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getName());
            statement.setDate(2, Date.valueOf(obj.getBirthDate())); // Converting LocalDate to SQL Date
            statement.setTime(3, Time.valueOf(obj.getBirthTime())); // Converting LocalTime to SQL Time
            statement.setString(4, obj.getBirthPlace());
            statement.setString(5, obj.getEmail());
            statement.setString(6, obj.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User get(Integer id) {
        String sql = "SELECT * FROM \"User\" WHERE id = ?";

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
    public void update(User obj) {
        String sql = "UPDATE \"User\" SET name = ?, birthDate = ?, birthTime = ?, birthPlace = ?, " +
                "email = ?, password = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getName());
            statement.setDate(2, Date.valueOf(obj.getBirthDate()));
            statement.setTime(3, Time.valueOf(obj.getBirthTime()));
            statement.setString(4, obj.getBirthPlace());
            statement.setString(5, obj.getEmail());
            statement.setString(6, obj.getPassword());
            statement.setInt(7, obj.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Optionally, handle the friends list update here if needed (requires an additional table for user relationships)
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM \"User\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Optionally, handle deleting the user's friendships from a `user_friends` table
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM \"User\"";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(extractFromResultSet(resultSet));
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDate("birthDate").toLocalDate(),
                resultSet.getTime("birthTime").toLocalTime(),
                resultSet.getString("birthPlace"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}

