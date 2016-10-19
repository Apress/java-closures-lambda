import java.util.*;

/*
* Responsible for running the demonstrations we have in this chapter.
 */
public class DemoRunner {

  /**
   * The number of runs we will do per demo to get our timings. Must be odd.
   */
  public static final int RUN_COUNT = 3;

  /**
   * The maximum bitlength of primes to generate.
   */
  public static final int MAX_BITLENGTH = 50000;

  /**
   * The interface for this chapter demos need to implement.
   */
  public interface Demo {

    /**
     * Implementation of generating primes up to a certain bitlength. The demo should generate the prime and then
     * print it to standard out.
     */
    void generatePrimesToBitLength(int bitLength);
  }

  /**
   * Runs the demo {@link #RUN_COUNT} times and then prints the median time.
   *
   * @param demo The demo to run; never {@code null}
   */
  public static void run(Demo demo) {
    Objects.requireNonNull(demo, "demo");
    long[] timings = new long[RUN_COUNT];
    for (int i = 0; i < timings.length; i++) {
      System.out.println("START ROUND " + (i+1));
      long startTime = System.currentTimeMillis();
      demo.generatePrimesToBitLength(MAX_BITLENGTH);
      long endTime = System.currentTimeMillis();
      timings[i] = endTime - startTime;
    }
    Arrays.sort(timings);
    long medianTime = timings[timings.length / 2];
    System.out.println("All timings: " + Arrays.toString(timings));
    System.out.println("Median time: " + medianTime + "ms");
  }

}
