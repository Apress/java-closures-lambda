import java.util.function.*;

public class Listing5 {

  public String provideMessage(String message) {
    return message;
  }

  public Supplier<String> getSupplier(String message) {
    return () -> this.provideMessage(message);
  }

}
