import java.util.function.*;

public class Listing19 {

  IntFunction<String> intToString = Integer::toString;
  ToIntFunction<String> parseInt = Integer::valueOf;

}

