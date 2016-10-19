import java.util.stream.*;

public class NumberStreamFactory {

  /**
   * Returns a parallel stream which returns the {@code int} values from 1 to {@code value}.
   *
   * @param value The value to count up to; must be positive.
   * @return A stream that returns 1, 2, ..., {@code value}.
   */
  public static Stream<Integer> countTo(int value) {
    if (value <= 0) {
      throw new IllegalArgumentException("Need a positive value to count up to, but we were given " + value);
    }
    return Stream.iterate(1, i -> i + 1).limit(value).parallel();
  }

}
