package org.starmatch.src.repository;
import org.starmatch.src.model.HasId;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import org.starmatch.src.model.*;

/**
 * The DBRepository class is a generic repository for performing CRUD operations on entities
 * that extend the HasID interface. It supports actions like creating, reading, updating, and deleting
 * records from a PostgreSQL database. It uses reflection to map object fields to database columns.
 *
 * @param <T> The type of entity this repository is working with, which extends HasID.
 */
public class DBRepository<T extends HasId> implements Repository<T> {
    private static final String URL = "jdbc:postgresql://localhost:5432/StarMatch";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    private static final String USER_TABLE = "User";
    private static final String ADMIN_TABLE = "Admin";
    private static final String COMPATIBILITY_TABLE = "Compatibility";
    private static final String QUOTE_TABLE = "Quote";
    private static final String STARSIGN_TABLE = "StarSign";
    private static final String TRAIT_TABLE = "Trait";
    private static final String STARSIGNTRAIT_TABLE = "StarSign_Trait";
    private static final String USERFRIENDS_TABLE = "User_Friends";
    private final Class<T> type;
    /**
     * Constructor for DBRepository.
     * Initializes the repository with a specific entity type.
     *
     * @param type The class type of the entity this repository will manage.
     */
    public DBRepository(Class<T> type) {
        this.type = type;
    }
    /**
     * Establishes and returns a connection to the PostgreSQL database.
     *
     * @return a Connection object used to interact with the database.
     * @throws RuntimeException if there is an error establishing the connection.
     */
    private Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Creates a new entity record in the database based on the provided object.
     *
     * @param obj The object representing the entity to be inserted into the database.
     */
    @Override
    public void create(T obj) {
        String createString = generateInsertSQL(obj);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(createString)){
            setInsertParameters(statement, obj);
            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error during create operation");
        }
    }
    /**
     * Deletes an entity record from the database based on its ID.
     *
     * @param id The ID of the entity to be deleted from the database.
     */
    @Override
    public void delete(Integer id) {
        String tableName = getTableName();
        String deleteString = "DELETE FROM " + tableName + " WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteString)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not find entity with ID: " + id);
        }
    }
    /**
     * Updates an existing entity record in the database based on the provided object.
     *
     * @param obj The object containing the updated values to be applied to the database record.
     */
    @Override
    public void update(T obj) {
        String updateString = generateUpdateSQL(obj);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateString)) {
            setUpdateParameters(statement, obj);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update entity");
        }
    }
    /**
     * Reads an entity from the database by its ID.
     *
     * @param id The ID of the entity to be fetched from the database.
     * @return The entity corresponding to the given ID, or null if not found.
     */
    @Override
    public T get(Integer id) {
        String tableName = getTableName();
        String readString = "SELECT * FROM " + tableName + " WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(readString)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToObject(resultSet);
            }
        } catch (SQLException | ReflectiveOperationException e) {
            throw new RuntimeException("Could not find entity with ID: " + id);
        }
        return null;
    }
    /**
     * Retrieves all entities from the database.
     *
     * @return A list of all entities of type T from the database.
     */
    @Override
    public List<T> getAll() {
        List<T> results = new ArrayList<>();
        String tableName = getTableName();
        String getAllString = "SELECT * FROM " + tableName;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getAllString)) {
            while (resultSet.next()) {
                results.add(mapResultSetToObject(resultSet));
            }
        } catch (SQLException | ReflectiveOperationException e) {
            throw new RuntimeException("Could not retrieve entities");
        }
        return results;
    }
    /**
     * Generates the SQL insert query string for inserting a new entity into the database.
     *
     * @param obj The entity object used to generate the SQL query.
     * @return A string representing the SQL insert query.
     */
    private String generateInsertSQL(T obj) {
        String tableName = getTableName();
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");

        List<String> columns = new ArrayList<>();
        List<String> placeholders = new ArrayList<>();

        Class<?> currentClass = obj.getClass();
        while (currentClass != null) {
            for (var field : currentClass.getDeclaredFields()) {
                if (shouldIncludeField(field)) {
                    field.setAccessible(true);
                    columns.add(field.getName());
                    placeholders.add("?");
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        if (columns.isEmpty()) {
            throw new RuntimeException("No fields available for insertion");
        }

        sql.append(String.join(", ", columns));
        sql.append(") VALUES (");
        sql.append(String.join(", ", placeholders));
        sql.append(")");
        //System.out.println("Generated SQL: " + sql);
        return sql.toString();
    }
    /**
     * Sets the parameters of a PreparedStatement for an insert operation.
     *
     * @param preparedStatement The PreparedStatement object to set parameters for.
     * @param entity The entity object containing values to be inserted.
     * @throws SQLException if there is an error setting parameters.
     */
    private void setInsertParameters(PreparedStatement preparedStatement, T entity) throws SQLException {
        int index = 1;
        Class<?> currentClass = entity.getClass();
        try {
            while (currentClass != null) {
                for (var field : currentClass.getDeclaredFields()) {
                    if (shouldIncludeField(field)) {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        if (value != null && field.getType().isEnum()) {
                            preparedStatement.setObject(index++, value.toString(), Types.VARCHAR);
                        } else {
                            preparedStatement.setObject(index++, value);
                        }
                    }
                }
                currentClass = currentClass.getSuperclass();
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing field values.");
        }
    }
    /**
     * Generates the SQL update query string for updating an existing entity in the database.
     *
     * @param obj The entity object used to generate the SQL update query.
     * @return A string representing the SQL update query.
     */
    private String generateUpdateSQL(T obj) {
        String tableName = getTableName();
        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        var fields = obj.getClass().getDeclaredFields();
        for (var field : fields) {
            if (shouldIncludeField(field) && !field.getName().equalsIgnoreCase("id")) {
                field.setAccessible(true);
                sql.append(field.getName()).append(" = ?, ");
            }
        }
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ?");
        return sql.toString();
    }
    /**
     * Sets the parameters of a PreparedStatement for an update operation.
     *
     * @param statement The PreparedStatement object to set parameters for.
     * @param obj The entity object containing updated values.
     * @throws SQLException if there is an error setting parameters.
     */
    private void setUpdateParameters(PreparedStatement statement, T obj) throws SQLException {
        int index = 1;
        var fields = obj.getClass().getDeclaredFields();
        try {
            for (var field : fields) {
                if (shouldIncludeField(field) && !field.getName().equalsIgnoreCase("id")) {
                    field.setAccessible(true);
                    Object value = field.get(obj);

                    if (value != null && value instanceof Enum) {
                        statement.setObject(index++, value.toString(), Types.VARCHAR);
                    } else if (value == null) {
                        statement.setNull(index++, Types.NULL);
                    } else {
                        statement.setObject(index++, value);
                    }
                }
            }
            statement.setInt(index, obj.getId());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing field values.");
        }
    }
    /**
     * Maps a ResultSet to an object of type T.
     *
     * @param resultSet The ResultSet containing the data to be mapped.
     * @return The entity object populated with data from the ResultSet.
     * @throws SQLException if there is an error processing the ResultSet.
     * @throws ReflectiveOperationException if there is an error reflecting on fields.
     */
    private T mapResultSetToObject(ResultSet resultSet) throws SQLException, ReflectiveOperationException {
        T obj = type.getDeclaredConstructor().newInstance();
        Class<?> currentClass = type;

        while (currentClass != null) {
            var fields = currentClass.getDeclaredFields();
            for (var field : fields) {
                if (shouldIncludeField(field)) {
                    field.setAccessible(true);
                    Object value = resultSet.getObject(field.getName());

                    if (value != null) {
                        if (field.getType().isEnum()) {
                            value = Enum.valueOf((Class<Enum>) field.getType(), value.toString());
                        } else if (field.getType() == java.util.Date.class) {
                            value = new java.util.Date(((Timestamp) value).getTime());
                        } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                            value = resultSet.getBoolean(field.getName());
                        }
                    }

                    field.set(obj, value);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return obj;
    }
    /**
     * Returns the table name corresponding to the entity type.
     *
     * @return The name of the table associated with the entity type.
     */
    private String getTableName() {
        if (type == User.class) {
            return USER_TABLE;
        } else if (type == Admin.class) {
            return ADMIN_TABLE;
        } else if (type == Trait.class) {
            return TRAIT_TABLE;
        } else if (type == Quote.class) {
            return QUOTE_TABLE;
        }else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
    /**
     * Determines if a field should be included in the SQL query.
     *
     * @param field The field to be checked.
     * @return true if the field should be included; false otherwise.
     */
    private boolean shouldIncludeField(Field field) {
        return !Collection.class.isAssignableFrom(field.getType());
    }

}