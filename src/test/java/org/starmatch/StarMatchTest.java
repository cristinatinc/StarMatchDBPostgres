package org.starmatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.starmatch.src.utils.InMemoryData.*;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import org.starmatch.src.model.*;
import org.starmatch.src.repository.*;

public class StarMatchTest {

    private Repository<User> userRepository;
    private Repository<Admin> adminRepository;
    private Repository<StarSign> signRepository;
    private Repository<Trait> traitRepository;
    private Repository<Quote> quoteRepository;

    @BeforeEach
    public void setUp() {
        userRepository = createInMemoryUserRepository();
        adminRepository = createInMemoryAdminRepository();
        signRepository = createInMemoryStarSignRepository();
        traitRepository = createInMemoryTraitRepository();
        quoteRepository = createInMemoryQuoteRepository();
    }

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

}
