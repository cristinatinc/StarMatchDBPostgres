package org.starmatch.src.model;

/**
 * Represents an Admin with a unique ID, name, email, and password.
 * Implements the {@link HasId} interface to provide an ID getter.
 */
public class Admin extends Person {
    private int id;
    private String name;
    private String password;
    private String email;

    /**
     * Constructs an Admin instance with the specified ID, name, email, and password.
     *
     * @param id       the unique identifier for the admin
     * @param name     the name of the admin
     * @param email    the email address of the admin
     * @param password the password for the admin's account
     */
    public Admin(int id, String name, String email, String password) {
        super(id, name);
        this.email = email;
        this.password = password;
    }

    /**
     * Sets the ID of this admin.
     *
     * @param id the new ID for this admin
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of this admin.
     *
     * @return the name of the admin
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this admin.
     *
     * @param name the new name for this admin
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the password of this admin.
     *
     * @return the password of the admin
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of this admin.
     *
     * @param password the new password for this admin
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email address of this admin.
     *
     * @return the email address of the admin
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of this admin.
     *
     * @param email the new email address for this admin
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the unique ID of this admin.
     *
     * @return the ID of the admin
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Returns a string representation of this admin.
     *
     * @return a string representation of the admin object
     */
    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    /**
     * Converts the Admin object to a CSV line.
     *
     * @return the CSV representation of this Admin object.
     */
    @Override
    public String convertObjectToLine() {
        return id + "," + name + "," + email + "," + password;
    }

    /**
     * Creates an Admin object from a CSV line.
     *
     * @param fields the CSV fields to create the object
     * @return the Admin object created from the fields
     */
    public static HasId createObjectFromFields(String[] fields) {
        int id = Integer.parseInt(fields[0]);
        String name = fields[1];
        String email = fields[2];
        String password = fields[3];
        return new Admin(id, name, email, password);
    }
}
