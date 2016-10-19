import java.util.*;

/**
 * Represents a word positioned within a line of Shakespeare.
 */
public class ShakespeareWord {

  private final int linePosition;
  private final String word;

  public ShakespeareWord(final int linePosition, final String word) {
    if (linePosition < 1) {
      throw new IllegalArgumentException("Bad line position: " + linePosition);
    }
    this.linePosition = linePosition;
    Objects.requireNonNull(word, "word");
    this.word = word;
  }

  public int getLinePosition() {
    return linePosition;
  }

  public String getWord() {
    return word;
  }
}
