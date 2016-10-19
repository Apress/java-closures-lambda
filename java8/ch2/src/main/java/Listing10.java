import funjava.util.function.FunBiFunction;

import java.util.*;
import java.util.function.*;

public class Listing10 {

  public static void main(String[] args) {
    FunBiFunction<String, String, String> concat = (a, b) -> a + b;
    greetFolks(concat.applyPartial("Hello, "));
  }

  public static void greetFolks(Function<String, String> greeter) {
    for (String name : Arrays.asList("Alice", "Bob", "Cathy")) {
      System.out.println(greeter.apply(name));
    }
  }

}

