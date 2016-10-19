import java.util.function.*;

public class Listing18 {

  public static void main(String[] args) {
    // This will throw an exception
    //methodBeingCalled((i -> Integer.toString(i));

    // This will not
    methodBeingCalled((int i) -> Integer.toString(i));

    IntFunction<String> function = Integer::toString;
  }

  static void methodBeingCalled(Function<Integer, String> function) {}
  static void methodBeingCalled(IntFunction<String> function) {}

}
