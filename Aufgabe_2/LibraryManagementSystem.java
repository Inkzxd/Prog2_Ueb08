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

    public LibraryManagementSystem () {
        this.books = new TreeSet<>(Comparator.comparing(Book::getTitle));
        this.users = new HashMap<>();
        this.borrowedBooksByReturnDate = new TreeSet<>(Comparator.comparing(Book::getReturnDate));
    }

    public void addUser (User user) {
        users.putIfAbsent(user.getReaderID(), user);
    }

    public void addBook (Book book) {
        books.add(book);
    }

    public void borrowBook (String readerID, Book book) {
        User user = users.get(readerID);
        if (user != null) {
            user.borrowBook(book);
            borrowedBooksByReturnDate.add(book);
        }
    }

    public void returnBook (Book book) {
        borrowedBooksByReturnDate.remove(book);
        book.setBorrowedStatus(false);
    }

    public Set<Book> getBooksBorrowedByUser (String readerID) {
        User user = users.get(readerID);
        if (user == null) {
            return null;
        }
        return user.getBorrowedBooks();
    }

    public Set<Book> getBorrowedBooksByReturnDate () {
        return borrowedBooksByReturnDate;
    }   

    public List<Book> filterBooksByYear (int year) {
        List<Book> filteredBooks = books.stream()
            .filter(book -> book.getYear() > year)
            .sorted(Comparator.comparing(Book::getYear))
            .toList();
        return filteredBooks;
    }

    public List<Book> sortBooksByPages () {
        List<Book> sortedBooks = books.stream()
            .sorted(Comparator.comparing(Book::getPages))
            .toList();
        return sortedBooks;
    }

    public int getTotalPages () {
        return books.stream()
            .mapToInt(Book::getPages)
            .sum();
    }

    public List<Book> filterBooksByGenre (String genre) {
        List<Book> filteredBooks = books.stream()
            .filter(book -> book.getGenre().equals(genre))
            .toList();
        return filteredBooks;
    }

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

    public List<Book> getTopRatedBooks () {
        return books.stream()
            .sorted(Comparator.comparingDouble(Book::getRating).reversed())
            .limit(3)
            .toList();
    }

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

    public List<Book> sortBooksByRating () {
        return books.stream()
            .sorted(Comparator.comparingDouble(Book::getRating).reversed())
            .toList();
    }

    public List<Book> filterAndSortBooks (Predicate<Book> filter, Comparator<Book> sorter) {
        return books.stream()
                .filter(filter)
                .sorted(sorter)
                .collect(Collectors.toList());
    }

    public Set<Book> getBooks () {
        return books;
    }

    public List<User> getUsers () {
        return users.values()
            .stream()
            .toList();
    }

    public User findUserByID (String readerID) {
        return users.get(readerID);
    }

    public Book findBookByTitle (String title) {
        return books.stream()
            .filter(book -> book.getTitle().toLowerCase().equals(title))
            .findFirst()
            .orElse(null);
    }   

    public void addBorowedBook(Book book) {
        borrowedBooksByReturnDate.add(book);
    }
}
