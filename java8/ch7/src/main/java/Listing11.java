import java.util.stream.*;

public class Listing11 {

  public static Integer[] generateArray() {
    final Integer[] elements = new Integer[1000];
    for (int i = 0; i < elements.length; i++) {
      elements[i] = i;
    }
    return elements;
  }

  public static void main(String[] args) {
    Integer[] array = generateArray();
    Stream<Integer> stream = Stream.of(array);
    array = stream.toArray(Integer[]::new);
  }

}
