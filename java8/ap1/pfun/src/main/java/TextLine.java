import java.util.*;

/**
 * A method representing a line in a text
 */
public class TextLine {

  private final String title;
  private final int lineNumber;
  private final String line;
  private final int year;

  public TextLine(final String title, final int year, final int lineNumber, final String line) {
    Objects.requireNonNull(title, "title");
    this.title = title;

    if (lineNumber < 1) {
      throw new IllegalArgumentException("Line offset must be positive, but was " + lineNumber);
    }
    this.lineNumber = lineNumber;

    Objects.requireNonNull(line, "line");
    if (line.isEmpty()) {
      throw new IllegalArgumentException("line is empty for " + title + ", line number " + lineNumber);
    }
    this.line = line;

    if (year < 1500 || year > 1699) {
      throw new IllegalArgumentException("year is egregiously off: " + year);
    }
    this.year = year;
  }

  public String getTitle() {
    return title;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public String getLine() {
    return line;
  }

  public int getYear() {
    return year;
  }

  public List<String> getWords() {
    return Arrays.asList(line.split("\\s+"));
  }
}
