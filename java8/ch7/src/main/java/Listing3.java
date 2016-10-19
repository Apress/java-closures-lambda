import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

public class Listing3 {

  public static InputStream generateInputStream() {
    return new ByteArrayInputStream("foobar".getBytes());
  }

  public static Stream<Integer> generateParallelStream() {
    final int elements = 1000;
    List<Integer> toReturn = new ArrayList(elements);
    for (int i = 0; i < elements; i++) {
      toReturn.add(i);
    }
    return toReturn.parallelStream();
  }

  public static Function<Integer, Integer> generateMap() {
    AtomicInteger counter = new AtomicInteger(0);
    return i -> {
      int count = counter.incrementAndGet();
      try (InputStream in = Listing3.generateInputStream()) {
        if (i == 100) {
          throw new IOException("And with a kiss, I die! (After " + count + " executions)");
        }
        return i;
      } catch (IOException ioe) {
        throw new UncheckedIOException(
            "Error working with input stream", ioe
        );
      }
    };
  }

  public static void main(String[] args) {
    generateParallelStream().map(generateMap()).forEach(System.out::println);
  }
}
