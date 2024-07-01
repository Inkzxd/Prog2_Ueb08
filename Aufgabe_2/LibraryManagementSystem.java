import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LibraryManagementSystem {
    private Set<Book> books;
    private Map<String, User> users;
    private Set<Book> borrowedBooksByReturnDate;

    public LibraryManagementSystem () {
        this.books = new TreeSet<>(Comparator.comparing(Book::getTitle));
        this.users = new HashMap<>();
        this.borrowedBooksByReturnDate = new TreeSet<>(Comparator.comparing(Book::getReturnDate));
    }

    public void addUser (User user) {
        users.put(user.getReaderID(), user);
    }

    public void addBook (Book book) {
        books.add(book);
    }

    public void borrowBook (String readerID, Book book) {
        User user = users.get(readerID);
        if (user != null) {
            user.borrowBook(book);
        }
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
            .toList();
        return filteredBooks;
    }

    public List<Book> sortBooksByPages () {
        List<Book> sortedBooks = books.stream()
            .sorted(Comparator.comparing(Book::getPages))
            .toList();
        return sortedBooks;
    }

    public int getNumberOfCombinedPages () {
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

    public Map<String, Double> getAverageRatingByGenre () {
        return books.stream()
            .collect(Collectors.groupingBy(Book::getGenre, Collectors.averagingDouble(Book::getRating)));
    }   

    public List<Book> getTopRatedBooks () {
        return books.stream()
            .sorted(Comparator.comparingDouble(Book::getRating).reversed())
            .limit(3)
            .toList();
    }

    public List<String> getAuthorsWithMostBooks (){
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

    public List<Book> getFilteredAndSortedBooks(Predicate<Book> filter, Comparator<Book> sorter) {
        return books.stream()
                .filter(filter)
                .sorted(sorter)
                .collect(Collectors.toList());
    }

    public List<Book> getBooks () {
        return books.stream()
            .toList();
    }

    public List<User> getUsers () {
        return users.values()
            .stream()
            .toList();
    }
}
