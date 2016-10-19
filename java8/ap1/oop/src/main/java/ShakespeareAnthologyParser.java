import java.io.IOException;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;

/**
 * Responsible for parsing a Shakespeare anthology into texts.
 */
public class ShakespeareAnthologyParser {

  private static final Predicate<String> IS_YEAR = Pattern.compile("^1(5|6)\\d\\d$").asPredicate();
  private static final Predicate<String> IS_AUTHOR = Pattern.compile("^by\\s+William\\s+Shakespeare$").asPredicate();
  private static final Predicate<String> IS_THE_END = Pattern.compile("^THE\\s+END$").asPredicate();
  private static final Predicate<String> IS_DIGITS = Pattern.compile("^\\d+$").asPredicate();
  private static final String SONNETS_TITLE = "THE SONNETS";

  /**
   * Parses the texts based on the in-memory Shakespeare anthology.
   *
   * @return The parsed texts
   * @throws IOException If an exception occurs in reading the texts
   */
  public Collection<ShakespeareText> parseTexts() throws IOException {
    List<ShakespeareText> texts = new ArrayList<>();
    ShakespeareLineIterator lines = new ShakespeareLineIterator();
    while (lines.hasNext()) {
      int year = parseYear(lines);
      String title = parseTitle(lines);
      parseAuthor(lines);
      if (SONNETS_TITLE.equalsIgnoreCase(title)) {
        texts.addAll(parseSonnets(lines));
      } else {
        texts.add(parseText(title, year, lines));
      }
    }
    return texts;
  }

  private ShakespeareText parseText(final String title, final int year, final ShakespeareLineIterator lines) {
    List<ShakespeareLine> parsedLines = new ArrayList<>();
    int lineNumber = 0;
    String line;
    while (lines.hasNext() && !IS_THE_END.test(line = lines.next())) {
      lineNumber += 1;
      parsedLines.add(new ShakespeareLine(lineNumber, line));
    }
    return new ShakespeareText(title, year, parsedLines);
  }

  private Collection<ShakespeareSonnet> parseSonnets(final ShakespeareLineIterator lines) {
    List<ShakespeareSonnet> sonnets = new ArrayList<>();
    while (lines.hasNext() && !IS_THE_END.test(lines.peek())) {
      sonnets.add(parseSonnet(lines));
    }
    if (lines.hasNext()) lines.next(); // Move past THE END
    return sonnets;
  }

  private ShakespeareSonnet parseSonnet(final ShakespeareLineIterator lines) {
    List<ShakespeareLine> parsedLines = new ArrayList<>();
    int number = parseSonnetNumber(lines);
    int lineNumber = 0;
    while (lines.hasNext() && !IS_DIGITS.test(lines.peek()) && !IS_THE_END.test(lines.peek())) {
      lineNumber += 1;
      parsedLines.add(new ShakespeareLine(lineNumber, lines.next()));
    }
    return new ShakespeareSonnet(number, parsedLines);
  }

  private static int parseSonnetNumber(ShakespeareLineIterator lines) {
    String number = lines.next();
    boolean isNumber = IS_DIGITS.test(number);
    assert isNumber : "Expected a sonnet number, but got: " + number;
    return Integer.parseInt(number);
  }

  private static String parseTitle(ShakespeareLineIterator lines) {
    String title = lines.next();
    boolean isTitle = title != null && !title.isEmpty();
    isTitle = isTitle && !IS_AUTHOR.test(title);
    isTitle = isTitle && !IS_THE_END.test(title);
    isTitle = isTitle && !IS_YEAR.test(title);
    assert isTitle : "Expected a title, but got: " + title;
    return title;
  }

  private static String parseAuthor(ShakespeareLineIterator lines) {
    String author = lines.next();
    boolean isAuthor = IS_AUTHOR.test(author);
    assert isAuthor : "Expected the author, but got: " + author;
    return author;
  }


  private static int parseYear(ShakespeareLineIterator lines) {
    String year = lines.next();
    boolean isYear = IS_YEAR.test(year);
    assert isYear : "Expected the year, but got: " + year;
    return Integer.parseInt(year);
  }

}
