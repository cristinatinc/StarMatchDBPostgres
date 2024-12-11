package org.starmatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.starmatch.src.utils.InMemoryData.*;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import org.starmatch.src.StarMatchService;
import org.starmatch.src.exceptions.BusinessLogicException;
import org.starmatch.src.exceptions.EntityNotFoundException;
import org.starmatch.src.model.*;
import org.starmatch.src.repository.*;

/**
 * Unit tests for the StarMatch application.
 *
 * This class tests various CRUD operations, complex service methods,
 * and business logic implemented in the StarMatchService and repositories.
 */
public class StarMatchTest {

    private Repository<User> userRepository;
    private Repository<Admin> adminRepository;
    private Repository<StarSign> signRepository;
    private Repository<Trait> traitRepository;
    private Repository<Quote> quoteRepository;

    /**
     * Sets up in-memory repositories for testing.
     */
    @BeforeEach
    public void setUp() {
        userRepository = createInMemoryUserRepository();
        adminRepository = createInMemoryAdminRepository();
        signRepository = createInMemoryStarSignRepository();
        traitRepository = createInMemoryTraitRepository();
        quoteRepository = createInMemoryQuoteRepository();
    }

    /**
     * Tests basic CRUD operations for all entity types (User, Admin, StarSign, Trait, Quote).
     * Verifies creation, retrieval, update, and deletion functionality in repositories.
     */
    @Test
    public void testCRUDOperations() {
        // --- CRUD for User ---
        User user = new User(5, "Test User", LocalDate.of(1995, 12, 15), LocalTime.of(9,0), "Bucharest", "testuser@gmail.com", "test123");
        userRepository.create(user);
        User fetchedUser = userRepository.get(5);
        assertNotNull(fetchedUser);
        assertEquals("Test User", fetchedUser.getName());

        user.setName("Updated Test User");
        userRepository.update(user);
        User updatedUser = userRepository.get(5);
        assertEquals("Updated Test User", updatedUser.getName());

        userRepository.delete(5);
        User deletedUser = userRepository.get(5);
        assertNull(deletedUser);

        // --- CRUD for Admin ---
        Admin admin = new Admin(3, "Admin Test", "admin@test.com", "admin123");
        adminRepository.create(admin);
        Admin fetchedAdmin = adminRepository.get(3);
        assertNotNull(fetchedAdmin);
        assertEquals("Admin Test", fetchedAdmin.getName());

        admin.setName("Updated Admin Test");
        adminRepository.update(admin);
        Admin updatedAdmin = adminRepository.get(3);
        assertEquals("Updated Admin Test", updatedAdmin.getName());

        adminRepository.delete(3);
        Admin deletedAdmin = adminRepository.get(3);
        assertNull(deletedAdmin);

        // --- CRUD for StarSign ---
        StarSign sign = new StarSign("Test Sign", Element.Air, List.of(new Trait(Element.Air, "Curious", 1)), 13);
        signRepository.create(sign);
        StarSign fetchedSign = signRepository.get(13);
        assertNotNull(fetchedSign);
        assertEquals("Test Sign", fetchedSign.getStarName());

        sign.setStarName("Updated Test Sign");
        signRepository.update(sign);
        StarSign updatedSign = signRepository.get(13);
        assertEquals("Updated Test Sign", updatedSign.getStarName());

        signRepository.delete(13);
        StarSign deletedSign = signRepository.get(13);
        assertNull(deletedSign);

        // --- CRUD for Trait ---
        Trait trait = new Trait(Element.Fire, "Test Trait", 13);
        traitRepository.create(trait);
        Trait fetchedTrait = traitRepository.get(13);
        assertNotNull(fetchedTrait);
        assertEquals("Test Trait", fetchedTrait.getTraitName());

        trait.setTraitName("Updated Test Trait");
        traitRepository.update(trait);
        Trait updatedTrait = traitRepository.get(13);
        assertEquals("Updated Test Trait", updatedTrait.getTraitName());

        traitRepository.delete(13);
        Trait deletedTrait = traitRepository.get(13);
        assertNull(deletedTrait);

        // --- CRUD for Quote ---
        Quote quote = new Quote(17, Element.Fire, "Test Quote");
        quoteRepository.create(quote);
        Quote fetchedQuote = quoteRepository.get(17);
        assertNotNull(fetchedQuote);
        assertEquals("Test Quote", fetchedQuote.getQuoteText());

        quote.setQuoteText("Updated Test Quote");
        quoteRepository.update(quote);
        Quote updatedQuote = quoteRepository.get(17);
        assertEquals("Updated Test Quote", updatedQuote.getQuoteText());

        quoteRepository.delete(17);
        Quote deletedQuote = quoteRepository.get(17);
        assertNull(deletedQuote);
    }

    /**
     * Tests complex operations and business logic in StarMatchService.
     *
     * This includes:
     * - Calculating natal charts and personality traits
     * - Generating personalized quotes
     * - Adding and removing friends
     * - Validating emails
     * - Filtering users and quotes
     * - Determining the most popular elements among users
     * - Finding friends near a user's location
     */
    @Test
    public void testCalculateNatalChart() {
        User user = new User(5, "Test User", LocalDate.of(1995, 12, 15), LocalTime.of(9, 0), "Bucharest", "testuser@gmail.com", "test123");
        userRepository.create(user);
        User friend = new User(6, "Friend", LocalDate.of(2001, 6, 23), LocalTime.of(10, 0), "Bucharest", "testfriend@gmail.com", "test123");
        userRepository.create(friend);
        StarMatchService service = new StarMatchService(userRepository, adminRepository, signRepository, quoteRepository, traitRepository);

        NatalChart chart = service.getNatalChart(user);
        NatalChart chart1 = service.getNatalChart(friend);

        assertEquals(3, chart.getPlanets().size());
        assertEquals("Sagittarius", chart.getPlanets().getFirst().getSign().getStarName());
        assertEquals("Cancer", chart1.getPlanets().getFirst().getSign().getStarName());
    }

    @Test
    public void testPersonalityTraits() {
        User user = new User(5, "Test User", LocalDate.of(1995, 12, 15), LocalTime.of(9, 0), "Bucharest", "testuser@gmail.com", "test123");
        userRepository.create(user);
        User friend = new User(6, "Friend", LocalDate.of(2001, 6, 23), LocalTime.of(10, 0), "Bucharest", "testfriend@gmail.com", "test123");
        userRepository.create(friend);
        StarMatchService service = new StarMatchService(userRepository, adminRepository, signRepository, quoteRepository, traitRepository);

        assertEquals(List.of("passionate", "playful", "energized"), service.getPersonalityTraits(user));
        assertEquals(List.of("emotional", "intuitive", "nurturing"), service.getPersonalityTraits(friend));
    }

    @Test
    public void testPersonalizedQuote() {
        User user = new User(5, "Test User", LocalDate.of(1995, 12, 15), LocalTime.of(9, 0), "Bucharest", "testuser@gmail.com", "test123");
        userRepository.create(user);
        StarMatchService service = new StarMatchService(userRepository, adminRepository, signRepository, quoteRepository, traitRepository);

        String quote = service.getPersonalizedQuote(user);
        assertTrue(
                quote.equals("The only trip you will regret is the one you don’t take.")
                        || quote.equals("Adventure is worthwhile in itself.")
                        || quote.equals("Life begins at the end of your comfort zone.")
                        || quote.equals("Free spirits don't ask for permission.")
        );
    }

    @Test
    public void testFriendManagement() {
        User user = new User(5, "Test User", LocalDate.of(1995, 12, 15), LocalTime.of(9, 0), "Bucharest", "testuser@gmail.com", "test123");
        userRepository.create(user);
        User friend = new User(6, "Friend", LocalDate.of(2001, 6, 23), LocalTime.of(10, 0), "Bucharest", "testfriend@gmail.com", "test123");
        userRepository.create(friend);
        StarMatchService service = new StarMatchService(userRepository, adminRepository, signRepository, quoteRepository, traitRepository);

        service.addFriend(user, friend.getEmail());
        assertTrue(user.getRawFriendEmails().contains(friend.getEmail()));

        BusinessLogicException exceptionAddYourself = assertThrows(BusinessLogicException.class, () -> service.addFriend(user, user.getEmail()));
        assertEquals("You cannot add yourself as your friend", exceptionAddYourself.getMessage());

        EntityNotFoundException exceptionAddInvalidFriend = assertThrows(EntityNotFoundException.class, () -> service.addFriend(user, "unemail@yahoo.com"));
        assertEquals("User with that email does not exist", exceptionAddInvalidFriend.getMessage());

        service.removeFriend(user, friend.getEmail());
        assertFalse(user.getRawFriendEmails().contains(friend.getEmail()));

        EntityNotFoundException exceptionRemoveInvalidFriend = assertThrows(EntityNotFoundException.class, () -> service.removeFriend(user, "unemail@yahoo.com"));
        assertEquals("User with that email does not exist", exceptionRemoveInvalidFriend.getMessage());
    }

    @Test
    public void testCompatibilityCalculations() {
        User user = new User(5, "Test User", LocalDate.of(1995, 12, 15), LocalTime.of(9, 0), "Bucharest", "testuser@gmail.com", "test123");
        userRepository.create(user);
        User friend = new User(6, "Friend", LocalDate.of(2001, 6, 23), LocalTime.of(10, 0), "Bucharest", "testfriend@gmail.com", "test123");
        userRepository.create(friend);
        StarMatchService service = new StarMatchService(userRepository, adminRepository, signRepository, quoteRepository, traitRepository);

        service.addFriend(user, friend.getEmail());
        Compatibility result = service.calculateCompatibility(user, friend.getEmail());
        assertTrue(result.getCompatibilityScore() >= 0 && result.getCompatibilityScore() <= 100);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.calculateCompatibility(user, "unemail@gmail.com"));
        assertEquals("User with that email does not exist", exception.getMessage());
    }

    @Test
    public void testEmailValidation() {
        StarMatchService service = new StarMatchService(userRepository, adminRepository, signRepository, quoteRepository, traitRepository);

        assertTrue(service.validateEmail("test@gmail.com"));
        assertFalse(service.validateEmail("test.com"));
        assertFalse(service.validateEmail("test@com"));
        assertFalse(service.validateEmail("testemail"));
        assertFalse(service.validateEmail(""));
    }

    @Test
    public void testFilterMethods() {
        User user = new User(5, "Test User", LocalDate.of(1995, 12, 15), LocalTime.of(9, 0), "Bucharest", "testuser@gmail.com", "test123");
        userRepository.create(user);
        User friend = new User(6, "Friend", LocalDate.of(2001, 6, 23), LocalTime.of(10, 0), "Bucharest", "testfriend@gmail.com", "test123");
        userRepository.create(friend);
        StarMatchService service = new StarMatchService(userRepository, adminRepository, signRepository, quoteRepository, traitRepository);

        List<User> users = List.of(user, friend);
        assertEquals(List.of(friend), service.filterUsersByYear(users, 2001));

        List<Quote> quotes = List.of(new Quote(1, Element.Air, "test"), new Quote(2, Element.Air, "test1"), new Quote(3, Element.Fire, "test2"));
        assertEquals(2, service.filterQuotesByElement(quotes, Element.Air).size());
    }

    @Test
    public void testMostPopularElements() {
        User user1 = new User(5, "User1", LocalDate.of(1995, 12, 15), LocalTime.of(9, 0), "Bucharest", "testuser1@gmail.com", "test123");
        User user2 = new User(6, "User2", LocalDate.of(1996, 5, 22), LocalTime.of(10, 0), "Cluj", "testuser2@gmail.com", "test123");
        userRepository.create(user1);
        userRepository.create(user2);
        StarMatchService service = new StarMatchService(userRepository, adminRepository, signRepository, quoteRepository, traitRepository);

        Map<Element, Long> popularElements = service.mostPopularElements(userRepository.getAll());
        assertNotNull(popularElements);
        assertFalse(popularElements.isEmpty());
    }

    @Test
    public void testFriendsNearMe() {
        User userNear = new User(7, "Near User", LocalDate.of(2001, 6, 23), LocalTime.of(10, 0), "Cluj", "testnear@gmail.com", "test123");
        User userFar = new User(8, "Far User", LocalDate.of(1990, 12, 15), LocalTime.of(9, 0), "Bucharest", "testfar@gmail.com", "test123");
        userRepository.create(userNear);
        userRepository.create(userFar);
        StarMatchService service = new StarMatchService(userRepository, adminRepository, signRepository, quoteRepository, traitRepository);

        List<User> friendsNear = service.getFriendsNearMe(userNear);
        assertNotNull(friendsNear);
        for (User friend : friendsNear)
            assertEquals("Cluj", friend.getBirthPlace());
    }

}

