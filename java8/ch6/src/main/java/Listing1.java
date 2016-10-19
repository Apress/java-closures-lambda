import java.math.BigInteger;
import java.util.*;

public class Listing1 {

  public static void printPrimes(int maxBitLength) {
    Random rand = new Random();
    for (int i = 0; i < maxBitLength; i++) {
      BigInteger prime = PrimeFactory.ofLength(i + 1, rand);
      System.out.println(prime.toString());
    }
  }

  public static void main(String[] args) {
    DemoRunner.run(Listing1::printPrimes);
  }
}
