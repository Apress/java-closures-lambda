import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class Listing25 {

  public static void main(String[] args) throws Exception {

    List<Number> numbers = Arrays.asList(1, 2, 3);
    numbers.forEach(i -> System.out.println(i));
    System.out.println("Done!");

    Files.lines(Paths.get("build.gradle")).forEach(s -> System.out.println(s));
    System.out.println("Done!");

  }

  class SomeModel {}

  class ResultsHandler implements Consumer<SomeModel> {
    @Override
    public void accept(final SomeModel o) {
      return;
    }
  }

  ForkJoinPool threadPool = null;

  Map<String, SomeModel> cache = null;

  void readData(String key, ResultsHandler handler) {
    SomeModel cachedModel = cache.get(key);
    if (cachedModel != null) {
      handler.accept(cachedModel);
    } else {
      threadPool.submit(() -> {
            SomeModel resultingModel = null;
          /* Do expensive query setting "resultingModel"... */
            cache.put(key, resultingModel);
            handler.accept(resultingModel);
          }
      );
    }

    String str = null;
    final Function<String,Integer> converter;
    if(str == null || str.isEmpty()) {
      converter = s -> 0;
    } else if(str.matches("\\d+")) {
      converter = Integer::parseInt;
    } else {
      Function<String,String> eliminateDigits = s -> {
        return s.replaceAll("[^\\d]", "");
      };
      Function<String,String> defaultString = s -> {
        if(s.isEmpty()) {
          return "0";
        } else {
          return s;
        }
      };
      converter = eliminateDigits.andThen(defaultString).andThen(Integer::parseInt);
    }
  }

}
