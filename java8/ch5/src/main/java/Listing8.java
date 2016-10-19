import java.util.concurrent.*;
import java.util.stream.*;

public class Listing8 {

  public static void main(String[] args) throws Exception {
    IntStream stream;
    long startTime, endTime, sequentialSeconds, parallelSeconds;

    startTime = System.currentTimeMillis();
    stream = IntStream.range(1, Integer.MAX_VALUE);
    stream.sequential().mapToObj(i -> {
          System.out.println("Mapping " + i + " to string");
          return Integer.toString(i);
        }
    ).forEach(System.out::println);
    endTime = System.currentTimeMillis();
    sequentialSeconds = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);

    startTime = System.currentTimeMillis();
    stream = IntStream.range(1, Integer.MAX_VALUE);
    stream.sequential().mapToObj(i -> {
          System.out.println("Mapping " + i + " to string");
          return Integer.toString(i);
        }
    ).forEach(System.out::println);
    endTime = System.currentTimeMillis();
    parallelSeconds = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);

    System.out.println("Sequential time (seconds): " + sequentialSeconds);
    System.out.println("Sequential time (minutes): " + TimeUnit.SECONDS.toMinutes(sequentialSeconds));
    System.out.println("Parallel time (seconds): " + parallelSeconds);
    System.out.println("Parallel time (minutes): " + TimeUnit.SECONDS.toMinutes(parallelSeconds));
  }
}
