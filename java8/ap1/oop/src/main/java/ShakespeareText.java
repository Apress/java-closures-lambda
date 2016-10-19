import java.util.*;

/**
 * The object representing one of Shakespeare's texts.
 */
public class ShakespeareText {

  private final String name;
  private final int year;
  private final List<ShakespeareLine> lines;

  public ShakespeareText(String name, int year, List<ShakespeareLine> lines) {
    Objects.requireNonNull(name, "name of the text");
    this.name = name;

    if (year < 1500 || year > 1699) {
      throw new IllegalArgumentException("Egregiously wrong year for " + name + ": " + year);
    }
    this.year = year;

    Objects.requireNonNull(lines, "lines");
    if (lines.isEmpty()) throw new IllegalArgumentException("Provided empty lines for " + name);
    this.lines = lines;
  }

  public String getName() {
    return name;
  }

  public int getYear() {
    return year;
  }

  public List<ShakespeareLine> getLines() {
    return lines;
  }
}
