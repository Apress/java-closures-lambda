import java.sql.*;
import java.util.*;

/**
 * Responsible for loading words into the database
 */
public class TextDatabaseWordLoader implements AutoCloseable {

  private final PreparedStatement createWord;
  private final PreparedStatement createLineWord;
  private final Map<String, Integer> wordIds = new HashMap<>();

  public TextDatabaseWordLoader(final Connection conn) throws SQLException {
    Objects.requireNonNull(conn, "connection");
    createWord = conn.prepareStatement("INSERT INTO word (\"value\") VALUES (?)",
        Statement.RETURN_GENERATED_KEYS
    );
    createLineWord = conn.prepareStatement(
        "INSERT INTO line_word " +
            " (line_id, word_id, \"offset\") " +
            " VALUES (?,?,?)",
        Statement.NO_GENERATED_KEYS
    );
  }

  public static void createTables(final Connection conn) throws SQLException {
    try (Statement stmt = conn.createStatement()) {
      stmt.execute("CREATE TABLE word (id INT PRIMARY KEY AUTO_INCREMENT, \"value\" VARCHAR UNIQUE)");
      stmt.execute("CREATE TABLE line_word (id INT PRIMARY KEY AUTO_INCREMENT, line_id INT, word_id INT, " +
              "\"offset\" INT)"
      );
    }
  }

  public void insertWord(final int lineId, final ShakespeareWord word) throws SQLException {
    int wordId = determineWordId(word.getWord());
    createLineWord.setInt(1, lineId);
    createLineWord.setInt(2, wordId);
    createLineWord.setInt(3, word.getLinePosition());
    boolean createdLineWord = createLineWord.executeUpdate() == 1;
    assert createdLineWord : "Could not create line-word";
  }

  private int determineWordId(String word) throws SQLException {
    if(wordIds.containsKey(word)) {
      return wordIds.get(word);
    } else {
      int wordId = insertWordRecord(word);
      wordIds.put(word, wordId);
      return wordId;
    }
  }

  private int insertWordRecord(String word) throws SQLException {
    createWord.setString(1, word);
    boolean createdWord = createWord.executeUpdate() == 1;
    assert createdWord : "Could not create word: " + word;
    try(ResultSet rs = createWord.getGeneratedKeys()) {
      boolean hasWord = rs.next();
      assert hasWord : "Created word but still could not find it!";
      return rs.getInt(1);
    }
  }

  public void close() throws SQLException {
    createWord.close();
    createLineWord.close();
  }

}
