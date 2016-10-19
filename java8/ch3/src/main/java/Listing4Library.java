import java.util.*;
import java.util.function.*;

public class Listing4Library extends Library {

  /**
   * Maps an element to a value and then tests that mapped value.
   *
   * @param map The function that defines the map; never {@code null}
   * @param test The function that defines the test; never {@code null}
   * @return A predicate that tests the result of the mapping of its argument.
   */
  public static <T,U> Predicate<T> mapThenTest(Function<T,U> map, Predicate<U> test) {
    Objects.requireNonNull(map, "the map implementation");
    Objects.requireNonNull(test, "the test implementation");
    return t -> test.test(map.apply(t));
  }

  /**
   * Removes all books of the given genre from both the library collection and the featured collection.
   *
   * @param genre The genre to compare against; never {@code null}.
   */
  public void removeGenre(Book.Genre genre) {
    Objects.requireNonNull(genre, "the genre to compare against");
    Predicate<Book> hasGenre = mapThenTest(Book::getGenre, genre::equals);
    if (this.getBooks().removeIf(hasGenre)) {
      this.getFeaturedBooks().keySet().removeIf(hasGenre);
    }
  }

}
