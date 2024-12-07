package org.starmatch.src.repository;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

import org.starmatch.src.model.*;

public class InFileRepository<T extends HasId> implements Repository<T> {
    private final String filePath;
    private final Class<T> entityClass;

    /**
     * Constructs a new FileRepository with the specified file path.
     *
     * @param filePath The path to the file where data will be stored.
     * @param entityClass The class type of T, used for reflection.
     */
    public InFileRepository(String filePath, Class<T> entityClass) {
        this.filePath = filePath;
        this.entityClass = entityClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(T obj) {
        doInFile(data -> data.putIfAbsent(obj.getId(), obj));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(Integer id) {
        return readDataFromFile().get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(T obj) {
        doInFile(data -> data.replace(obj.getId(), obj));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Integer id) {
        doInFile(data -> data.remove(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAll() {
        return new ArrayList<>(readDataFromFile().values());
    }

    /**
     * Performs an operation on the data stored in the file.
     *
     * @param function The function to apply to the data.
     */
    private void doInFile(Consumer<Map<Integer, T>> function) {
        Map<Integer, T> data = readDataFromFile();
        function.accept(data);
        writeDataToFile(data);
    }

    /**
     * Reads the data from the file.
     *
     * @return The data stored in the file, or an empty map if the file is empty or does not exist.
     */
    private Map<Integer, T> readDataFromFile() {
        Map<Integer, T> data = new HashMap<>();
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) return data;

        List<User> allUsers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 0 || fields[0].isEmpty()) {
                    continue;
                }

                try {
                    Integer id = Integer.parseInt(fields[0]);
                    T obj = createObjectFromFields(fields);
                    if (obj != null) {
                        data.put(id, obj);

                        if (obj instanceof User user) {
                            allUsers.add(user);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (User user : allUsers) {
                List<User> resolvedFriends = user.getRawFriendEmails().stream()
                        .map(email -> allUsers.stream()
                                .filter(u -> u.getEmail().equals(email))
                                .findFirst()
                                .orElse(null))
                        .filter(Objects::nonNull)
                        .toList();
                user.setFriends(resolvedFriends);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Writes the data to the file.
     *
     * @param data The data to write to the file.
     */
    private void writeDataToFile(Map<Integer, T> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (T obj : data.values()) {
                String line = convertObjectToLine(obj);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Converts an object to a CSV line using the object's own method.
     *
     * @param obj The object to convert.
     * @return The CSV line representation of the object.
     */
    private String convertObjectToLine(T obj) {
        return obj.convertObjectToLine();
    }

    /**
     * Creates an object from the CSV fields by invoking the `createObjectFromFields` method.
     * This assumes that the class T implements `HasId` and provides this static method.
     *
     * @param fields The fields from the CSV line.
     * @return A new instance of T created from the fields, or null if creation fails.
     */
    private T createObjectFromFields(String[] fields) {
        try {
            return switch (entityClass.getSimpleName()) {
                case "User" -> (T) User.createObjectFromFields(fields);
                case "Quote" -> (T) Quote.createObjectFromFields(fields);
                case "StarSign" -> (T) StarSign.createObjectFromFields(fields);
                case "Admin" -> (T) Admin.createObjectFromFields(fields);
                case "Trait" -> (T) Trait.createObjectFromFields(fields);
                default -> null;
            };
        } catch (Exception e) {
            System.err.println("Error invoking createObjectFromFields for class: " + entityClass.getName());
            e.printStackTrace();
            return null;
        }
    }
}
