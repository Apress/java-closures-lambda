import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * Source for a Shakespeare Text
 */
public class ShakespeareTextSource {

  private static final String SOURCE = "shakespeare.txt";

  /**
   * Provides a reader of the in-memory anthology. No filtering is performed on this reader.
   *
   * @return A reader of the in-memory anthology; never {@code null}
   * @throws IOException If there is an exception retrieving the in-memory anthology
   */
  public BufferedReader getReader() throws IOException {
    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(SOURCE);
    return new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
  }

  private static UnaryOperator<String> filterBadChars() {
    Pattern BAD_CHARS = Pattern.compile("\uFEFF|\\p{Cntrl}"); // BOMs and control characters
    return string -> BAD_CHARS.matcher(string).replaceAll("");
  }

  private static Predicate<String> notInComment() {
    final Predicate<String> IS_COMMENT_START = Pattern.compile("^<<").asPredicate();
    final Predicate<String> IS_COMMENT_END = Pattern.compile(">>$").asPredicate();

    final AtomicBoolean inComment = new AtomicBoolean(false);
    return line -> {
      if (IS_COMMENT_START.test(line)) {
        inComment.set(true);
        return false;
      } else if (inComment.get() && IS_COMMENT_END.test(line)) {
        inComment.set(false);
        return false;
      } else {
        return !inComment.get();
      }
    };
  }

  private static Predicate<String> notEmptyOrWhitespace() {
    return Pattern.compile("^\\s*$").asPredicate().negate();
  }

  /**
   * Provides a sequential stream of the text source. The stream is filtered of bad characters, whitespace lines,
   * and comments. Each line is also trimmed.
   *
   * @return A stream, which must be processed sequentially
   * @throws IOException If there is an exception getting the resource
   */
  public Stream<String> getStream() throws IOException {
    return getReader().lines()
        .sequential()
        .map(filterBadChars())
        .map(String::trim)
        .filter(notEmptyOrWhitespace())
        .filter(notInComment())
        ;

  }

}
