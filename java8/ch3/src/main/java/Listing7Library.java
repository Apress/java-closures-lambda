import java.util.*;

public class Listing7Library extends Listing6Library {

  /**
   * Provides a featured book message for the given book. If the book was not previously a featured book, it will be
   * after this method resolves. If it did not previously have a featured book message, it will have one after this
   * method resolves.
   *
   * @param featuredBook The book whose message is desired; never {@code null}
   * @return The message for the featured book; never {@code null}
   */
  public String getFeaturedBookMessage(Book featuredBook) {
    Objects.requireNonNull(featuredBook, "featured book");
    return this.getFeaturedBooks().computeIfAbsent(featuredBook, book ->
      "Featured " + book.getGenre().toString().toLowerCase() + " book by " + book.getAuthor()
    );
  }


}
