import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

public class LibraryManagementSystem {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private Set<Book> books;
    private Set<Book> borrowedBooksByReturnDate;
    private Map<String, User> users;

    /**
     * Konstruktor der eine neue Instanz der Klasse LibraryManagementSystem erstellt
     */
    public LibraryManagementSystem () {
        this.books = new TreeSet<>(Comparator.comparing(Book::getTitle));
        this.users = new HashMap<>();
        this.borrowedBooksByReturnDate = new TreeSet<>(Comparator.comparing(Book::getReturnDate));
    }

    /**
     * Fuegt einen Nutzer hinzu, falls er noch nicht existiert
     * Wird in der Map gespeichert und mit seiner readerID verknuepft
     * @param user Nutzer, der hinzugefuegt werden soll
     */
    public void addUser (User user) {
        users.putIfAbsent(user.getReaderID(), user);
    }

    /**
     * Fuegt ein Buch hinzu
     * @param book hinzuzufuegendes Buch
     */
    public void addBook (Book book) {
        books.add(book);
    }

    /**
     * Methode zum Ausleihen eines Buches
     * @param readerID ID des Users, der das Buch ausleihen will
     * @param book zu ausgeliehendes Buch
     */
    public void borrowBook (String readerID, Book book) {
        User user = users.get(readerID);
        if (user != null) {
            user.borrowBook(book);
            borrowedBooksByReturnDate.add(book);
        }
    }

    /**
     * Methode zur Rückgabe eines Buches
     * @param book zurückzugebenes Buch
     */
    public void returnBook (Book book) {
        borrowedBooksByReturnDate.remove(book);
        book.setBorrowedStatus(false);
    }

    /**
     * Gibt alle Bücher aus, die von einem bestimmten User ausgeliehen wurden
     * @param readerID ID des Users
     * @return Set mit allen Büchern, die von diesem User ausgeliehen wurden
     */
    public Set<Book> getBooksBorrowedByUser (String readerID) {
        User user = users.get(readerID);
        if (user == null) {
            return null;
        }
        return user.getBorrowedBooks();
    }

    /**
     * Gibt alle ausgeliehenen Bücher sortiert nach Rückgabedatum aus
     * @return
     */
    public Set<Book> getBorrowedBooksByReturnDate () {
        return borrowedBooksByReturnDate;
    }   

    /**
     * Gibt alle Bücher aus, die in oder nach einem vom User angegebenen Jahr veröffentlich wurden
     * @param year Jahr, in oder nach dem die Bücher angezeigt werden sollen
     * @return Liste der Bücher, die in oder nach dem angegebenen Jahr veröffentlich wurden
     */
    public List<Book> filterBooksByYear (int year) {
        List<Book> filteredBooks = books.stream()
            .filter(book -> book.getYear() >= year)
            .sorted(Comparator.comparing(Book::getYear))
            .toList();
        return filteredBooks;
    }

    /**
     * Gibt alle Bücher sortiert nach Seitenanzahl aus
     * @return Liste der Bücher sortiert nach Seitenanzahl
     */
    public List<Book> sortBooksByPages () {
        List<Book> sortedBooks = books.stream()
            .sorted(Comparator.comparing(Book::getPages))
            .toList();
        return sortedBooks;
    }

    /**
     * Gibt die Gesamtseitenanzahl aller Bücher aus
     * @return Gesamtseitenanzahl
     */
    public int getTotalPages () {
        return books.stream()
            .mapToInt(Book::getPages)
            .sum();
    }

    /**
     * Filtert alle Bücher nach einem bestimmten Genre
     * @param genre vom User angegebenes Genre, nach dem gefiltert werden soll
     * @return Liste aller gefilterten Bücher
     */
    public List<Book> filterBooksByGenre (String genre) {
        List<Book> filteredBooks = books.stream()
            .filter(book -> book.getGenre().equals(genre))
            .sorted(Comparator.comparing(Book::getYear))
            .toList();
        return filteredBooks;
    }

    /**
     * Gibt die durchschnittliche Bewertung pro Genre aus
     * Bewertung wird auf 2 Nachkommastellen gerundet
     * @return Map mit Genre als Key und durchschnittliche Bewertung als Value
     */
    public Map<String, String> getAverageRatingByGenre () {
        return books.stream()
            .collect(Collectors.groupingBy(Book::getGenre, Collectors.averagingDouble(Book::getRating)))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> df.format(e.getValue()),
                (oldValue, newValue) -> oldValue
            ));
    }   

    /**
     * Gibt die drei am besten bewerteten Bücher aus
     * @return Liste mit den drei am meisten bewerteten Büchern
     */
    public List<Book> getTopRatedBooks () {
        return books.stream()
            .sorted(Comparator.comparingDouble(Book::getRating).reversed())
            .limit(3)
            .toList();
    }

    /**
     * Gibt die Top 5 Authoren mit den meisten veröffentlichten Büchern aus
     * @return Liste mit den Top 5 Authoren
     */
    public List<String> getAuthorsMostBooks (){
        return books.stream()
            .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5)
            .map(Map.Entry::getKey)
            .toList();
    }

    /**
     * Sortiert alle Bücher aufsteigend nach Bewertung
     * @return Liste mit den sortierten Büchern
     */
    public List<Book> sortBooksByRating () {
        return books.stream()
            .sorted(Comparator.comparingDouble(Book::getRating))
            .toList();
    }

    /**
     * Filtert und sortiert nach von dem User angegebenen Filter und Sortier-Kriterium  
     * @param filter vom User angegebener Filter
     * @param sorter von User angegebenes Sortier-Kriterium
     * @return  
     */
    public List<Book> filterAndSortBooks (Predicate<Book> filter, Comparator<Book> sorter) {
        return books.stream()
                .filter(filter)
                .sorted(sorter)
                .collect(Collectors.toList());
    }

    /**
     * Gibt alle Bücher aus
     * @return Set mit allen Büchern
     */
    public Set<Book> getBooks () {
        return books;
    }

    /**
     * Gibt alle angelegten User aus
     * @return Map mit readerID als Key und User-Objekt als Value
     */
    public Map<String, User> getUsers () {
        return users;
    }

    /**
     * Gibt den User mit dem angegebenen readerID aus
     * @param readerID ID des Users
     * @return User mit readerID
     */
    public User findUserByID (String readerID) {
        return users.get(readerID);
    }

    /**
     * Gibt das Buch mit dem angegebenen Titel aus
     * @param title Titel, nach dem gesucht werden soll
     * @return Buch mit dem angegebenen Titel
     */
    public Book findBookByTitle (String title) {
        return books.stream()
            .filter(book -> book.getTitle().toLowerCase().equals(title))
            .findFirst()
            .orElse(null);
    }   
}
