public class Listing6 {

  public static void main(String[] args) {
    System.out.println("\n\n\nPARALLEL");
    NumberStreamFactory.countTo(200).parallel().map(i -> {
          System.out.println("map: " + i + " Thread: " + Thread.currentThread().getName());
          return i;
        }
    ).forEach(i ->
            System.out.println("forEach: " + i + " Thread: " + Thread.currentThread().getName())
    );

    System.out.println("\n\n\nSEQUENTIAL");
    NumberStreamFactory.countTo(200).sequential().map(i -> {
          System.out.println("map: " + i + " Thread: " + Thread.currentThread().getName());
          return i;
        }
    ).forEach(i ->
            System.out.println("forEach: " + i + " Thread: " + Thread.currentThread().getName())
    );


  }
}
