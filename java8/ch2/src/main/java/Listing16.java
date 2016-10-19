import java.util.function.*;

// Predicates
public class Listing16 {

  Predicate<String> notNullOrEmpty = s -> s != null && s.length() > 0;

}
