import java.time.LocalDate;

/**
 * Klasse zum Anlegen eines Buch-Objekts
 * @author Christian Petry, Xudong Zhang
 * @version 1.0
 */
public class Book implements Comparable<Book> {
    private String title, author, genre;
    private int year, pages;
    private double rating;
    private boolean borrowed;
    private LocalDate returnDate;

    /**
     * Konstruktor zum Anlegen eines neuen Buches
     * @param title Titel des Buches
     * @param author Autor des Buches
     * @param year Veröffsungsjahr
     * @param pages Anzahl der Seiten
     * @param genre Genre 
     * @param rating Bewertung
     */
    public Book (String title, String author, int year, int pages, String genre, double rating) {
        setTitle(title);
        setAuthor(author);
        setYear(year);
        setPages(pages);
        setGenre(genre);
        setRating(rating);
        setBorrowedStatus(false);
        setReturnDate(null);
    }

    /**
     * Validiert den Titel des Buches
     * @param title Titel
     * @throws IllegalArgumentException wenn Titel leer oder null ist
     */
    private static void validateTitle (String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Der Titel darf nicht leer sein.");
        }
    }

    /**
     * Validiert den Autor des Buches
     * @param author Autor
     * @throws IllegalArgumentException wenn Autor leer oder null ist
     */
    private static void validateAuthor (String author) {
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Der Autor darf nicht leer sein.");
        }
    }

    /**
     * Validiert das Jahr
     * @param year Jahr
     * @throws IllegalArgumentException wenn Jahr vor 1900 liegt
     */
    private static void validateYear (int year) {
        if (year < 1500) {
            throw new IllegalArgumentException("Das Veröffentlichungsjahr muss nach 1500 liegen");
        }
    }

    /**
     * Validiert die Anzahl der Seiten
     * @param pages Anzahl der Seiten
     * @throws IllegalArgumentException wenn Anzahl der Seiten negativ ist
     */
    private static void validatePages (int pages) {
        if (pages < 0) {
            throw new IllegalArgumentException("Die Anzahl der Seiten darf nicht negativ sein");
        }
    }

    /**
     * Validiert das Genre
     * @param genre Genre
     * @throws IllegalArgumentException wenn Genre leer oder null ist
     */
    private static void validateGenre (String genre) {
        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Das Genre darf nicht leer sein");
        }
    }

    /**
     * Validiert die Bewertung
     * @param rating Bewertung
     * @throws IllegalArgumentException wenn Bewertung nicht zwischen 1.0 und 5.0 liegt
     */
    private static void validateRating (double rating) {
        if (rating < 1.0d || rating > 5.0d) {
            throw new IllegalArgumentException("Die Bewertung muss zwischen 1.0 und 5.0 liegen");
        }
    }

    /**
     * Setzt den Titel
     * Nutzt validateTitle zur Überprüfung
     * @param title Titel
     */
    public void setTitle (String title) {
        validateTitle(title);
        this.title = title;
    }

    /**
     * Setzt den Autor
     * Nutzt validateAuthor zur Überprüfung
     * @param author Autor
     */
    public void setAuthor (String author) {
        validateAuthor(author);
        this.author = author;
    }

    /**
     * Setzt das Jahr
     * Nutzt validateYear zur Überprüfung
     * @param year Jahr
     */
    public void setYear (int year) {
        validateYear(year);
        this.year = year;
    }

    /**
     * Setzt die Anzahl der Seiten
     * Nutzt validatePages zurprüfung
     * @param pages Anzahl der Seiten
     */
    public void setPages (int pages) {
        validatePages(pages);
        this.pages = pages;
    }

    /**
     * Setzt das Genre
     * Nutzt validateGenre zurprüfung
     * @param genre Genre
     */
    public void setGenre (String genre) {
        validateGenre(genre);
        this.genre = genre;
    }

    /**
     * Setzt die Bewertung
     * Nutzt validateRating zurprüfung
     * @param rating Bewertung
     */
    public void setRating (double rating) {
        validateRating(rating);
        this.rating = rating;
    }

    /**
     * Setzt den Status, ob der Buch ausgeliehen ist
     * Falls das Buch ausgeliehen ist, wird das Ausleihdatum gesetzt
     * @param borrowed
     */
    public void setBorrowedStatus (boolean borrowed) {
        this.borrowed = borrowed;
        if (borrowed) {
            setReturnDate(LocalDate.now().plusDays(14));
        }
    }

    /**
     * Setzt das Ausleihdatum
     * Dieses wird jeweils auf das aktuelle Datum + 14 Tage gesetzt
     * @param returnDate
     */
    public void setReturnDate (LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Gibt den Titel des Buches aus
     * @return Titel
     */
    public String getTitle () {
        return title;
    }

    /**
     * Gibt den Autor des Buches aus
     * @return Autor
     */
    public String getAuthor () {
        return author;
    }

    /**
     * Gibt das Veröffentlichungsjahr des Buches aus
     * @return Veröffentlichungsjahr
     */
    public int getYear () {
        return year;
    }

    /**
     * Gibt die Anzahl der Seiten des Buches aus
     * @return Anzahl der Seiten
     */
    public int getPages () {
        return pages;
    }

    /**
     * Gibt das Genre des Buches aus
     * @return Genre
     */
    public String getGenre () {
        return genre;
    }

    /**
     * Gibt die Bewertung des Buches aus
     * @return Bewertung
     */
    public double getRating () {
        return rating;
    }

    /**
     * Gibt den Ausleihstatus des Buches aus
     * @return true, falls der Buch ausgeliehen ist
     */
    public boolean isBorrowed () {
        return borrowed;
    }

    /**
     * Gibt das Ausleihdatum des Buches in lokaler Zeit aus
     * @return Ausleihdatum
     */
    public LocalDate getReturnDate () {
        return returnDate;
    }

    /**
     * Vergleicht zwei Buch-Objekte anhand des Titels
     * @return 0 falls Titel gleich, -1 falls Titel des ersten Buches lexikographisch vor zweitem, 1 falls Titel des ersten Buches lexikographisch nach zweitem
     */
    @Override
    public int compareTo (Book other) {
        return this.getTitle().compareTo(other.getTitle());
    }

    /**
     * Gibt das Objekt als String aus
     * Falls das Buch ausgeliehen ist, wird der Status und das Ausleihdatum angezeigt
     * @return String mit allen Attributen
     */
    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("Titel: " + getTitle() + ", Autor: " + getAuthor() + "\nVeröffentlichungsjahr: " + getYear() + ", Anzahl der Seiten: " + getPages() + "\nGenre: " + getGenre() + ", Bewertung: " + getRating());
        if (isBorrowed()) {
            sb.append("\nBuch ist aktuell ausgeliehen. \nRückgabedatum: " + getReturnDate());
        }
        return sb.toString();
    }
}