import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

public class Listing6 {

  public static class Result<SUCCESS_T, FAILURE_T extends Exception> {

    private final SUCCESS_T success;

    private final FAILURE_T failure;

    private Result(final SUCCESS_T success, final FAILURE_T failure) {
      if (success == null && failure == null) {
        throw new IllegalArgumentException("Success and failure cannot both be null");
      }
      if (success != null && failure != null) {
        throw new IllegalArgumentException("Success and failure cannot both be non-null");
      }
      this.success = success;
      this.failure = failure;
    }

    public static <SUCCESS_T, FAILURE_T extends Exception> Result<SUCCESS_T, FAILURE_T> ofFailure(FAILURE_T failure) {
      Objects.requireNonNull(failure, "failure to assign is null");
      return new Result(null, failure);
    }

    public static <SUCCESS_T, FAILURE_T extends Exception> Result<SUCCESS_T, FAILURE_T> ofSuccess(SUCCESS_T success) {
      Objects.requireNonNull(success, "success to assign is null");
      return new Result<>(success, null);
    }

    public boolean isSuccess() { return success != null; }

    public boolean isFailure() { return failure != null; }

    /**
     * Provides the result on successful, or throws the failure exception on failure.
     *
     * @return The success value on successful.
     * @throws FAILURE_T If this result is not successful.
     */
    public SUCCESS_T get() throws FAILURE_T {
      if (success != null) return success;
      throw failure;
    }

    /**
     * Returns the success value, or {@code null} if this result is a failure.
     *
     * @return The success value or {@code null}.
     */
    public SUCCESS_T getSuccess() {
      return success;
    }

    /**
     * Returns the failure, or {@code null} if this result is successful.
     *
     * @return The failure value or {@code null}.
     */
    public FAILURE_T getFailure() {
      return failure;
    }

    @Override
    public String toString() {
      if (isSuccess()) {
        return "SUCCESS[" + success + "]";
      } else {
        return "FAILURE[" + failure + "]";
      }
    }
  }

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

  public static Function<Integer, Result<Integer, IOException>> generateMap() {
    AtomicInteger counter = new AtomicInteger(0);
    return i -> {
      int count = counter.incrementAndGet();
      try (InputStream in = Listing3.generateInputStream()) {
        if (i == 100) {
          return Result.ofFailure(
              new IOException("And with a kiss, I die! (After " + count + " executions)")
          );
        }
        return Result.ofSuccess(i);
      } catch (IOException ioe) {
        return Result.ofFailure(ioe);
      }
    };
  }

  public static void main(String[] args) {
    // Perform the stream processing
    generateParallelStream()
        .map(generateMap())
        .forEach(result -> {
              if (result.isFailure()) {
                result.getFailure().printStackTrace(System.err);
                System.err.flush();
              } else {
                System.out.println(result.getSuccess());
                System.out.flush();
              }
            }
        );
  }

}
