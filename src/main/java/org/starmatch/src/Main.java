package org.starmatch.src;

import org.starmatch.src.model.*;
import org.starmatch.src.repository.*;
import org.starmatch.src.repository.DBRepo.*;
import org.starmatch.src.repository.Repository;
import java.util.Scanner;
import static org.starmatch.src.utils.InMemoryData.*;

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

}
