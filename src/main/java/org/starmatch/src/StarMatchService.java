package org.starmatch.src;

import org.starmatch.src.model.*;
import org.starmatch.src.repository.Repository;
import org.starmatch.src.exceptions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StarMatchService provides methods for managing users, admins, quotes, traits, and compatibility calculations
 * within the StarMatch application.
 */
public class StarMatchService {
    private final Repository<User> userRepository;
    private final Repository<Admin> adminRepository;
    private final Repository<StarSign> signRepository;
    private final Repository<Quote> quoteRepository;
    private final Repository<Trait> traitRepository;

    /**
     * Initializes StarMatchService with the given repositories.
     */
    public StarMatchService(Repository<User> userRepository, Repository<Admin> adminRepository, Repository<StarSign> signRepository, Repository<Quote> quoteRepository, Repository<Trait> traitRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.signRepository = signRepository;
        this.quoteRepository = quoteRepository;
        this.traitRepository = traitRepository;
    }

    /**
     * Validates user login credentials.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return true if login is valid, false otherwise
     */
    public boolean validateUserLogin(String email, String password) {
        return userRepository.getAll().stream()
                .anyMatch(user -> user.getEmail().equals(email) && user.getPassword().equals(password));
    }

    /**
     * Validates admin login credentials.
     *
     * @param email    the admin's email
     * @param password the admin's password
     * @return true if login is valid, false otherwise
     */
    public boolean validateAdminLogin(String email, String password) {
        return adminRepository.getAll().stream()
                .anyMatch(admin -> admin.getEmail().equals(email) && admin.getPassword().equals(password));
    }

    /**
     * Creates a new user and adds them to the user repository.
     */
    public void createUser(String name, LocalDate birthDate, LocalTime birthTime, String birthPlace, String email, String password) {
        User newUser = new User(getMaxId(userRepository) + 1, name, birthDate, birthTime, birthPlace, email, password);
        userRepository.create(newUser);
    }

    /**
     * Removes a user by ID from the user repository.
     */
    public void removeUser(Integer userId) {
        if(!userRepository.getAll().contains(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.delete(userId);
    }

    /**
     * Creates a new admin and adds them to the admin repository.
     */
    public void createAdmin(String name, String email, String password) {
        Admin newAdmin = new Admin(getMaxId(adminRepository) + 1, name, email, password);
        adminRepository.create(newAdmin);
    }

    /**
     * Removes an admin by ID from the admin repository.
     */
    public void removeAdmin(Integer adminId){
        adminRepository.delete(adminId);
    }

    /**
     * Updates admin information.
     */
    public void updateAdmin(Integer adminId, String name, String email, String password) {
        Admin admin = adminRepository.get(adminId);
        if (!name.isBlank()) admin.setName(name);
        if (!email.isBlank()) admin.setEmail(email);
        if (!password.isBlank()) admin.setPassword(password);
        adminRepository.update(admin);
    }

    /**
     * Creates a new quote and adds it to the quote repository.
     */
    public void createQuote(String newQuoteText, String element) {
        if(newQuoteText==null)
            throw new EntityNotFoundException("New quote text is null");
        Element quoteElement = null;
        for (Element e : Element.values()) {
            if (e.name().equalsIgnoreCase(element)) {
                quoteElement = e;
                break;
            }
        }
        Quote newQuote = new Quote(getMaxId(quoteRepository) + 1, quoteElement, newQuoteText);
        quoteRepository.create(newQuote);
    }

    /**
     * Removes a quote by ID from the quote repository.
     */
    public void removeQuote(Integer quoteId) {
        if(!quoteRepository.getAll().contains(quoteId)) {
            throw new EntityNotFoundException("Quote not found");
        }
        quoteRepository.delete(quoteId);
    }

    /**
     * Updates a quote's text by its ID.
     */
    public void updateQuote(Integer quoteId, String newQuoteText) {
        Quote quote = quoteRepository.get(quoteId);
        quote.setQuoteText(newQuoteText);
        quoteRepository.update(quote);
    }

    /**
     * Creates a new trait and adds it to the trait repository.
     */
    public void createTrait(String traitName, Element element){
        if(traitRepository.getAll().contains(traitName) || traitName==null || element==null) {
            throw new ValidationException("Trait name or element is null or the trait already exists");
        }
        Trait trait=new Trait(element,traitName,getMaxId(traitRepository)+1);
        traitRepository.create(trait);
    }

    /**
     * Removes a trait by ID from the trait repository.
     */
    public void removeTrait(Integer traitId){
        if(!traitRepository.getAll().contains(traitId)) {
            throw new EntityNotFoundException("Trait not found");
        }
        traitRepository.delete(traitId);
    }

    /**
     * Updates a trait's information.
     */
    public void updateTrait(Integer traitID, String traitName, Element element){
        Trait trait = traitRepository.get(traitID);
        if(!traitName.isBlank()) trait.setTraitName(traitName);
        if(element!=null) trait.setElement(element);
        traitRepository.update(trait);
    }

    /**
     * Retrieves the maximum ID from the given repository.
     *
     * @param repository the repository containing elements with IDs
     * @param <T>        a type that implements the HasId interface
     * @return the maximum ID found in the repository, or 0 if empty
     */
    public <T extends HasId> int getMaxId(Repository<T> repository) {
        return repository.getAll().stream()
                .mapToInt(T::getId)
                .max()
                .orElse(0);
    }

    /**
     * Retrieves a list of all admins.
     *
     * @return a list of all Admin objects
     */
    public List<Admin> getAdmins() { return adminRepository.getAll();}

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all User objects
     */
    public List<User> getUsers() {
        List<User> users = userRepository.getAll();
        sortUsersByBirthdate(users);
        return users;}

    /**
     * Retrieves a list of all quotes.
     *
     * @return a list of all Quote objects
     */
    public List<Quote> getQuotes() { return quoteRepository.getAll();}

    /**
     * Retrieves a list of all traits.
     *
     * @return a list of all Trait objects
     */
    public List<Trait> getTraits(){
        List<Trait> traits = traitRepository.getAll();
        sortTraitsByElement(traits);
        return traits;}

    /**
     * Retrieves a user by their email address.
     *
     * @param email the user's email address
     * @return the User object if found, or null if not found
     */
    public User getUserByEmail(String email) {
        return userRepository.getAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Generates a natal chart for a user, containing their sun, moon, and rising signs.
     *
     * @param user the user for whom the natal chart is generated
     * @return a NatalChart object with the calculated planetary positions of the user
     */
    public NatalChart getNatalChart(User user) {

        List<Planet> planets = new ArrayList<>();
        LocalDate birthDate = user.getBirthDate();
        LocalTime birthTime = user.getBirthTime();

        StarSign sunSign = calculateSunSign(birthDate);
        StarSign moonSign = calculateMoonSign(birthDate);
        StarSign risingSign = calculateRisingSign(birthTime);

        planets.add(new Planet("Sun", sunSign, 1));
        planets.add(new Planet("Moon", moonSign, 2));
        planets.add(new Planet("Rising", risingSign, 3));

        return new NatalChart(planets);
    }

    /**
     * Calculates the sun sign based on the user's birth date.
     *
     * @param birthDate the user's birth date
     * @return the user's sun sign as a StarSign object
     */
    private StarSign calculateSunSign(LocalDate birthDate) {
        int day = birthDate.getDayOfMonth();
        String sunSignName;

        switch (birthDate.getMonth()) {
            case MARCH -> sunSignName = (day < 21) ? "Pisces" : "Aries";
            case APRIL -> sunSignName = (day < 20) ? "Aries" : "Taurus";
            case MAY -> sunSignName = (day < 21) ? "Taurus" : "Gemini";
            case JUNE -> sunSignName = (day < 21) ? "Gemini" : "Cancer";
            case JULY -> sunSignName = (day < 23) ? "Cancer" : "Leo";
            case AUGUST -> sunSignName = (day < 23) ? "Leo" : "Virgo";
            case SEPTEMBER -> sunSignName = (day < 23) ? "Virgo" : "Libra";
            case OCTOBER -> sunSignName = (day < 23) ? "Libra" : "Scorpio";
            case NOVEMBER -> sunSignName = (day < 22) ? "Scorpio" : "Sagittarius";
            case DECEMBER -> sunSignName = (day < 22) ? "Sagittarius" : "Capricorn";
            case JANUARY -> sunSignName = (day < 20) ? "Capricorn" : "Aquarius";
            case FEBRUARY -> sunSignName = (day < 19) ? "Aquarius" : "Pisces";
            default -> sunSignName = "Unknown";
        }

        return signRepository.getAll().stream().filter(starSign -> starSign.getStarName().equals(sunSignName)).findFirst().orElse(null);
    }

    /**
     * Calculates the moon sign based on the user's birth date.
     *
     * @param birthDate the user's birth date
     * @return the user's moon sign as a StarSign object
     */
    private StarSign calculateMoonSign(LocalDate birthDate){
        long daysSinceFixedDate = java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.of(2000, 1, 1), birthDate);
        int moonIndex = (int) ((daysSinceFixedDate / 2.5) % 12);
        if(moonIndex<0)
            moonIndex+=12;
        String moonSignName = getZodiacSignFromIndex(moonIndex);
        return signRepository.getAll().stream().filter(starSign -> starSign.getStarName().equals(moonSignName)).findFirst().orElse(null);
    }

    /**
     * Calculates the rising sign based on the user's birth time.
     *
     * @param birthTime the user's birth time
     * @return the user's rising sign as a StarSign object
     */
    private StarSign calculateRisingSign(LocalTime birthTime) {
        int hour = birthTime.getHour();
        int risingIndex = (hour / 2) % 12;
        String risingSign= getZodiacSignFromIndex(risingIndex);
        return signRepository.getAll().stream().filter(starSign -> starSign.getStarName().equals(risingSign)).findFirst().orElse(null);
    }

    /**
     * Retrieves a zodiac sign based on its index in the zodiac cycle.
     *
     * @param index the index corresponding to a zodiac sign
     * @return the name of the zodiac sign at the specified index
     */
    private String getZodiacSignFromIndex(int index) {
        String[] zodiacSigns = {
                "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
                "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
        };
        int checkIndex=index%12;
        if(checkIndex<0)
            checkIndex+=12;
        return zodiacSigns[index];
    }

    /**
     * Retrieves a list of personality traits for a user based on their natal chart.
     *
     * @param user the user for whom personality traits are retrieved
     * @return a list of personality trait names
     */
    public List<String> getPersonalityTraits(User user){
        NatalChart chart=getNatalChart(user);
        StarSign sunSign=chart.getPlanets().getFirst().getSign();
        return sunSign.getTraits().stream()
                .map(Trait::getTraitName)
                .collect(Collectors.toList());
    }

    /**
     * Generates a personalized quote for the user based on their sun sign's element.
     *
     * @param user the user for whom the quote is generated
     * @return a random quote matching the user's element
     */
    public String getPersonalizedQuote(User user){
        NatalChart chart=getNatalChart(user);
        Element element=chart.getPlanets().getFirst().getSign().getElement();
        List<String> quotes=quoteRepository.getAll().stream().filter(quote -> quote.getElement().equals(element)).map(Quote::getQuoteText).toList();
        Random random=new Random();
        return quotes.get(random.nextInt(quotes.size()));
    }

    /**
     * Updates a user's information with new details
     */
    public void updateUser(User user,String name, String email, String password, LocalDate birthDate, LocalTime birthTime, String birthPlace){
        User user1 = userRepository.get(user.getId());
        if (!name.isBlank()) user1.setName(name);
        if (!email.isBlank()) user1.setEmail(email);
        if (!password.isBlank()) user1.setPassword(password);
        if (birthDate!=null) user1.setBirthDate(birthDate);
        if (birthTime!=null) user1.setBirthTime(birthTime);
        if (!birthPlace.isBlank()) user1.setBirthPlace(birthPlace);
        userRepository.update(user1);
    }

    /**
     * Retrieves all users except a specified user.
     *
     * @param currentUser the user to exclude from the list
     * @return a list of users excluding the specified user
     */
    public List<User> getAllUsersExcept(User currentUser){
        return userRepository.getAll().stream().filter(user -> !user.equals(currentUser)).collect(Collectors.toList());
    }

    /**
     * Adds a friend to the user's friend list by email.
     *
     * @param user the user adding a friend
     * @param friendEmail the email of the friend to add
     * @throws NoSuchElementException if a user with the specified email does not exist
     */
    public void addFriend(User user, String friendEmail) {
        if(user.getEmail().equals(friendEmail))
            throw new BusinessLogicException("You cannot add yourself as your friend");
        User friend = userRepository.getAll().stream()
                .filter(user1 -> user1.getEmail().equals(friendEmail))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User with that email does not exist"));

        if (!user.getRawFriendEmails().contains(friendEmail)) {
            user.getRawFriendEmails().add(friendEmail);
            user.getFriends().add(friend);

            userRepository.update(user);
        }
    }


    /**
     * Retrieves a list of a user's friends.
     *
     * @param user the user whose friends are retrieved
     * @return a list of User objects representing the user's friends
     */
    public List<User> getFriends(User user) {
        refreshFriendsList(user);
        return user.getFriends();
    }

    private void refreshFriendsList(User user) {
        List<User> resolvedFriends = user.getRawFriendEmails().stream()
                .map(email -> userRepository.getAll().stream()
                        .filter(u -> u.getEmail().equals(email))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
        user.setFriends(resolvedFriends);
    }


    /**
     * Removes a friend from the user's friend list by email.
     *
     * @param user the user removing a friend
     * @param friendEmail the email of the friend to remove
     * @throws NoSuchElementException if a user with the specified email does not exist
     */
    public void removeFriend(User user, String friendEmail) {
        User friend = userRepository.getAll().stream()
                .filter(u -> u.getEmail().equals(friendEmail))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User with that email does not exist"));

        if (user.getRawFriendEmails().contains(friendEmail)) {
            user.getRawFriendEmails().remove(friendEmail);
            friend.getRawFriendEmails().remove(user.getEmail());

            userRepository.update(user);
            userRepository.update(friend);
        }
    }


    /**
     * Calculates compatibility between a user and a friend.
     *
     * @param user the user for whom compatibility is calculated
     * @param friendEmail the email of the friend to calculate compatibility with
     * @return a Compatibility object with the calculated compatibility score
     * @throws NoSuchElementException if the specified friend is not found or not in the user's friend list
     */
    public Compatibility calculateCompatibility(User user, String friendEmail){
        User friend=userRepository.getAll().stream().filter(user1 -> user1.getEmail().equals(friendEmail)).findFirst().orElseThrow(() -> new NoSuchElementException("User with that email does not exist"));
        if(!user.getFriends().contains(friend) && !user.getRawFriendEmails().contains(friendEmail))
            throw new EntityNotFoundException("That User is not your friend");

        NatalChart chartUser=getNatalChart(user);
        NatalChart chartFriend=getNatalChart(friend);

        StarSign userSunSign=chartUser.getPlanets().getFirst().getSign();
        StarSign friendSunSign=chartFriend.getPlanets().getFirst().getSign();
        StarSign userMoonSign=chartUser.getPlanets().get(1).getSign();
        StarSign friendMoonSign=chartFriend.getPlanets().get(1).getSign();
        StarSign userRisingSign=chartUser.getPlanets().getLast().getSign();
        StarSign friendRisingSign=chartFriend.getPlanets().getLast().getSign();

        boolean sunCompatible=checkElementCompatibility(userSunSign.getElement(),friendSunSign.getElement());
        boolean moonCompatible=checkElementCompatibility(userMoonSign.getElement(),friendMoonSign.getElement());
        boolean risingCompatible=checkElementCompatibility(userRisingSign.getElement(),friendRisingSign.getElement());

        long actualCompatibility=calculateBinaryCompatibility(userSunSign,friendSunSign,sunCompatible)+calculateBinaryCompatibility(userMoonSign,friendMoonSign,moonCompatible)+calculateBinaryCompatibility(userRisingSign,friendRisingSign,risingCompatible);
        actualCompatibility=actualCompatibility/500000;
        if(actualCompatibility>100)
            actualCompatibility=100;
        return new Compatibility(actualCompatibility,friend.getId(),user.getId());
    }

    /**
     * Checks if two elements are compatible for the actual calculation of the compatibility.
     *
     * @param userElement  the user's element
     * @param friendElement the friend's element
     * @return true if the elements are compatible, false otherwise
     */
    private boolean checkElementCompatibility(Element userElement, Element friendElement){
        if (userElement.equals(friendElement))
            return true;
        if (userElement.equals(Element.Fire) && friendElement.equals(Element.Air))
            return true;
        if (userElement.equals(Element.Air) && friendElement.equals(Element.Fire))
            return true;
        if (userElement.equals(Element.Water) && friendElement.equals(Element.Earth))
            return true;
        return userElement.equals(Element.Earth) && friendElement.equals(Element.Water);
    }

    /**
     * Converts the name of a star sign into a binary representation.
     *
     * @param starName the name of the star sign
     * @return the binary representation of the star sign's name as a long
     */
    private long convertNameToBinary(String starName){
        long binary=0;
        for(char c:starName.toCharArray()){
            binary+=Integer.parseInt(Integer.toBinaryString(c));
        }
        return binary;
    }

    /**
     * Calculates a compatibility score between two star signs.
     *
     * @param userStarSign the user's star sign
     * @param friendStarSign the friend's star sign
     * @param compatible a boolean indicating if the elements are compatible
     * @return the compatibility score as a long
     */
    private long calculateBinaryCompatibility(StarSign userStarSign, StarSign friendStarSign, boolean compatible){
        long userStarNameBinary=convertNameToBinary(userStarSign.getStarName());
        long friendStarNameBinary=convertNameToBinary(friendStarSign.getStarName());
        long score=userStarNameBinary+friendStarNameBinary;
        if(!compatible)
            score-=5000000;
        return score;
    }

    /**
     * Validates an email address against a regular expression pattern.
     *
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
     */
    public boolean validateEmail(String email){
        String emailValid = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailValid);
    }

    /**
     * Sorts the users by their birth date in ascending order.
     * If the provided list is null or empty, it returns an empty list.
     *
     * @param users the list of users to be sorted
     * @return a list of users sorted by their birth date, or an empty list if input is null or empty
     */
    public List<User> sortUsersByBirthdate(List<User> users){
        if(users == null || users.isEmpty())
            return Collections.emptyList();
        users.sort(Comparator.comparing(User::getBirthDate));
        return users;
    }

    /**
     * Sorts the traits by their associated element in the order of
     * Air, Earth, Fire, and Water.
     * If the provided list is null or empty, it returns an empty list.
     *
     * @param traits the list of personality traits to be sorted
     * @return a list of personality traits sorted by their element, or an empty list if input is null or empty
     */
    public List<Trait> sortTraitsByElement(List<Trait> traits){
        if(traits == null || traits.isEmpty())
            return Collections.emptyList();
        List<Element> elementOrder = List.of(Element.Air,Element.Earth,Element.Fire,Element.Water);
        traits.sort(Comparator.comparing(trait -> elementOrder.indexOf(trait.getElement())));
        return traits;
    }

    /**
     * Filters the users to only include those who were born in a specific year.
     * If the provided list is null or empty, or no users match the year, it returns an empty list.
     *
     * @param users the list of users to filter
     * @param year the year to filter users by
     * @return a list of users born in the specified year, or an empty list if input is null, empty, or no users match the year
     */
    public List<User> filterUsersByYear(List<User> users, int year){
        if(users == null || users.isEmpty())
            return Collections.emptyList();
        return users.stream().filter(user -> user.getBirthDate().getYear() == year).collect(Collectors.toList());
    }

    /**
     * Filters the quotes to only include those associated with a specific element.
     * If the provided list is null or empty, or no quotes match the element, it returns an empty list.
     *
     * @param quotes the list of quotes to filter
     * @param element the element to filter quotes by
     * @return a list of quotes associated with the specified element, or an empty list if input is null, empty, or no quotes match the element
     */
    public List<Quote> filterQuotesByElement(List<Quote> quotes, Element element){
        if(quotes == null || quotes.isEmpty() || element == null)
            return Collections.emptyList();
        return quotes.stream().filter(quote -> quote.getElement().equals(element)).collect(Collectors.toList());
    }


    /**
     * Calculates the most popular elements among users based on their sun signs.
     * This method counts the occurrences of each element (Air, Earth, Fire, Water) in the users' sun signs
     * and returns a map of the elements sorted by their popularity in descending order.
     * If the provided list of users is null or empty, it returns an empty map.
     *
     * @param users the list of users to analyze
     * @return a map of elements with their count, sorted by the most popular element, or an empty map if input is null or empty
     */
    public Map<Element,Long> mostPopularElements(List<User> users){
        if(users == null || users.isEmpty())
            return Collections.emptyMap();
        return users.stream().map(user -> calculateSunSign(user.getBirthDate())).filter(Objects::nonNull).map(StarSign::getElement).
                collect(Collectors.groupingBy(element -> element,Collectors.counting())).entrySet().
                stream().sorted(Map.Entry.<Element, Long>comparingByValue().reversed()).
                collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1,e2)->e1,LinkedHashMap::new));

    }

    public List<User> getFriendsNearMe(User user) {
        ArrayList<User> usersNearMe = new ArrayList<>( userRepository.getAll().stream().filter(user1 -> user1.getBirthPlace().equals(user.getBirthPlace())).toList());
        usersNearMe.removeIf(u -> u.getEmail().equals(user.getEmail()));
        return usersNearMe;
    }
}
