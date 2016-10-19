import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

public class PrimeFactory {

  /**
   * Returns a number that has bit length {@code bitLength} and is probably prime. The odds of the value being
   * prime are the same as {@link BigInteger#probablePrime(int, java.util.Random)}.
   *
   * @param bitLength The bit length of the resulting prime; must be
   * @return A random {@link java.math.BigInteger} of bit length {@code bitLength} which is probably prime.
   * @see java.math.BigInteger#probablePrime(int, java.util.Random)
   */
  public static BigInteger ofLength(int bitLength) {
    return ofLength(bitLength, ThreadLocalRandom.current());
  }

  /**
   * Returns a number that has bit length {@code bitLength} and is probably prime generated using {@code rand}.
   * The odds of the value being prime are the same as {@link BigInteger#probablePrime(int, java.util.Random)}.
   *
   * @param bitLength The bit length of the resulting prime; must be
   * @param rand      The random bit generator to use.
   * @return A random {@link java.math.BigInteger} of bit length {@code bitLength} which is probably prime.
   * @see java.math.BigInteger#probablePrime(int, java.util.Random)
   */
  public static BigInteger ofLength(int bitLength, Random rand) {
    if (bitLength <= 0) {
      throw new IllegalArgumentException("Bit length must be positive; was " + bitLength);
    }
    return new BigInteger(bitLength, ThreadLocalRandom.current());
  }

}
