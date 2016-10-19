import java.util.function.*;

public class Listing6 {

  public static Supplier<String> getSupplier() {
    return () -> "Hello, World!";
  }

  public static void main(String args[]) {
    System.out.println(getSupplier().getClass());
    System.out.println(getSupplier().getClass());
    System.out.println(getSupplier().getClass());
  }

}
