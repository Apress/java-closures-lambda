import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

public class Listing4 {

  public static class ResourceExceptionHandler<RESOURCE_T extends AutoCloseable> {

    public static interface FunctionWithResource<RESOURCE_T extends AutoCloseable, IN_T, OUT_T> {
      OUT_T apply(RESOURCE_T resource, IN_T value) throws Exception;
    }

    public static interface ResourceMaker<RESOURCE_T extends AutoCloseable> {
      RESOURCE_T create() throws Exception;
    }

    private final ResourceMaker<RESOURCE_T> init;
    private final ConcurrentMap<Object, Exception> exceptions = new ConcurrentSkipListMap<>();

    public ResourceExceptionHandler(final ResourceMaker<RESOURCE_T> init) {
      Objects.requireNonNull(init, "ResourceMaker (initialization code for resource)");
      this.init = init;
    }

    public Map<Object, Exception> getExceptions() {
      return exceptions;
    }

    public Function<Integer, Optional<Integer>> map(
        FunctionWithResource<RESOURCE_T, Integer, Integer> f
    ) {
      return i -> {
        try (RESOURCE_T resource = init.create()) {
          return Optional.of(f.apply(resource, i));
        } catch (Exception e) {
          exceptions.put(i, e);
          return Optional.empty();
        }
      };
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

  public static ResourceExceptionHandler.FunctionWithResource<InputStream, Integer, Integer> generateMap() {
    AtomicInteger counter = new AtomicInteger(0);
    return (in, i) -> {
      int count = counter.incrementAndGet();
      if (i == 100) {
        throw new IOException("And with a kiss, I die! (After " + count + " executions)");
      }
      return i;
    };
  }

  public static void main(String[] args) {
    // Create the handler for exceptions
    ResourceExceptionHandler handler = new ResourceExceptionHandler(Listing4::generateInputStream);

    // Perform the stream processing
    Function<Integer, Optional<Integer>> mapFunction = handler.map(generateMap());
    generateParallelStream()
        .map(mapFunction)
        .filter(Optional::isPresent).map(Optional::get)
        .forEach(System.out::println);

    // Work with the exceptions
    Map<Object, Exception> exceptions = handler.getExceptions();
    exceptions.forEach((key, val) -> {
          System.out.println(key + ": " + val);
          val.printStackTrace(System.err);
        }
    );
  }
}
