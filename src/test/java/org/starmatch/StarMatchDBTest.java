package org.starmatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

import org.starmatch.src.model.*;
import org.starmatch.src.repository.DBRepo.*;

/**
 * This class contains unit tests for CRUD operations on various repositories
 * in the StarMatch application, using mocked database repositories.
 * Each repository is tested for creating, reading, updating, and deleting
 * entities such as users, admins, star signs, traits, and quotes.
 */
public class StarMatchDBTest {
    private UserDBRepository userDBRepo;
    private AdminDBRepository adminDBRepo;
    private StarSignDBRepository starSignDBRepo;
    private TraitDBRepository traitDBRepo;
    private QuoteDBRepository quoteDBRepo;

    /**
     * Initializes mocked database repository instances before each test.
     */
    @BeforeEach
    void setUp() {
        userDBRepo = mock(UserDBRepository.class);
        adminDBRepo = mock(AdminDBRepository.class);
        starSignDBRepo = mock(StarSignDBRepository.class);
        traitDBRepo = mock(TraitDBRepository.class);
        quoteDBRepo = mock(QuoteDBRepository.class);
    }

    /**
     * Tests the basic CRUD (Create, Read, Update, Delete) operations
     * for users, admins, star signs, traits, and quotes using mocked repositories.
     */
    @Test
    void testCRUDDBOperations() {
        // --User CRUD--
        User mockUser = new User(1, "Test User", LocalDate.of(1995, 12, 15), LocalTime.of(9, 0), "Bucharest", "testuser@gmail.com", "test123");
        doNothing().when(userDBRepo).create(mockUser);
        mockUser.setId(1);
        when(userDBRepo.get(1)).thenReturn(mockUser);
        userDBRepo.create(mockUser);
        assertEquals(1,mockUser.getId());

        User fetchedUser = userDBRepo.get(1);
        assertNotNull(fetchedUser);
        assertEquals("Test User", fetchedUser.getName());

        mockUser.setName("Updated Test User");
        doNothing().when(userDBRepo).update(mockUser);
        when(userDBRepo.get(1)).thenReturn(mockUser);

        userDBRepo.update(mockUser);
        User updatedUser = userDBRepo.get(1);
        assertEquals("Updated Test User", updatedUser.getName());

        doNothing().when(userDBRepo).delete(1);
        when(userDBRepo.get(1)).thenReturn(null);

        userDBRepo.delete(1);
        User deletedUser = userDBRepo.get(1);
        assertNull(deletedUser);

        verify(userDBRepo).create(mockUser);
        verify(userDBRepo).update(mockUser);
        verify(userDBRepo).delete(1);


        //--Admin CRUD--
        Admin mockAdmin = new Admin(1, "Admin User", "admin@gmail.com", "admin123");

        doNothing().when(adminDBRepo).create(mockAdmin);
        when(adminDBRepo.get(1)).thenReturn(mockAdmin);

        adminDBRepo.create(mockAdmin);
        assertEquals(1, mockAdmin.getId());

        Admin fetchedAdmin = adminDBRepo.get(1);
        assertNotNull(fetchedAdmin);
        assertEquals("Admin User", fetchedAdmin.getName());

        mockAdmin.setName("Updated Admin User");
        doNothing().when(adminDBRepo).update(mockAdmin);
        when(adminDBRepo.get(1)).thenReturn(mockAdmin);

        adminDBRepo.update(mockAdmin);
        Admin updatedAdmin = adminDBRepo.get(1);
        assertEquals("Updated Admin User", updatedAdmin.getName());

        doNothing().when(adminDBRepo).delete(1);
        when(adminDBRepo.get(1)).thenReturn(null);

        adminDBRepo.delete(1);
        Admin deletedAdmin = adminDBRepo.get(1);
        assertNull(deletedAdmin);

        verify(adminDBRepo).create(mockAdmin);
        verify(adminDBRepo).update(mockAdmin);
        verify(adminDBRepo).delete(1);

        //--StarSign CRUD--
        StarSign mockStarSign = new StarSign("Test Sign", Element.Air, List.of(new Trait(Element.Air, "Curious", 1)), 1);
        doNothing().when(starSignDBRepo).create(mockStarSign);

        when(starSignDBRepo.get(1)).thenReturn(mockStarSign);

        starSignDBRepo.create(mockStarSign);
        assertEquals(1, mockStarSign.getId());

        StarSign fetchedStarSign = starSignDBRepo.get(1);
        assertNotNull(fetchedStarSign);
        assertEquals("Test Sign", fetchedStarSign.getStarName());

        mockStarSign.setStarName("Updated Test Sign");
        doNothing().when(starSignDBRepo).update(mockStarSign);
        when(starSignDBRepo.get(1)).thenReturn(mockStarSign);

        starSignDBRepo.update(mockStarSign);
        StarSign updatedStarSign = starSignDBRepo.get(1);
        assertEquals("Updated Test Sign", updatedStarSign.getStarName());

        doNothing().when(starSignDBRepo).delete(1);
        when(starSignDBRepo.get(1)).thenReturn(null);

        starSignDBRepo.delete(1);
        StarSign deletedStarSign = starSignDBRepo.get(1);
        assertNull(deletedStarSign);

        verify(starSignDBRepo).create(mockStarSign);
        verify(starSignDBRepo).update(mockStarSign);
        verify(starSignDBRepo).delete(1);

        //--Trait CRUD--
        Trait mockTrait = new Trait(Element.Fire, "Test Trait", 1);
        doNothing().when(traitDBRepo).create(mockTrait);

        when(traitDBRepo.get(1)).thenReturn(mockTrait);

        traitDBRepo.create(mockTrait);
        assertEquals(1, mockTrait.getId());

        Trait fetchedTrait = traitDBRepo.get(1);
        assertNotNull(fetchedTrait);
        assertEquals("Test Trait", fetchedTrait.getTraitName());

        mockTrait.setTraitName("Updated Test Trait");
        doNothing().when(traitDBRepo).update(mockTrait);
        when(traitDBRepo.get(1)).thenReturn(mockTrait);

        traitDBRepo.update(mockTrait);
        Trait updatedTrait = traitDBRepo.get(1);
        assertEquals("Updated Test Trait", updatedTrait.getTraitName());

        doNothing().when(traitDBRepo).delete(1);
        when(traitDBRepo.get(1)).thenReturn(null);

        traitDBRepo.delete(1);
        Trait deletedTrait = traitDBRepo.get(1);
        assertNull(deletedTrait);

        verify(traitDBRepo).create(mockTrait);
        verify(traitDBRepo).update(mockTrait);
        verify(traitDBRepo).delete(1);

        //--Quote CRUD--
        Quote mockQuote = new Quote(1, Element.Fire, "Test Quote");
        doNothing().when(quoteDBRepo).create(mockQuote);

        when(quoteDBRepo.get(1)).thenReturn(mockQuote);

        quoteDBRepo.create(mockQuote);
        assertEquals(1, mockQuote.getId());

        Quote fetchedQuote = quoteDBRepo.get(1);
        assertNotNull(fetchedQuote);
        assertEquals("Test Quote", fetchedQuote.getQuoteText());

        mockQuote.setQuoteText("Updated Test Quote");
        doNothing().when(quoteDBRepo).update(mockQuote);
        when(quoteDBRepo.get(1)).thenReturn(mockQuote);

        quoteDBRepo.update(mockQuote);
        Quote updatedQuote = quoteDBRepo.get(1);
        assertEquals("Updated Test Quote", updatedQuote.getQuoteText());

        doNothing().when(quoteDBRepo).delete(1);
        when(quoteDBRepo.get(1)).thenReturn(null);

        quoteDBRepo.delete(1);
        Quote deletedQuote = quoteDBRepo.get(1);
        assertNull(deletedQuote);

        verify(quoteDBRepo).create(mockQuote);
        verify(quoteDBRepo).update(mockQuote);
        verify(quoteDBRepo).delete(1);
    }
}
