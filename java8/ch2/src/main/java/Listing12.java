import java.util.*;
import java.util.function.*;

public class Listing12 {

  public static void main(String[] args) {
    greetFolks();
  }

  public static void greetFolks() {
    Consumer<String> doGreet = name -> System.out.println("Hello, " + name);
    for (String name : Arrays.asList("Alice", "Bob", "Cathy")) {
      doGreet.accept(name);
    }
  }

  public static void concat() {
    BiConsumer<String,String> printConcat = (left,right) -> System.out.println(left + right);
    for (String name : Arrays.asList("Alice", "Bob", "Cathy")) {
      printConcat.accept("Goodbye, ", name);
    }
  }

}

