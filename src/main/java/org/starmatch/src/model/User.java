package org.starmatch.src.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a user with personal details, login information, and a list of friends.
 * Extends the {@link Person} class to include attributes for email and password.
 */
public class User extends Person {

    private String email;
    private String password;
    private List<User> friends;
    private List<String> rawFriendEmails;

    /**
     * Constructs a User with the specified details and initializes an empty friends list.
     *
     * @param id         the unique identifier of the user
     * @param name       the name of the user
     * @param birthDate  the birth date of the user
     * @param birthTime  the birth time of the user
     * @param birthPlace the birth place of the user
     * @param email      the email address of the user
     * @param password   the password of the user
     */
    public User(int id, String name, LocalDate birthDate, LocalTime birthTime, String birthPlace, String email, String password) {
        super(id, name, birthDate, birthTime, birthPlace);
        this.email = email;
        this.password = password;
        this.friends = new ArrayList<>();
        this.rawFriendEmails = new ArrayList<>();
    }

    /**
     * Gets the email of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the new email address for the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the new password for the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the list of friends for this user.
     *
     * @return a list of friends
     */
    public List<User> getFriends() {
        return friends;
    }

    /**
     * Sets the list of friends for this user.
     *
     * @param friends the new list of friends for the user
     */
    public void setFriends(List<User> friends) {
        this.friends = new ArrayList<>(friends);
    }

    /**
     * Returns a string representation of the user, including their personal details and friends.
     *
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthDate=" + birthDate +
                ", birthTime=" + birthTime +
                ", birthPlace='" + birthPlace + '\'' +
                ", friends=" + friends +
                '}';
    }


    /**
     * Converts this User object to a line for the text file.
     *
     * @return The CSV representation of the User.
     */
    @Override
    public String convertObjectToLine() {
        String rawFriendEmailsStr = String.join(",", rawFriendEmails); // Serialize raw friend emails
        return id + "," + name + "," + email + "," + password + "," + birthDate + "," +
                birthTime + "," + birthPlace + "," + rawFriendEmailsStr;
    }



    /**
     * Creates a User object from the given fields in a text file line.
     *
     * @param fields The fields from the CSV line.
     * @return A new User object created from the fields.
     */

    public static User createObjectFromFields(String[] fields) {
        Integer id = Integer.parseInt(fields[0]);
        String name = fields[1];
        String email = fields[2];
        String password = fields[3];
        LocalDate birthDate = LocalDate.parse(fields[4]);
        LocalTime birthTime = LocalTime.parse(fields[5]);
        String birthPlace = fields[6];

        User user = new User(id, name, birthDate, birthTime, birthPlace, email, password);

        if (fields.length > 7 && !fields[7].isEmpty()) {
            List<String> fe = Arrays.stream(fields, 7, fields.length)
                    .map(String::trim)
                    .toList();

            user.setRawFriendEmails(fe);
        }

        return user;
    }

    /**
     * Gets the raw list of friend emails.
     * This list represents the emails of the user's friends, stored as raw data.
     *
     * @return the raw list of friend emails
     */
    public List<String> getRawFriendEmails() {
        return rawFriendEmails;
    }

    /**
     * Sets the raw list of friend emails.
     * This method sets the list of emails for the user's friends, which are stored as raw data.
     *
     * @param rawFriendEmails the new list of raw friend emails
     */
    public void setRawFriendEmails(List<String> rawFriendEmails) {
        this.rawFriendEmails = new ArrayList<>(rawFriendEmails);
    }
}
