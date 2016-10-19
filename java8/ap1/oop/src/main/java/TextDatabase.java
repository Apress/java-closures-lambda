import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Represents the database for holding texts, their lines, and words.
 */
public class TextDatabase {

  private final String schema;

  /**
   * Defines a given text base which will operate on the given schema.
   *
   * @param schema The schema to operate on; may not be {@code null}.
   */
  public TextDatabase(String schema) {
    Objects.requireNonNull(schema, "schema name");
    this.schema = schema;
  }

  /**
   * Provides a new connection to the text database.
   */
  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:h2:mem:shakespeare;INIT=CREATE SCHEMA IF NOT EXISTS " + schema + "\\; " +
            "SET SCHEMA " + schema + ";DB_CLOSE_DELAY=-1", "sa", ""
    );
  }

  /**
   * Creates the database, removing the schema if it previously existed.
   */
  public void createDatabase() throws SQLException {
    try (Connection conn = getConnection()) {
      try (Statement stmt = conn.createStatement()) {
        stmt.execute("DROP SCHEMA " + schema);
        stmt.execute("CREATE SCHEMA " + schema);
        stmt.execute("SET SCHEMA " + schema);
      }
      TextDatabaseTextLoader.createTables(conn);
    }
  }

  public void insertTexts(Collection<ShakespeareText> texts) throws SQLException {
    try (Connection conn = getConnection()) {
      try (TextDatabaseTextLoader textLoader = new TextDatabaseTextLoader(conn)) {
        for (ShakespeareText text : texts) {
          textLoader.insertText(text);
        }
      }
    }
  }


}
