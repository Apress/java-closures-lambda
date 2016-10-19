import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

public class Listing3 {

  public static void printPrimes(int maxBitLength) {
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    ExecutorService printExecutor = Executors.newSingleThreadExecutor(r -> {
          Thread t = new Thread(r);
          t.setPriority(Thread.MAX_PRIORITY);
          return t;
        }
    );

    // The resulting work
    List<Future<?>> futures = new ArrayList<>(maxBitLength);

    for (int i = 0; i < maxBitLength; i++) {
      int bitLength = i + 1;
      Future<String> stringFuture = executor.submit(() -> {
            BigInteger prime = PrimeFactory.ofLength(bitLength);
            return prime.toString();
          }
      );
      futures.add(printExecutor.submit(() -> {
                String primeString = stringFuture.get();
                System.out.println(primeString);
                return null;
              }
          )
      );
    }

    // Signal that there will be no more tasks added
    executor.shutdown();
    printExecutor.shutdown();

    // Wait for everything to complete and check for errors
    futures.parallelStream().forEach(future -> {
          try {
            future.get();
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
          }
        }
    );
  }

  public static void main(String[] args) {
    DemoRunner.run(Listing3::printPrimes);
  }
}
