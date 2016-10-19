import java.util.*;
import java.util.concurrent.atomic.*;

public class Listing12 {

  public interface LambdaIterator<E> extends Iterator<E> {

    Optional<E> maybeNext(boolean advance);

    default E next() {
      return maybeNext(true).orElseThrow(NoSuchElementException::new);
    }

    default boolean hasNext() {
      return maybeNext(false).isPresent();
    }
  }

  public static Iterator<String> generateIterator() {
    String[] values = new String[]{"Hello", " ", "World"};
    AtomicInteger nextI = new AtomicInteger(0);
    LambdaIterator<String> it = advance -> {
      if (nextI.get() >= values.length) return Optional.empty();
      String result = values[nextI.get()];
      if (advance) nextI.incrementAndGet();
      return Optional.of(result);
    };
    return it;
  }


  public static void main(String[] args) {
    Iterator<String> it = generateIterator();
  }

}
