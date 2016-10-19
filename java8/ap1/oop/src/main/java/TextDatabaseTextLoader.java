import java.sql.*;
import java.util.*;

/**
 * Responsible for loading a text.
 */
public class TextDatabaseTextLoader implements AutoCloseable {

  private final PreparedStatement createBook;
  private final TextDatabaseLineLoader lineLoader;

  public TextDatabaseTextLoader(Connection conn) throws SQLException {
    Objects.requireNonNull(conn, "connection for loading texts");
    createBook = conn.prepareStatement("INSERT INTO \"text\" (name, year) VALUES (?,?)",
        Statement.RETURN_GENERATED_KEYS
    );
    lineLoader = new TextDatabaseLineLoader(conn);
  }

  /**
   * Creates the tables used for populating the text.
   *
   * @param conn The connection to use; never {@code null}
   */
  public static void createTables(final Connection conn) throws SQLException {
    Objects.requireNonNull(conn, "connection");
    try (Statement stmt = conn.createStatement()) {
      stmt.execute("CREATE TABLE \"text\" (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR UNIQUE, year INT)");
    }
    TextDatabaseLineLoader.createTables(conn);
  }

  public void insertText(ShakespeareText text) throws SQLException {
    int textId = insertTextRecord(text.getName(), text.getYear());
    for (ShakespeareLine line : text.getLines()) {
      lineLoader.insertLine(textId, line);
    }
  }

  private int insertTextRecord(String title, int year) throws SQLException {
    createBook.setString(1, title);
    createBook.setInt(2, year);
    boolean createdBook = createBook.executeUpdate() == 1;
    assert createdBook : "Could not create book";
    try (ResultSet rs = createBook.getGeneratedKeys()) {
      boolean hasNext = rs.next();
      assert hasNext : "No result when getting generated keys for " + title;
      return rs.getInt(1);
    }
  }

  public void close() throws SQLException {
    createBook.close();
    lineLoader.close();
  }

}
