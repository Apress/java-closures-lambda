import java.util.*;

public class ComparatorExample {

  class User {
    public String getFirstName() { return null; }
    public String getLastName() { return null; }
  }



  public static void main(String[] args) {
    Comparator c = Comparator.comparing(User::getLastName).thenComparing(User::getFirstName);
  }

}
