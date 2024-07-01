import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;




public class LibraryCLI {
    private LibraryManagementSystem libraryManagementSystem;
    private Scanner scanner;

    public LibraryCLI(LibraryManagementSystem libraryManagementSystem) {
        this.libraryManagementSystem = libraryManagementSystem;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        String currentPath = System.getProperty("user.dir");
        loadBooksFromCSV(Paths.get(currentPath,  "Aufgabe_2", "books.csv").toString());

        boolean running = true;

        while (running) {
            System.out.println("\n--- Bücherverwaltungssystem ---");
            System.out.println("1. Buch hinzufügen");
            System.out.println("2. Alle Bücher anzeigen");
            System.out.println("3. Bücher nach Jahr filtern");
            System.out.println("4. Bücher nach Seitenanzahl sortieren");
            System.out.println("5. Gesamtanzahl der Seiten berechnen");
            System.out.println("6. Buch ausleihen");
            System.out.println("7. Buch zurückgeben");
            System.out.println("8. Ausgeliehene Bücher eines Nutzers anzeigen");
            System.out.println("9. Alle ausgeliehenen Bücher anzeigen, sortiert nach Rückgabedatum");
            System.out.println("10. Bücher nach Genre filtern");
            System.out.println("11. Durchschnittliche Bewertung pro Genre berechnen");
            System.out.println("12. Top-bewertete Bücher anzeigen");
            System.out.println("13. Autoren mit den meisten Büchern anzeigen");
            System.out.println("14. Bücher nach Bewertung sortieren");
            System.out.println("15. Gefilterte und sortierte Liste der Bücher anzeigen");
            System.out.println("16. Programm beenden");
            System.out.print("Bitte wählen Sie eine Option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    displayAllBooks();
                    break;
                case 3:
                    filterBooksByYear();
                    break;
                case 4:
                    sortBooksByPages();
                    break;
                case 5:
                    calculateTotalPages();
                    break;
                case 6:
                    borrowBook();
                    break;
                case 7:
                    returnBook();
                    break;
                case 8:
                    displayBorrowedBooksByUser();
                    break;
                case 9:
                    displayAllBorrowedBooks();
                    break;
                case 10:
                    filterBooksByGenre();
                    break;
                case 11:
                    calculateAverageRatingPerGenre();
                    break;
                case 12:
                    displayTopRatedBooks();
                    break;
                case 13:
                    displayAuthorsWithMostBooks();
                    break;
                case 14:
                    sortBooksByRating();
                    break;
                case 15:
                    filterAndSortBooks();
                    break;
                case 16:
                    running = false;
                    break;
                default:
                    System.out.println("Ungültige Option. Bitte versuchen Sie es erneut.");
            }
        }
    }

    private void loadBooksFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                Book book = new Book(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[4], Double.parseDouble(parts[5]));
                libraryManagementSystem.addBook(book);
            }
            System.out.println("Bücher aus CSV-Datei geladen.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addBook() {
        System .out.print("Bitte Titel eingeben: ");    
        String title = scanner.nextLine();
        System.out.print("Bitte Autor eingeben: ");
        String author = scanner.nextLine();
        System.out.print("Bitte Jahr eingeben: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Bitte Seitenanzahl eingeben: ");
        int pages = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Bitte Genre eingeben: ");
        String genre = scanner.nextLine();
        System.out.print("Bitte Bewertung eingeben: ");
        double rating = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        libraryManagementSystem.addBook(new Book(title, author, year, pages, genre, rating));
    }

    private void displayAllBooks() {
        System.out.println(libraryManagementSystem.getBooks());
    }

    private void filterBooksByYear() {
        System.out.println("Bücher nach Jahr filtern:");
        System.out.println("Bitte Anfangsjahr eingeben: ");
        int year = scanner.nextInt();
        System.out.println(libraryManagementSystem.filterBooksByYear(year));

    }

    private void sortBooksByPages() {
        System.out.println("Bücher sortiert nach Seitenanzahl:");
        System.out.println(libraryManagementSystem.sortBooksByPages());
    }

    private void calculateTotalPages() {
        System.out.println("Gesamtanzahl der Seiten aller Bücher: " + libraryManagementSystem.getTotalPages());  
    }

    private void borrowBook() {
        System.out.print("Bitte Nutzer-ID eingeben: ");
        String readerID = scanner.nextLine();
        User user = libraryManagementSystem.findUserByID(readerID);
        if (user == null) {
            System.out.print("Bitte Nutzernamen eingeben: ");
            String name = scanner.nextLine();
            user = new User(name, readerID);
            libraryManagementSystem.addUser(user);
            System.out.print("Neuer Nutzer angelegt: " + user);
        }
        System.out.print("\nBitte Buchtitel eingeben: ");
        String title = scanner.nextLine().toLowerCase();
        Book book = libraryManagementSystem.findBookByTitle(title);
        if  (book != null && !book.isBorrowed()) {
            System.out.println("Buch\n" + book + "\nerfolgreich ausgeliehen.");
            user.borrowBook(book);
            libraryManagementSystem.addBorowedBook(book);
            System.out.println("Rückgabedatum: " + book.getReturnDate());
        } else if (book == null) {
            System.out.println("Kein Buch mit diesem Titel gefunden.");
        } else {
            System.out.println("Dieses Buch ist bereits ausgeliehen.");
        }
    }

    private void returnBook() {
        System.out.println(libraryManagementSystem.getBooks());
        System.out.println("Bitte Titel des ausgeliehenden Buches eingeben:");
        String title = scanner.nextLine();
        for (Book book : libraryManagementSystem.getBooks()) {
            if (book.isBorrowed() && book.getTitle().contains(title)) {
                libraryManagementSystem.returnBook(book);
            } else if (!book.isBorrowed()) {
                System.out.println("Dieses Buch ist nicht ausgeliehen.");
            }
        }
    }

    private void displayBorrowedBooksByUser() {
        System.out.println("Bitte ID des Nutzers eingeben:");
        String readerID = scanner.nextLine();
        System.out.println(libraryManagementSystem.getBooksBorrowedByUser(readerID));
    }

    private void displayAllBorrowedBooks() {
        System.out.println("Alle ausgeliehenen Bücher:");
        System.out.println(libraryManagementSystem.getBorrowedBooksByReturnDate());
    }

    private void filterBooksByGenre() {
        System.out.println("Bitte wählen Sie ein Genre:");
        String genre = scanner.nextLine();
        System.out.println(libraryManagementSystem.filterBooksByGenre(genre));
    }

    private void calculateAverageRatingPerGenre() {
        System.out.println("Durchschnittliche Bewertung pro Genre:");
        System.out.println(libraryManagementSystem.getAverageRatingByGenre());
    }

    private void displayTopRatedBooks() {
        System.out.println("Top-Bewertete Bücher:");
        System.out.println(libraryManagementSystem.getTopRatedBooks());
    }

    private void displayAuthorsWithMostBooks() {
        System.out.println("Autoren mit den meisten veröffentlichten Büchern:");
        System.out.println(libraryManagementSystem.getAuthorsMostBooks());
    }

    private void sortBooksByRating() {
        System.out.println("Bücher sortiert nach Bewertung:");
        System.out.println(libraryManagementSystem.sortBooksByRating());
    }

    private void filterAndSortBooks() {
        System.out.println("Filtern nach benutzerdefinierten Kriterien:");
        System.out.println("1. Nach Jahr");
        System.out.println("2. Nach Seitenanzahl");
        System.out.println("3. Nach Bewertung");
        System.out.print("Wählen Sie ein Filterkriterium: ");
        int filterChoice = scanner.nextInt();
        System.out.print("Wählen Sie 1 für > oder 2 für <: ");
        int comparison = scanner.nextInt();
        System.out.print("Geben Sie den Wert ein: ");
        double filterValue = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        Predicate<Book> filter;
        switch (filterChoice) {
            case 1:
                filter = comparison == 1 ? book -> book.getYear() > filterValue : book -> book.getYear() < filterValue;
                break;
            case 2:
                filter = comparison == 1 ? book -> book.getPages() > filterValue : book -> book.getPages() < filterValue;
                break;
            case 3:
                filter = comparison == 1 ? book -> book.getRating() > filterValue : book -> book.getRating() < filterValue;
                break;
            default:
                System.out.println("Ungültige Auswahl.");
                return;
        }
    

        System.out.println("Sortieren nach benutzerdefinierten Kriterien:");
        System.out.println("1. Nach Titel");
        System.out.println("2. Nach Jahr");
        System.out.println("3. Nach Seitenanzahl");
        System.out.println("4. Nach Bewertung");
        System.out.print("Wählen Sie ein Sortierkriterium: ");
        int sortChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Comparator<Book> sorter;
        switch (sortChoice) {
            case 1:
                sorter = Comparator.comparing(Book::getTitle);
                break;
            case 2:
                sorter = Comparator.comparingInt(Book::getYear);
                break;
            case 3:
                sorter = Comparator.comparingInt(Book::getPages);
                break;
            case 4:
                sorter = Comparator.comparingDouble(Book::getRating);
                break;
            default:
                System.out.println("Ungültige Auswahl.");
                return;
        }

        List<Book> result = libraryManagementSystem.filterAndSortBooks(filter, sorter);
        result.forEach(System.out::println);
    }

    public static void main(String[] args) {
        LibraryManagementSystem libraryManagementSystem = new LibraryManagementSystem();
        LibraryCLI libraryCLI = new LibraryCLI(libraryManagementSystem);
        libraryCLI.run();
    }
}
