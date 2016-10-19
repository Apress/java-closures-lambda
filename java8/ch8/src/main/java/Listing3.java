import java.util.function.*;

public class Listing3 {

  public Consumer<String> getConsumer() {
    return System.out::println;
  }

}
