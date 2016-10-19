import java.util.*;
import java.util.function.*;
import java.util.regex.*;

/**
 * A mapper that presumes a sequential execution through the anthology of text.
 */
public class TextMapper implements java.util.function.Function<String, Optional<TextLine>> {

  private volatile String currentTitle;
  private volatile int currentYear;
  private volatile int currentOffset = 0;
  private volatile boolean inSonnets = false;

  private static final Optional<TextLine> SKIP_THIS_LINE = Optional.empty();
  private static final Predicate<String> IS_YEAR = Pattern.compile("^1(5|6)\\d\\d$").asPredicate();
  private static final Predicate<String> IS_AUTHOR = Pattern.compile("^by\\s+William\\s+Shakespeare$").asPredicate();
  private static final Predicate<String> IS_DIGITS = Pattern.compile("^\\d+$").asPredicate();
  private static final Predicate<String> IS_THE_END = Pattern.compile("^THE\\s+END$").asPredicate();
  private static final String SONNETS_TITLE = "THE SONNETS";

  /**
   * Creates a text line, or {@link java.util.Optional#empty()} if the string does not correspond to a line.
   *
   * @param s the function argument
   * @return the function result
   */
  @Override
  public Optional<TextLine> apply(final String s) {
    if (s == null || s.isEmpty()) return SKIP_THIS_LINE;

    // Skip author lines
    if (IS_AUTHOR.test(s)) return SKIP_THIS_LINE;

    // Skip THE END lines, and note that it's the end of the text
    if (IS_THE_END.test(s)) {
      currentOffset = Integer.MIN_VALUE;
      currentTitle = null;
      inSonnets = false;
      return SKIP_THIS_LINE;
    }

    // If this is the year, the next line should be the title
    if (IS_YEAR.test(s)) {
      currentYear = Integer.parseInt(s);
      currentTitle = null;
      return SKIP_THIS_LINE;
    }

    // If we are looking for the title, we just found it!
    if (currentTitle == null || (inSonnets && IS_DIGITS.test(s))) {
      if (inSonnets) {
        currentTitle = "Sonnet #" + s;
      } else {
        currentTitle = s;
        inSonnets = SONNETS_TITLE.equalsIgnoreCase(s);
      }
      currentOffset = 0;
      return SKIP_THIS_LINE;
    }

    // Just a normal line
    currentOffset += 1;
    return Optional.of(new TextLine(currentTitle, currentYear, currentOffset, s));
  }
}
