package org.starmatch.src.repository.DBRepo;

import org.starmatch.src.exceptions.DatabaseException;
import org.starmatch.src.model.User;
import org.starmatch.src.repository.DBRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing User entities in the database.
 * Provides methods to perform CRUD operations, manage friendships, and retrieve user-related data.
 */
public class UserDBRepository extends DBRepository<User> {

    /**
     * Constructor for initializing the repository with database connection details.
     *
     * @param dbUrl      the URL of the database
     * @param dbUser     the database username
     * @param dbPassword the database password
     */
    public UserDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    /**
     * Creates a new User entity in the database.
     * Also saves the user's friendships, if any.
     *
     * @param obj the User object to be created
     */
    @Override
    public void create(User obj) {
        String sql = "INSERT INTO \"User\" (name, birthDate, birthTime, birthPlace, email, password) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, obj.getName());
            statement.setDate(2, Date.valueOf(obj.getBirthDate()));
            statement.setTime(3, Time.valueOf(obj.getBirthTime()));
            statement.setString(4, obj.getBirthPlace());
            statement.setString(5, obj.getEmail());
            statement.setString(6, obj.getPassword());

            statement.executeUpdate();

            // Retrieve generated user ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                obj.setId(generatedKeys.getInt(1));
            }

            // Optionally, handle the initial friendships if needed
            saveFriendships(obj);

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves a User entity from the database by its ID.
     * Includes the user's friendships.
     *
     * @param id the ID of the user to retrieve
     * @return the User object if found, null otherwise
     */
    @Override
    public User get(Integer id) {
        String sql = "SELECT * FROM \"User\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = extractFromResultSet(resultSet);

                // Fetch the user's friends from the user_friends table
                user.setFriends(getFriends(user.getId())); // Add friends to the user
                user.setRawFriendEmails(getRawFriendEmails(user.getId())); // Set raw friend emails
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Updates an existing User entity in the database.
     * Also updates the user's friendships.
     *
     * @param obj the User object with updated data
     */
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

            // Update the user's friendships
            updateFriendships(obj);

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Deletes a User entity from the database by its ID.
     * Also removes all friendships related to the user.
     *
     * @param id the ID of the user to delete
     */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM \"User\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();

            // Remove friendships related to the user
            deleteFriendships(id);

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves all User entities from the database.
     * Includes their respective friendships.
     *
     * @return a list of all User objects in the database
     */
    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM \"User\" ORDER BY birthdate";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = extractFromResultSet(resultSet);

                // Fetch the user's friends from the user_friends table
                user.setFriends(getFriends(user.getId())); // Add friends to the user
                user.setRawFriendEmails(getRawFriendEmails(user.getId())); // Set raw friend emails
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Saves the friendships of a user in the database.
     * Ensures bidirectional friendships are stored.
     *
     * @param user the User object whose friendships are to be saved
     */
    private void saveFriendships(User user) {
        String sql = "INSERT INTO \"User_Friends\" (userId, friendId) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (User friend : user.getFriends()) {
                // Insert the friendship from user to friend
                statement.setInt(1, user.getId());
                statement.setInt(2, friend.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }
    /**
     * Updates the friendships of a user in the database.
     * Removes old friendships and saves the new ones.
     *
     * @param user the User object whose friendships are to be updated
     */
    private void updateFriendships(User user) {
        // Delete old friendships first
        deleteFriendships(user.getId());

        // Save the new friendships
        saveFriendships(user);
    }

    /**
     * Deletes all friendships related to a user from the database.
     *
     * @param userId the ID of the user whose friendships are to be deleted
     */
    private void deleteFriendships(Integer userId) {
        String sql = "DELETE FROM \"User_Friends\" WHERE userId = ? OR friendId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves the friends of a user from the database.
     *
     * @param userId the ID of the user whose friends are to be retrieved
     * @return a list of User objects representing the user's friends
     */
    private List<User> getFriends(Integer userId) {
        String sql = "SELECT * FROM \"User\" u JOIN \"User_Friends\" uf ON u.id = uf.friendId WHERE uf.userId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            List<User> friends = new ArrayList<>();
            while (resultSet.next()) {
                friends.add(extractFromResultSet(resultSet));
            }

            return friends;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves the email addresses of a user's friends from the database.
     *
     * @param userId the ID of the user whose friends' emails are to be retrieved
     * @return a list of email addresses of the user's friends
     */
    private List<String> getRawFriendEmails(Integer userId) {
        String sql = "SELECT u.email FROM \"User_Friends\" uf JOIN \"User\" u ON u.id = uf.friendId WHERE uf.userId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            List<String> emails = new ArrayList<>();
            while (resultSet.next()) {
                emails.add(resultSet.getString("email"));
            }

            return emails;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Extracts a User object from a ResultSet.
     *
     * @param resultSet the ResultSet containing user data
     * @return the User object created from the ResultSet
     * @throws SQLException if a database access error occurs
     */
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
