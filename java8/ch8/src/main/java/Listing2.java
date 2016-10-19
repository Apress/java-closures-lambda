import java.util.function.*;

public class Listing2 {

  public static String lambdiseMe() {
    return "Hello, World!";
  }

  public static Supplier<String> getSupplier() {
    return Listing2::lambdiseMe;
  }


}
