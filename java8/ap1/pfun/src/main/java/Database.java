import java.sql.*;
import java.util.*;
import java.util.function.*;

/**
 * Represents that database holding the text information
 */
public class Database {

  private static final String SCHEMA = "shakespeare";

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:h2:mem:shakespeare;INIT=CREATE SCHEMA IF NOT EXISTS " + SCHEMA + "\\; " +
            "SET SCHEMA " + SCHEMA + ";DB_CLOSE_DELAY=-1", "sa", ""
    );
  }

  public void initializeDb() throws SQLException {
    try (Connection c = getConnection()) {
      try (Statement stmt = c.createStatement()) {
        BiFunction<SQLException, String, SQLException> exec = (ex, s) -> {
          // If there is an exception, return it (punt)
          if (ex != null) return ex;

          // Execute this command
          try {
            stmt.execute(s);
            return null;
          } catch (SQLException e) {
            return e;
          }
        };

        // How to manage multiple exceptions
        BinaryOperator<SQLException> pickNonnull = (e1, e2) -> {
          if (e1 == null) return e2;
          if (e2 == null) return e1;
          e1.addSuppressed(e2);
          return e1;
        };

        // Execute the statements
        SQLException e = Arrays.asList(
            "DROP SCHEMA " + SCHEMA,
            "CREATE SCHEMA " + SCHEMA,
            "SET SCHEMA " + SCHEMA,
            "CREATE TABLE \"text\" (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR UNIQUE, year INT)",
            "CREATE TABLE line (id INT PRIMARY KEY AUTO_INCREMENT, text_id INT, \"offset\" INT)",
            "CREATE TABLE word (id INT PRIMARY KEY AUTO_INCREMENT, \"value\" VARCHAR UNIQUE)",
            "CREATE TABLE line_word (id INT PRIMARY KEY AUTO_INCREMENT, line_id INT, word_id INT, " +
                "\"offset\" INT)"
        ).stream().reduce(null, exec, pickNonnull);

        // If we got an exception, explode with it
        if (e != null) throw e;
      }
    }
  }

  /**
   * Inserts a database line into the database.
   *
   * @param databaseLine The line to insert; never {@code null}.
   */
  public void insertLine(final DatabaseLine databaseLine) {
    Objects.requireNonNull(databaseLine, "database line");
    try (Connection conn = getConnection()) {
      PreparedStatement createLineWord = conn.prepareStatement(
          "INSERT INTO line_word " +
              " (line_id, word_id, \"offset\") " +
              " VALUES (?,?,?)",
          Statement.NO_GENERATED_KEYS
      );

      createLineWord.setInt(1, databaseLine.getLineId());

      int[] words = databaseLine.getWords();
      for (int i = 0; i < words.length; i++) {
        createLineWord.setInt(2, words[i]);
        createLineWord.setInt(3, i + 1);
        boolean createdLineWord = createLineWord.executeUpdate() == 1;
        assert createdLineWord : "Could not create line-word";
      }
    } catch (SQLException sqle) {
      throw new RuntimeException("error while inserting database line", sqle);
    }
  }
}
