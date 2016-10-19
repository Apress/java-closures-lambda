import java.util.function.*;

public class Listing15 {

  UnaryOperator<String> upperCase = str -> str.toUpperCase();
  BinaryOperator<String> concat = (left,right) -> left + right;

}
