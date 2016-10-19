import java.util.*;

/**
 * Class representing an element of a {@link Library}.
 */
public class Book {

  /**
   * Represents the allowed genres of a book.
   */
  public static enum Genre {
    HORROR, COMEDY, TECHNICAL;
  }

  private final String title;
  private final String author;
  private final Genre genre;

  /**
   * Constructor.
   *
   * @param title  The title of the book; may not be {@code null}.
   * @param author The author of the book; may not be {@code null}.
   * @param genre  The genre of the book; may not be {@code null}.
   */
  public Book(final String title, final String author, final Genre genre) {
    Objects.requireNonNull(title, "title of the book");
    this.title = title;
    Objects.requireNonNull(author, "author of the book");
    this.author = author;
    Objects.requireNonNull(genre, "genre of the book");
    this.genre = genre;
  }

  /**
   * Proivdes the title of the book.
   *
   * @return The title; never {@code null}
   */
  public String getTitle() {
    return title;
  }

  /**
   * Provides the author of the book.
   *
   * @return The author; never {@code null}
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Provides the genre of the book.
   *
   * @return The genre; never {@code null}
   */
  public Genre getGenre() {
    return genre;
  }

  @Override
  public String toString() {
    return "\"" + title + "\" by " + author + " (" + genre.toString().toLowerCase() + ")";
  }

  @Override
  public int hashCode() {
    int result = title.hashCode();
    result = 17 * result + author.hashCode();
    result = 31 * result + genre.hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == null) return false;
    if (this == o) return true;
    if (!(o instanceof Book)) return false;

    final Book book = (Book) o;

    if (!author.equals(book.author)) return false;
    if (genre != book.genre) return false;
    if (!title.equals(book.title)) return false;

    return true;
  }

}
