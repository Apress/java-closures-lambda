import java.math.BigInteger;
import java.util.function.*;

public class Listing4 {

  public static Function<BigInteger, String> getFunction() {
    return BigInteger::toString;
  }


}
