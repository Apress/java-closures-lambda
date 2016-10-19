import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Functional-friendly class for storing results of querying the word usage database.
 */
public class WordUsage {

  private final String textName;
  private final int lineOffset;
  private final String word;
  private final int wordOffset;

  public WordUsage(final String textName, final int lineOffset, final String word, final int wordOffset) {
    this.textName = textName;
    this.lineOffset = lineOffset;
    this.word = word;
    this.wordOffset = wordOffset;
  }

  public String getTextName() {
    return textName;
  }

  public WordUsage withTextName(String textName) {
    return new WordUsage(textName, lineOffset, word, wordOffset);
  }

  public int getLineOffset() {
    return lineOffset;
  }

  public WordUsage withLineOffset(int lineOffset) {
    return new WordUsage(textName, lineOffset, word, wordOffset);
  }

  public String getWord() {
    return word;
  }

  public WordUsage withWord(String word) {
    return new WordUsage(textName, lineOffset, word, wordOffset);
  }

  public int getWordOffset() {
    return wordOffset;
  }

  public WordUsage withWordOffset(int wordOffset) {
    return new WordUsage(textName, lineOffset, word, wordOffset);
  }

  public String toTSV() {
    return String.format("%s\t%d\t%s\t%d", textName, lineOffset, word, wordOffset);
  }

  /**
   * Returns a new instance of this class based on the current row of a result set. The results within the result set
   * are expected to be as follows:
   * <p>
   * <b>1.</b> The string title of the text.<br />
   * <b>2.</b> The line offset within the text.<br />
   * <b>3.</b> The word itself.<br />
   * <b>4.</b> The word offset within the line.<br />
   *
   * @param rs The result set whose current row should be read; never {@code null}.
   * @return A {@link WordUsage} constructed from the query results.
   */
  public static WordUsage fromResultSet(ResultSet rs) throws SQLException {
    Objects.requireNonNull(rs, "result set to read from");
    return new WordUsage(
        rs.getString(1),
        rs.getInt(2),
        rs.getString(3),
        rs.getInt(4)
    );
  }

}
