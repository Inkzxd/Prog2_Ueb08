import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class User {
    
    private String name, readerID;
    private Set<Book> borrowedBooks;

    public User(String name, String readerID) {
        setName(name);
        setReaderID(readerID);
        borrowedBooks = new TreeSet<>(Comparator.comparing(Book::getReturnDate, Comparator.nullsLast(LocalDate::compareTo)));
    }

    private static void validateName (String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name darf nicht leer sein");
        }
    }

    private static void validateReaderID (String readerID) {
        if (readerID == null || readerID.isEmpty()) {
            throw new IllegalArgumentException("Die ID darf nicht leer sein");
        }
    }

    public void setName (String name) {
        validateName(name);
        this.name = name;
    }

    public void setReaderID (String readerID) {
        validateReaderID(readerID);
        this.readerID = readerID;
    }

    public String getName () {
        return name;
    }

    public String getReaderID () {
        return readerID;
    }   

    public Set<Book> getBorrowedBooks () {
        return borrowedBooks;
    }

    public void borrowBook (Book book) {
        if (book.isBorrowed()) {
            throw new IllegalArgumentException("Das Buch ist bereits ausgeliehen");
        }
        borrowedBooks.add(book);
        book.setBorrowedStatus(true);
    }

    public void returnBook (Book book) {
        borrowedBooks.remove(book);
        book.setBorrowedStatus(false);
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + getName() + ", ID: " + getReaderID());
        if (borrowedBooks.isEmpty()) {
            sb.append("\nKeine Bücher ausgeliehen");
        } else {
            sb.append("\nAusgeliehene Bücher:");
            for (Book book : borrowedBooks) {
                sb.append("\n" + book);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        User user = new User("Christian", "12345678");
        Book book = new Book("The Lord of the Rings", "J.R.R. Tolkien", 1954, 295, "Fantasy", 4.0);
        user.borrowBook(book);
        System.out.println(user);
    }
}
