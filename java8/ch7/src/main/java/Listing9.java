import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.stream.*;

public class Listing9 {

  public static Stream<Integer> generateParallelStream() {
    final int elements = 1000;
    List<Integer> toReturn = new ArrayList(elements);
    for (int i = 0; i < elements; i++) {
      toReturn.add(i);
    }
    return toReturn.parallelStream();
  }

  public static void main(String[] args) {
    // Perform the stream processing
    ImmutableList.Builder<Integer> builder = ImmutableList.builder();
    generateParallelStream().forEach(builder::add);
    ImmutableList list = builder.build();
  }

}
