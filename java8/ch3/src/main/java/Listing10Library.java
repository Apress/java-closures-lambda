import java.util.*;

public class Listing10Library extends Listing9Library {

  public Map<Book.Genre, Integer> getGenreCounts() {
    Map<Book.Genre, Integer> counts = new HashMap<>();
    getBooks().forEach(book ->
     counts.merge(book.getGenre(), 1, (i, j) -> i + j)
    );
    return counts;
  }

}
