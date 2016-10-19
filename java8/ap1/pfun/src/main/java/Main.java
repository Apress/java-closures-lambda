import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Main {

  public static void printDatabaseSizing(Database database) throws SQLException {
    System.out.println("SIZES");
    System.out.println("-----");
    try (Connection conn = database.getConnection()) {
      Statement stmt = conn.createStatement();
      for (String table : new String[]{"\"text\"", "line", "word", "line_word"}) {
        try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
          boolean hasNext = rs.next();
          assert hasNext : "No result in count from table " + table;
          System.out.println(table + " => " + rs.getInt(1));
        }
      }
    }
  }


  public static ResultSet queryResults(Connection conn) throws SQLException {
    return conn.createStatement().executeQuery(
        "SELECT t.name, l.\"offset\", w.\"value\", lw.\"offset\" " +
            "FROM \"text\" t, word w " +
            "INNER JOIN line l ON (t.id = l.text_id) " +
            "INNER JOIN line_word lw ON (lw.line_id = l.id AND lw.word_id = w.id)"
    );
  }

  public static void printWordUsages(Database database) throws SQLException {
    int lineNumber = 0;
    try (Connection conn = database.getConnection()) {
      try (ResultSet rs = queryResults(conn)) {
        String lastText = null;
        int lastLine = -1;
        while (rs.next()) {
          if (lineNumber % 20 == 0) {
            System.out.println("text\tline-offset\tword\tword-offset");
            System.out.println("----\t-----------\t----\t-----------");
          }
          lineNumber += 1;
          String text = rs.getString(1);
          if (!text.equals(lastText)) {
            lastText = text;
          }
          int lineOffset = rs.getInt(2);
          String word = rs.getString(3);
          int wordOffset = rs.getInt(4);
          if (lineOffset != lastLine) {
            lastLine = lineOffset;
          }
          System.out.println(String.format("%s\t%d\t%s\t%d", text, lineOffset, word, wordOffset));
        }
      }
    }
  }


  public static void main(String[] args) throws Exception {
    Database database = new Database();
    database.initializeDb();

    new ShakespeareTextSource().getStream().sequential()
        .map(new TextMapper())
        .filter(Optional::isPresent).map(Optional::get)
        // In a longer example, this next line might be broken out into
        // distinct intermediate forms for providing text id, line id,
        // and word id.
        .map(new DatabaseLineMapper(database))
        .forEach(database::insertLine);

    printWordUsages(database);
    printDatabaseSizing(database);
  }

}
