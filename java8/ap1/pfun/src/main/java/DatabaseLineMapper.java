import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * Responsible for mapping text lines into database lines.
 */
public class DatabaseLineMapper implements Function<TextLine, DatabaseLine> {

  private final ConcurrentMap<String, Integer> textIds = new ConcurrentHashMap<>();
  private final ConcurrentMap<Integer, ConcurrentMap<Integer, Integer>> textLineIds = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, Integer> wordIds = new ConcurrentHashMap<>();
  private final Database database;

  public DatabaseLineMapper(Database database) throws SQLException {
    Objects.requireNonNull(database, "database");
    this.database = database;
  }

  private int computeTextId(String title, int year) {
    try (Connection conn = database.getConnection()) {
      PreparedStatement createBook = conn.prepareStatement("INSERT INTO \"text\" (name, year) VALUES (?,?)",
          Statement.RETURN_GENERATED_KEYS
      );
      createBook.setString(1, title);
      createBook.setInt(2, year);
      boolean createdBook = createBook.executeUpdate() == 1;
      assert createdBook : "Could not create book";
      try (ResultSet rs = createBook.getGeneratedKeys()) {
        boolean hasNext = rs.next();
        assert hasNext : "No result when getting generated keys for " + title;
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new RuntimeException("error while computing text id for: " + title, e);
    }
  }

  private int computeLineId(final int textId, final int lineNumber) {
    try (Connection conn = database.getConnection()) {
      PreparedStatement createLine = conn.prepareStatement("INSERT INTO line (text_id, \"offset\") VALUES (?,?)",
          Statement.RETURN_GENERATED_KEYS
      );
      createLine.setInt(1, textId);
      createLine.setInt(2, lineNumber);
      boolean createdLine = createLine.executeUpdate() == 1;
      assert createdLine : "Could not create line";
      try (ResultSet rs = createLine.getGeneratedKeys()) {
        boolean hasNext = rs.next();
        assert hasNext : "No result when getting generated keys for for line " + textId + "-" + lineNumber;
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new RuntimeException("error while computing line id for: " + textId + " - " + lineNumber, e);
    }
  }

  private int computeWordId(final String word) {
    try (Connection conn = database.getConnection()) {
      PreparedStatement createWord = conn.prepareStatement("INSERT INTO word (\"value\") VALUES (?)",
          Statement.RETURN_GENERATED_KEYS
      );
      createWord.setString(1, word);
      boolean createdWord = createWord.executeUpdate() == 1;
      assert createdWord : "Could not create word: " + word;
      try (ResultSet rs = createWord.getGeneratedKeys()) {
        boolean hasNext = rs.next();
        assert hasNext : "Created word but still could not find it!";
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new RuntimeException("error while computing word id for: " + word, e);
    }
  }

  /**
   * Applies this function to the given argument.
   *
   * @param textLine the function argument
   * @return the function result
   */
  @Override
  public DatabaseLine apply(final TextLine textLine) {
    // Get the text id
    int textId = lookupTextId(textLine);

    // Get the line id
    int lineId = lookupLineId(textId, textLine);

    // Get the word ids
    int[] words = textLine.getWords().parallelStream()
        .mapToInt(this::lookupWord)
        .toArray();

    return new DatabaseLine(lineId, words);
  }

  private int lookupWord(final String word) {
    return wordIds.computeIfAbsent(word, this::computeWordId);
  }


  private int lookupLineId(int textId, final TextLine textLine) {
    ConcurrentMap<Integer, Integer> lineIds = textLineIds.computeIfAbsent(textId, i -> new ConcurrentHashMap());
    return lineIds.computeIfAbsent(textLine.getLineNumber(), i -> this.computeLineId(textId, i));
  }

  private int lookupTextId(final TextLine textLine) {
    return textIds.computeIfAbsent(textLine.getTitle(), s -> this.computeTextId(s, textLine.getYear()));
  }

}
