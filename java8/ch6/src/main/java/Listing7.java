import java.util.concurrent.*;

public class Listing7 {

  public static void printPrimes(int maxBitLength) {
    NumberStreamFactory.countTo(maxBitLength)
        .map(i -> ForkJoinTask.adapt(() -> PrimeFactory.ofLength(i)).fork())
        .forEach(ForkJoinTask::join);
  }

  public static void main(String[] args) {
    DemoRunner.run(Listing7::printPrimes);
  }
}
