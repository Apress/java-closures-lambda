import funjava.util.function.FunBiFunction;
import funjava.util.function.FunFunction;

import java.util.*;
import java.util.function.*;

public class Listing11 {

  public static void main(String[] args) {
    FunBiFunction<String, String, String> concat = (a, b) -> a + b;
    FunFunction<String, FunFunction<String, String>> curriedConcat = concat.curry();
    for (String greetings : Arrays.asList("Hello", "Guten Tag", "Bonjour")) {
      greetFolks(curriedConcat.apply(greetings + ", "));
    }
  }

  public static void greetFolks(Function<String, String> greeter) {
    for (String name : Arrays.asList("Alice", "Bob", "Cathy")) {
      System.out.println(greeter.apply(name));
    }
  }

}

