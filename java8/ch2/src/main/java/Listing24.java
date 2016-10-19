import java.io.File;
import java.util.*;

public class Listing24 {

  public static void main(String[] args) {

    Comparator<File> fileComparator =
        Comparator.nullsLast(
          Comparator.comparing(File::getName)
        );
  }

}
