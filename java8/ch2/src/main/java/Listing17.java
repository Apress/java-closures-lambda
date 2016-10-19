import java.util.*;
import java.util.function.*;

public class Listing17 {

  IntFunction<String> intToString = i -> Integer.toString(i);
  ToIntFunction<String> parseInt = str -> Integer.valueOf(str);
  IntPredicate isEven = i -> i % 2 == 0;
  ToIntBiFunction<String,String> maxLength = (left,right) -> Math.max(left.length(), right.length());
  IntConsumer printInt = i -> System.out.println(Integer.toString(i));
  ObjIntConsumer<String> printParsedIntWithRadix = (str,radix) -> System.out.println(Integer.parseInt(str,radix));
  IntSupplier randomInt = () -> new Random().nextInt();
  IntUnaryOperator negateInt = i -> -1 * i;
  IntBinaryOperator multiplyInt = (x,y) -> x*y;
  IntToDoubleFunction intAsDouble = i -> Integer.valueOf(i).doubleValue();
  DoubleToIntFunction doubleAsInt = d -> Double.valueOf(d).intValue();
  IntToLongFunction intAsLong = i -> Integer.valueOf(i).longValue();
  LongToIntFunction longAsInt = x -> Long.valueOf(x).intValue();



}
