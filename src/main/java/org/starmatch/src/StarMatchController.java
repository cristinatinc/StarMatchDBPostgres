package org.starmatch.src;

import org.starmatch.src.StarMatchService;
import org.starmatch.src.model.*;
import org.starmatch.src.exceptions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * StarMatchController provides methods for interacting with the StarMatchService,
 * handling user login, account management, and various astrological features.
 */
public class StarMatchController {

    private final StarMatchService starMatchService;

    /**
     * Constructor for the StarMatchController with the StarMatchService.
     */
    public StarMatchController(StarMatchService starMatchService) {this.starMatchService=starMatchService;}

    /**
     * Validates user Login using function in service.
     */
    public boolean handleUserLogin(String email, String password) {
        return starMatchService.validateUserLogin(email, password);
    }

    /**
     * Validates admin Login using function in service.
     */
    public boolean handleAdminLogin(String email, String password) {
        return starMatchService.validateAdminLogin(email, password);
    }

    /**
     * Registers a new user if the email is valid
     */
    public void signUpNewUser(String name, LocalDate birthDate, LocalTime birthTime, String birthPlace, String email, String password) {
        if(starMatchService.validateEmail(email))
            starMatchService.createUser(name, birthDate, birthTime, birthPlace, email, password);
        else
            throw new EntityNotFoundException("Invalid email");
    }

    /**
     * Adds a new admin if the email is valid
     */
    public void addNewAdmin(String name, String email, String password){
        if(starMatchService.validateEmail(email)){
            starMatchService.createAdmin(name, email, password);
            System.out.println("Admin added successfully!");}
        else
            throw new ValidationException("Invalid email");
    }

    /**
     * Removes an admin by ID.
     */
    public void removeAdmin(Integer adminID){
        starMatchService.removeAdmin(adminID);
        System.out.println("Removed admin with ID " + adminID);
    }

    /**
     * Updates an admin's information if the email is valid.
     */
    public void updateAdmin(Integer adminID, String name, String email, String password){
        if(starMatchService.validateEmail(email)){
            starMatchService.updateAdmin(adminID, name, email, password);
            System.out.println("Admin updated successfully!");}
        else
            throw new ValidationException("Invalid email");
    }

    /**
     * Displays all admins.
     */
    public void viewAdmins(){
        StringBuilder output = new StringBuilder("Admins of the app:\n");
        starMatchService.getAdmins().forEach(admin -> output.append(admin.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Displays all quotes.
     */
    public void viewQuotes(){
        StringBuilder output = new StringBuilder("Quotes:\n");
        starMatchService.getQuotes().forEach(quote -> output.append(quote.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Adds a new quote associated with a specified element.
     */
    public void addNewQuote(String newQuote, String element){
        try{
            starMatchService.createQuote(newQuote, element);
            System.out.println("Quote added successfully!");}
        catch(EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a quote by its ID.
     */
    public void removeQuote(Integer quoteID){
        try{
            starMatchService.removeQuote(quoteID);
            System.out.println("Quote removed successfully!");}
        catch(EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the text of a quote by ID.
     */
    public void updateQuote(Integer quoteID, String quoteText){
        starMatchService.updateQuote(quoteID, quoteText);
        System.out.println("Quote updated successfully!");
    }

    /**
     * Displays all traits.
     */
    public void viewTraits(){
        StringBuilder output = new StringBuilder("Traits:\n");
        starMatchService.getTraits().forEach(trait -> output.append(trait.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Adds a new traits.
     */
    public void addTrait(String traitName, Element element){
        try{
            starMatchService.createTrait(traitName,element);}
        catch(ValidationException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a trait by its ID.
     */
    public void removeTrait(Integer traitID){
        try{
            starMatchService.removeTrait(traitID);}
        catch(EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates a trait by its ID.
     */
    public void updateTrait(Integer traitID, String traitName, Element element){
        starMatchService.updateTrait(traitID,traitName,element);
    }

    /**
     * Displays all Users.
     */
    public void viewUsers(){
        StringBuilder output = new StringBuilder("User profile:\n");
        starMatchService.getUsers().forEach(user -> output.append(user.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Removes a user by its ID.
     */
    public void removeUser(Integer userID){
        try{
            starMatchService.removeUser(userID);
            System.out.println("Removed user with ID " + userID);}
        catch(EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves and returns a user's natal chart.
     */
    public NatalChart viewNatalChart(String userEmail){
        User user = starMatchService.getUserByEmail(userEmail);
        if (user != null) {
            return starMatchService.getNatalChart(user);
        } else {
            throw new EntityNotFoundException("User not found.");
        }
    }

    /**
     * Retrieves and returns a user's profile by email.
     */
    public User viewUserProfile(String userEmail){
        return starMatchService.getUserByEmail(userEmail);
    }

    /**
     * Retrieves and returns a user's personality traits.
     */
    public List<String> viewPersonalityTraits(String userEmail){
        User user = starMatchService.getUserByEmail(userEmail);
        return starMatchService.getPersonalityTraits(user);
    }

    /**
     * Retrieves and returns a user's personalized quote.
     */
    public String getPersonalizedQuote(String userEmail){
        User user = starMatchService.getUserByEmail(userEmail);
        return starMatchService.getPersonalizedQuote(user);
    }

    /**
     * Updates a user's profile information with new details
     * throws exception if the email is invalid
     */
    public void updateUser(User user, String name, String email, String password, LocalDate birthDate, LocalTime birthTime, String birthPlace){
        if(email!=null && !email.isEmpty()){
            if(starMatchService.validateEmail(email)){
                User user1=starMatchService.getUserByEmail(user.getEmail());
                starMatchService.updateUser(user1,name,email,password,birthDate,birthTime,birthPlace);}
            else
                throw new EntityNotFoundException("Invalid email");}
        else {
            User user1=starMatchService.getUserByEmail(user.getEmail());
            starMatchService.updateUser(user1,name,email,password,birthDate,birthTime,birthPlace);
        }
    }

    /**
     * Retrieves all users except the specified user.
     */
    public List<User> getAllUsersExcept(String userEmail){
        User currentUser = starMatchService.getUserByEmail(userEmail);
        return starMatchService.getAllUsersExcept(currentUser);
    }

    /**
     * Adds a friend to the user's friend list by email.
     */
    public void addFriend(String userEmail, String friendEmail){
        try{
            User user = starMatchService.getUserByEmail(userEmail);
            starMatchService.addFriend(user,friendEmail);}
        catch(EntityNotFoundException | BusinessLogicException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves a user's friend list.
     */
    public List<User> viewFriends(String userEmail){
        User user = starMatchService.getUserByEmail(userEmail);
        return starMatchService.getFriends(user);
    }

    /**
     * Removes a friend from the user's friend list.
     */
    public void removeFriend(String userEmail, String friendEmail){
        try {
            User user = starMatchService.getUserByEmail(userEmail);
            starMatchService.removeFriend(user,friendEmail);
        }
        catch(EntityNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Calculates compatibility between the user and a friend.
     * @return a Compatibility object
     */
    public Compatibility getCompatibility(String userEmail, String friendEmail){
        User user = starMatchService.getUserByEmail(userEmail);
        return starMatchService.calculateCompatibility(user, friendEmail);
    }

    /**
     *
     * Uses the filter function from the service
     * @return the filtered list of quotes
     */
    public List<Quote> filterQuotes(Element element){
        return starMatchService.filterQuotesByElement(starMatchService.getQuotes(), element);
    }

    /**
     *
     * Uses the filter function from the service
     * @return the filtered list of users
     */
    public List<User> filterUsers(int year){
        return starMatchService.filterUsersByYear(starMatchService.getUsers(), year);
    }

    /**
     *
     * Uses the function from the service
     * @return the map of the elements and the number of users which have that element
     */
    public Map<Element,Long> mostPopularElement(){
        return starMatchService.mostPopularElements(starMatchService.getUsers());
    }

    public List<User> getFriendsNearMe(String userEmail) {
        User user = starMatchService.getUserByEmail(userEmail);
        return starMatchService.getFriendsNearMe(user);
    }
}
