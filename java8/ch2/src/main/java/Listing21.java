import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;

public class Listing21 {

  Consumer<String> print = System.out::println;
  UnaryOperator<String> makeGreeting = "Hello, "::concat;
  IntFunction<String> lookup = Arrays.asList("a","b","c")::get;
  IntSupplier randomInt = new Random()::nextInt;

  Path base = Paths.get(".");
  Function<String,Path> resolvePath = base::resolve;

  public IntUnaryOperator compareAgainst(Integer compareLeft) {
    return compareLeft::compareTo;
  }
}
