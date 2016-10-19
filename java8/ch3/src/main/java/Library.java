import java.util.*;

/**
 * Class representing a collection of {@link Book} objects. The {@code Book} objects are stored in a user-defined
 * order, which defaults to insertion order. A given {@code Book} can also be specified by the user as <i>featured</i>,
 * optionally with a <i>featured message</i>.
 */
public class Library {

  private final List<Book> books = new ArrayList<>();
  private final Map<Book, String> featuredBooks = new HashMap<>();

  /**
   * Adds a book to the end of the books list.
   *
   * @param book The book to add; may not be {@code null}.
   */
  public void addBook(Book book) {
    Objects.requireNonNull(book, "book to add");
    this.books.add(book);
  }

  /**
   * Adds a book to the end of the books list, and adds it as a featured book with the given message.
   *
   * @param book    The book to add; may not be {@code null}.
   * @param message The featured message, which may be {@code null}.
   */
  public void addFeaturedBook(Book book, String message) {
    this.addBook(book);
    this.featuredBooks.put(book, message);
  }

  /**
   * Provides direct access to the listing of books. Modifying the returned collection will modify the books in the
   * library.
   *
   * @return The books in this library; never {@code null}.
   */
  public List<Book> getBooks() {
    return books;
  }

  /**
   * Provides direct access to the listing of featured books. Modifying the returned map will modify the featured books
   * and their messages.
   *
   * @return The featured books in this library mapped to their (possibly {@code null}) message; never {@code null}.
   */
  public Map<Book, String> getFeaturedBooks() {
    return featuredBooks;
  }

  public static Library createSampleLibrary() {
    Book[] books = new Book[]{
                                 new Book("Grails Persistence with GORM and GSQL", "Robert Fischer",
                                             Book.Genre.TECHNICAL
                                 ),
                                 new Book("The Stand", "Stephen King", Book.Genre.HORROR),
                                 new Book("IT", "Stephen King", Book.Genre.HORROR),
                                 new Book("Three Men in a Boat", "Jerome K. Jerome", Book.Genre.COMEDY),
                                 new Book("The Picture of Dorian Grey", "Oscar Wilde", Book.Genre.HORROR)
    };
    Library library = new Library();
    for (int i = 0; i < books.length; i++) {
      if (i % 2 == 0) {
        library.addFeaturedBook(books[i], "Featured Book #" + (i / 2 + 1));
      } else {
        library.addBook(books[i]);
      }
    }
    return library;
  }

}
