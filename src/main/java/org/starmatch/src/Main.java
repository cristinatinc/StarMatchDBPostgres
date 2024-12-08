package org.starmatch.src;

import org.starmatch.src.model.*;
import org.starmatch.src.repository.*;
import org.starmatch.src.repository.DBRepo.*;
import org.starmatch.src.repository.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    /**
     * Main function which serves as the starting point of the application.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the storage type:");
        System.out.println("1: In-Memory Storage");
        System.out.println("2: File-based Storage");
        System.out.println("3: Database Storage");
        System.out.print("Your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        StarMatchService starMatchService;
        switch (choice) {
            case 1 -> {
                System.out.println("Using In-Memory Storage...");
                starMatchService = setupInMemoryService();
            }
            case 2 -> {
                System.out.println("Using File-based Storage...");
                System.out.print("Enter user (Cristina/Ioana): ");
                String user = scanner.nextLine();
                starMatchService = setupFileBasedService(user);
            }
            case 3 -> {
                System.out.println("Using Database Storage...");
                starMatchService = setupDatabaseService();
            }
            default -> {
                System.out.println("Invalid choice. Exiting.");
                return;
            }
        }

        StarMatchController starMatchController = new StarMatchController(starMatchService);
        ConsoleApp consoleApp = new ConsoleApp(starMatchController);
        consoleApp.start();
    }

    /**
     * Function to set up the In Memory Service
     * @return new StarMatchService
     */
    private static StarMatchService setupInMemoryService() {
        Repository<User> userRepository = createInMemoryUserRepository();
        Repository<Admin> adminRepository = createInMemoryAdminRepository();
        Repository<StarSign> signRepository = createInMemoryStarSignRepository();
        Repository<Quote> quoteRepository = createInMemoryQuoteRepository();
        Repository<Trait> traitRepository = createInMemoryTraitRepository();
        return new StarMatchService(userRepository, adminRepository, signRepository, quoteRepository, traitRepository);
    }

    /**
     * Function to set up the FileBased Service
     * @return new StarMatchServie
     */
    private static StarMatchService setupFileBasedService(String user) {
        String basePath = "C:\\Users\\" + user + "\\IdeaProjects\\StarMatchDBPostgres\\src\\main\\java\\org\\starmatch\\src\\files\\";
        Repository<User> userFileRepo = new InFileRepository<>(basePath + "users.txt", User.class);
        Repository<Admin> adminFileRepo = new InFileRepository<>(basePath + "admins.txt", Admin.class);
        Repository<StarSign> starSignFileRepo = new InFileRepository<>(basePath + "starsigns.txt", StarSign.class);
        Repository<Quote> quoteFileRepo = new InFileRepository<>(basePath + "quotes.txt", Quote.class);
        Repository<Trait> traitFileRepo = new InFileRepository<>(basePath + "traits.txt", Trait.class);
        return new StarMatchService(userFileRepo, adminFileRepo, starSignFileRepo, quoteFileRepo, traitFileRepo);
    }

    /**
     * Function to set up the Database Service
     * @return new StarMatchService
     */
    private static StarMatchService setupDatabaseService() {
        String url = "jdbc:postgresql://localhost:5432/StarMatch";
        String user = "postgres";
        String password = "1234";

        Repository<User> userDBRepo = new UserDBRepository(url, user, password);
        Repository<Admin> adminDBRepo = new AdminDBRepository(url, user, password);
        Repository<Quote> quoteDBRepo = new QuoteDBRepository(url, user, password);
        Repository<Trait> traitDBRepo = new TraitDBRepository(url, user, password);
        Repository<StarSign> starSignDBRepo = new StarSignDBRepository(url, user, password);
        return new StarMatchService(userDBRepo, adminDBRepo, starSignDBRepo, quoteDBRepo, traitDBRepo);
    }

    /**
     * User InMemoryRepository
     */
    private static Repository<User> createInMemoryUserRepository() {
        Repository<User> userRepository = new InMemoryRepository<>();
        userRepository.create(new User(1, "Amna", LocalDate.of(2000, 3, 12), LocalTime.of(9,0), "Cluj", "amna@gmail.com", "parola"));
        userRepository.create(new User(2, "Florian", LocalDate.of(2007, 7, 24), LocalTime.of(10,0), "Cluj", "florinel@gmail.com", "0987"));
        userRepository.create(new User(3, "Briana Gheorghe", LocalDate.of(2004, 1, 3), LocalTime.of(22,12), "Sibiu", "brianaagheorghe@yahoo.com", "bribri"));
        userRepository.create(new User(4, "sore marian", LocalDate.of(1990, 9, 10), LocalTime.of(6,23), "Victoria", "soremarian@gmail.com", "sore1"));
        return userRepository;
    }

    /**
     * Admin InMemoryRepository
     */
    private static Repository<Admin> createInMemoryAdminRepository() {
        Repository<Admin> adminRepository = new InMemoryRepository<>();
        adminRepository.create(new Admin(1, "Bogdan Popa", "bogdan.popa@yahoo.com", "1234"));
        adminRepository.create(new Admin(2, "Ioana Popa", "ioana.popa@yahoo.com" , "1234"));
        return adminRepository;
    }

    /**
     * StarSign InMemoryRepository
     */
    private static Repository<StarSign> createInMemoryStarSignRepository() {
        Repository<StarSign> signRepository = new InMemoryRepository<>();
        Repository<Trait> traitRepository = createInMemoryTraitRepository();

        Element fire = Element.Fire;
        Element water = Element.Water;
        Element air = Element.Air;
        Element earth = Element.Earth;
        List<Trait> fireTraits=traitRepository.getAll().stream().filter(trait -> trait.getElement()==Element.Fire).toList();
        List<Trait> waterTraits=traitRepository.getAll().stream().filter(trait -> trait.getElement()==Element.Water).toList();
        List<Trait> airTraits=traitRepository.getAll().stream().filter(trait -> trait.getElement()==Element.Air).toList();
        List<Trait> earthTraits=traitRepository.getAll().stream().filter(trait -> trait.getElement()==Element.Earth).toList();

        signRepository.create(new StarSign("Aries",fire,fireTraits,1));
        signRepository.create(new StarSign("Taurus",earth,earthTraits,2));
        signRepository.create(new StarSign("Gemini",air,airTraits,3));
        signRepository.create(new StarSign("Cancer",water,waterTraits,4));
        signRepository.create(new StarSign("Leo",fire,fireTraits,5));
        signRepository.create(new StarSign("Virgo",earth,earthTraits,6));
        signRepository.create(new StarSign("Libra",air,airTraits,7));
        signRepository.create(new StarSign("Scorpio",water,waterTraits,8));
        signRepository.create(new StarSign("Sagittarius",fire,fireTraits,9));
        signRepository.create(new StarSign("Capricorn",earth,earthTraits,10));
        signRepository.create(new StarSign("Aquarius",air,airTraits,11));
        signRepository.create(new StarSign("Pisces",water,waterTraits,12));
        return signRepository;
    }

    /**
     * Trait InMemoryRepository
     */
    private static Repository<Trait> createInMemoryTraitRepository(){
        Repository<Trait> traitRepository = new InMemoryRepository<>();
        Element fire = Element.Fire;
        Element water = Element.Water;
        Element air = Element.Air;
        Element earth = Element.Earth;

        traitRepository.create(new Trait(fire,"passionate",1));
        traitRepository.create(new Trait(fire,"playful",2));
        traitRepository.create(new Trait(fire,"energized",3));
        traitRepository.create(new Trait(water,"emotional",4));
        traitRepository.create(new Trait(water,"intuitive",5));
        traitRepository.create(new Trait(water,"nurturing",6));
        traitRepository.create(new Trait(air,"adventurous",7));
        traitRepository.create(new Trait(air,"curious",8));
        traitRepository.create(new Trait(air,"sociable",9));
        traitRepository.create(new Trait(earth,"stable",10));
        traitRepository.create(new Trait(earth,"pragmatic",11));
        traitRepository.create(new Trait(earth,"analytic",12));
        return traitRepository;
    }

    /**
     * Quote InMemoryRepository
     */
    private static Repository<Quote> createInMemoryQuoteRepository() {
        Repository<Quote> quoteRepository = new InMemoryRepository<>();
        Element fire = Element.Fire;
        Element water = Element.Water;
        Element air = Element.Air;
        Element earth = Element.Earth;

        quoteRepository.create(new Quote(1, fire ,"The only trip you will regret is the one you don’t take." ));
        quoteRepository.create(new Quote(2, fire ,"Adventure is worthwhile in itself." ));
        quoteRepository.create(new Quote(3, fire ,"Life begins at the end of your comfort zone." ));
        quoteRepository.create(new Quote(4, fire ,"Free spirits don't ask for permission." ));
        quoteRepository.create(new Quote(5, water ,"Normal is nothing more than a cycle on a washing machine." ));
        quoteRepository.create(new Quote(6, water ,"The great gift of human beings is that we have the power of empathy." ));
        quoteRepository.create(new Quote(7, water ,"To be rude to someone is not my nature." ));
        quoteRepository.create(new Quote(8, water ,"Learn as much from joy as you do from pain." ));
        quoteRepository.create(new Quote(9, air ,"That was her gift. She filled you with words you didn’t know were there." ));
        quoteRepository.create(new Quote(10, air ,"I feel like I'm too busy writing history to read it." ));
        quoteRepository.create(new Quote(11, air ,"Identify with everything. Align with nothing." ));
        quoteRepository.create(new Quote(12, air ,"Everything in the universe is within you. Ask all from yourself." ));
        quoteRepository.create(new Quote(13, earth ,"Empty yourself and let the universe fill you." ));
        quoteRepository.create(new Quote(14, earth ,"Fall seven times, stand up eight." ));
        quoteRepository.create(new Quote(15, earth ,"I have standards I don’t plan on lowering for anybody, including myself." ));
        quoteRepository.create(new Quote(16, earth ,"Be easily awed, not easily impressed." ));
        return quoteRepository;
    }
}
