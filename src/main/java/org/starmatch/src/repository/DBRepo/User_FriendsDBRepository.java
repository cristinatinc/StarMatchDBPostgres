package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.model.User;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class User_FriendsDBRepository extends DBRepository<User> {

    public User_FriendsDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    /**
     * Adds a friend relationship between two users.
     *
     * @param userId the ID of the user
     * @param friendId the ID of the friend
     */
    public void addFriend(int userId, int friendId) {
        String sql = "INSERT INTO \"User_Friends\" (userId, friendId) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            statement.executeUpdate();

            // Ensure the reverse relationship is added (friend -> user)
            statement.setInt(1, friendId);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding friend", e);
        }
    }

    /**
     * Removes the friendship between two users.
     *
     * @param userId the ID of the user
     * @param friendId the ID of the friend
     */
    public void removeFriend(int userId, int friendId) {
        String sql = "DELETE FROM \"User_Friends\" WHERE (userId = ? AND friendId = ?) OR (userId = ? AND friendId = ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            statement.setInt(3, friendId);
            statement.setInt(4, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing friend", e);
        }
    }

    /**
     * Gets the list of all friends for a given user.
     *
     * @param userId the ID of the user
     * @return the list of friends
     */
    public List<User> getFriends(int userId) {
        String sql = "SELECT u.id, u.name, u.birthDate, u.birthTime, u.birthPlace, u.email, u.password " +
                "FROM \"User_Friends\" uf " +
                "JOIN \"User\" u ON u.id = uf.friendId " +
                "WHERE uf.userId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            List<User> friends = new ArrayList<>();
            while (resultSet.next()) {
                friends.add(extractFromResultSet(resultSet));
            }

            return friends;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching friends for user", e);
        }
    }

    /**
     * Checks if two users are friends.
     *
     * @param userId the ID of the first user
     * @param friendId the ID of the second user
     * @return true if they are friends, false otherwise
     */
    public boolean isFriend(int userId, int friendId) {
        String sql = "SELECT 1 FROM \"User_Friends\" WHERE (userId = ? AND friendId = ?) OR (userId = ? AND friendId = ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            statement.setInt(3, friendId);
            statement.setInt(4, userId);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking friendship status", e);
        }
    }

    @Override
    public void create(User obj) {
        // Creating a user is handled by UserDBRepository, not here.
    }

    @Override
    public User get(Integer id) {
        // Getting a user is handled by UserDBRepository, not here.
        return null;
    }

    @Override
    public void update(User obj) {
        // Updating a user is handled by UserDBRepository, not here.
    }

    @Override
    public void delete(Integer id) {
        // Deleting a user is handled by UserDBRepository, not here.
    }

    @Override
    public List<User> getAll() {
        // Getting all users is handled by UserDBRepository, not here.
        return null;
    }

    // Helper method to convert a result set to a User object
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
