import java.util.*;

/**
 * A line of a text as understood by the database.
 */
public class DatabaseLine {
  private final int lineId;
  private final int[] words;

  public DatabaseLine(final int lineId, final int[] words) {
    this.lineId = lineId;
    Objects.requireNonNull(words, "words");
    if (words.length == 0) {
      throw new IllegalArgumentException("no words in this line!");
    }
    this.words = words;
  }

  public int getLineId() {
    return lineId;
  }

  public int[] getWords() {
    return words;
  }
}
