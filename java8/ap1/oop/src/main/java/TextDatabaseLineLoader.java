import java.sql.*;
import java.util.*;

/**
 * Responsible for loading lines into the database.
 */
public class TextDatabaseLineLoader implements AutoCloseable {


  private final PreparedStatement createLine;
  private final TextDatabaseWordLoader wordLoader;

  public TextDatabaseLineLoader(final Connection conn) throws SQLException {
    Objects.requireNonNull(conn, "connection");
    createLine = conn.prepareStatement("INSERT INTO line (text_id, \"offset\") VALUES (?,?)",
        Statement.RETURN_GENERATED_KEYS
    );
    wordLoader = new TextDatabaseWordLoader(conn);
  }

  public static void createTables(final Connection conn) throws SQLException {
    try (Statement stmt = conn.createStatement()) {
      stmt.execute("CREATE TABLE line (id INT PRIMARY KEY AUTO_INCREMENT, text_id INT, \"offset\" INT)");
    }
    TextDatabaseWordLoader.createTables(conn);
  }

  public void insertLine(int textId, ShakespeareLine line) throws SQLException {
    int lineId = insertLineRecord(textId, line.getTextPosition());
    for (ShakespeareWord word : line.getWords()) {
      wordLoader.insertWord(lineId, word);
    }
  }

  private int insertLineRecord(final int textId, final int textPosition) throws SQLException {
    createLine.setInt(1, textId);
    createLine.setInt(2, textPosition);
    boolean createdLine = createLine.executeUpdate() == 1;
    assert createdLine : "Could not create line";
    try (ResultSet rs = createLine.getGeneratedKeys()) {
      boolean hasNext = rs.next();
      assert hasNext : "No result when getting generated keys for line in text id " + textId + ", " +
          "offset " + textPosition;
      return rs.getInt(1);
    }
  }


  public void close() throws SQLException {
    createLine.close();
    wordLoader.close();
  }
}
