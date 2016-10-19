import java.util.*;

public class Listing8Library extends Listing7Library {

  public Map<Book.Genre, Integer> getGenreCounts(Book.Genre... genres) {
    Map<Book.Genre, Integer> toReturn = new HashMap<>();
    Arrays.asList(genres).forEach(genre -> toReturn.put(genre, 0));
    getBooks().forEach(book ->
      toReturn.computeIfPresent(book.getGenre(), (key, count) -> count + 1 )
    );
    return toReturn;
  }


}
