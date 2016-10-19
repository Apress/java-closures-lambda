import java.util.concurrent.*;

public class Listing5 {

  public static void printPrimes(int maxBitLength) {
    NumberStreamFactory.countTo(maxBitLength)
        .map(i ->
                ForkJoinTask.adapt(() -> PrimeFactory.ofLength(i)).fork()
        )
        .map(ForkJoinTask::join)
        .forEach(System.out::println);
  }

  public static void main(String[] args) {
    DemoRunner.run(Listing5::printPrimes);
  }
}
