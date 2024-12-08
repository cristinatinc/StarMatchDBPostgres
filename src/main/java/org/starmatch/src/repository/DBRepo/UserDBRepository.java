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
                User user = extractFromResultSet(resultSet);

                // Fetch the user's friends from the user_friends table
                user.setFriends(getFriends(user.getId())); // Add friends to the user
                user.setRawFriendEmails(getRawFriendEmails(user.getId())); // Set raw friend emails
                return user;
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

            // Update the user's friendships
            updateFriendships(obj);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM \"User\" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();

            // Remove friendships related to the user
            deleteFriendships(id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM \"User\"";

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
            throw new RuntimeException(e);
        }
    }

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

            // Now check for any unidirectional friendships and insert the reverse direction if needed
            String reverseSql = "SELECT 1 FROM \"User_Friends\" WHERE userId = ? AND friendId = ?";
            try (PreparedStatement reverseStatement = connection.prepareStatement(reverseSql)) {
                for (User friend : user.getFriends()) {
                    // Check if the reverse friendship already exists
                    reverseStatement.setInt(1, friend.getId());
                    reverseStatement.setInt(2, user.getId());

                    ResultSet resultSet = reverseStatement.executeQuery();
                    if (!resultSet.next()) {
                        // If the reverse friendship does not exist, insert it
                        try (PreparedStatement insertReverse = connection.prepareStatement(sql)) {
                            insertReverse.setInt(1, friend.getId());
                            insertReverse.setInt(2, user.getId());
                            insertReverse.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void updateFriendships(User user) {
        // Delete old friendships first
        deleteFriendships(user.getId());

        // Save the new friendships
        saveFriendships(user);
    }

    private void deleteFriendships(Integer userId) {
        String sql = "DELETE FROM \"User_Friends\" WHERE userId = ? OR friendId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
            throw new RuntimeException(e);
        }
    }

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
