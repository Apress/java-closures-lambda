import java.util.*;
import java.util.function.*;

public class Listing5Library extends Listing4Library {

  /**
   * Applies a mapping operation to the argument only when {@code test} returns {@code true} for that argument.
   *
   * @param test The test to determine if the mapping should be applied; never {@code null}
   * @param ifTrue The mapping to apply when the test is true; never {@code null}
   * @return A unary operator that applies {@code ifTrue} when {@code test} is true and otherwise returns the argument.
   */
  private static <T> UnaryOperator<T> applyIf(Predicate<T> test, UnaryOperator<T> ifTrue) {
    Objects.requireNonNull(test, "the predicate to test");
    Objects.requireNonNull(ifTrue, "the method to apply if true");
    return t -> {
      if(test.test(t)) {
        return ifTrue.apply(t);
      } else {
        return t;
      }
    };
  }

  /**
   * Updates the library, replacing each book that is featured with a book with the same title plus {@code *}.
   */
  public void starFeatureBooks() {
    UnaryOperator<Book> starBook = book ->
                                           new Book(book.getTitle() + "*", book.getAuthor(), book.getGenre());
    Predicate<Book> isFeatured = getFeaturedBooks().keySet()::contains;
    UnaryOperator<Book> starIfFeatured = applyIf(isFeatured, starBook);
    getBooks().replaceAll(starIfFeatured);
  }
}
