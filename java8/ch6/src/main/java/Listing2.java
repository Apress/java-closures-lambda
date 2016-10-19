import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class Listing2 {

  private static void joinThread(Thread t) {
    try {
      t.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void printPrimes(int maxBitLength) {
    Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
          System.err.println("Exception in " + t + ": " + e);
        }
    );

    int processorCount = Runtime.getRuntime().availableProcessors();

    BlockingQueue<String> primes = new LinkedBlockingQueue<String>();

    Thread[] threads = new Thread[processorCount];
    for (int t = 0; t < threads.length; t++) {
      int modulus = t;
      threads[modulus] = new Thread(() -> {
        for (int i = 0; i < maxBitLength; i++) {
          int bitLength = i + 1;
          if (bitLength % processorCount == modulus) {
            BigInteger prime = PrimeFactory.ofLength(bitLength);
            primes.add(prime.toString());
          }
        }
      }
      );
      threads[modulus].start();
    }

    AtomicBoolean doneSignal = new AtomicBoolean(false);
    Thread printer = new Thread(() -> {
      List<String> myPrimes = new ArrayList<>(threads.length);
      for (Thread.yield(); !doneSignal.get(); Thread.yield()) {
        primes.drainTo(myPrimes);
        if (myPrimes.isEmpty()) {
          // Take an extra breather
          Thread.yield();
        } else {
          System.out.println(String.join("\n", myPrimes));
          myPrimes.clear();
        }
      }
    }
    );
    printer.start();

    for (int t = 0; t < threads.length; t++) {
      joinThread(threads[t]);
    }
    doneSignal.set(true);
    joinThread(printer);
  }

  public static void main(String[] args) {
    DemoRunner.run(Listing2::printPrimes);
  }
}
