import java.util.stream.*;

public class WordUsageToTSVDemo {


  public static void main(String[] args) {
    // Using toTSV method
    Stream<WordUsage> source = Stream.of(new WordUsage("foo", 1, "bar", 2));
    Stream<String> output = source.map(WordUsage::toTSV);
    output.forEach(System.out::println);

    // Using inline lambda
    source = Stream.of(new WordUsage("foo", 1, "bar", 2));
    source.map(wordUsage ->
      String.format("%s\t%d\t%s\t%d",
          wordUsage.getTextName(), wordUsage.getLineOffset(),
          wordUsage.getWord(),  wordUsage.getWordOffset()
      )
    );
  }


}
