import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;

public class Database {

  private static final String SOURCE = "shakespeare.txt";
  private static final String SCHEMA = "shakespeare";
  private static final Predicate<String> IS_WHITESPACE = Pattern.compile("^\\s*$").asPredicate();
  private static final Predicate<String> IS_YEAR = Pattern.compile("^1(5|6)\\d\\d$").asPredicate();
  private static final Predicate<String> IS_COMMENT_START = Pattern.compile("^<<").asPredicate();
  private static final Predicate<String> IS_COMMENT_END = Pattern.compile(">>$").asPredicate();
  private static final Predicate<String> IS_THE_END = Pattern.compile("^THE\\s+END$").asPredicate();
  private static final Predicate<String> IS_DIGITS = Pattern.compile("^\\d+$").asPredicate();
  private static final Predicate<String> IS_AUTHOR = Pattern.compile("^by\\s+William\\s+Shakespeare$").asPredicate();
  private static final String SONNETS_TITLE = "THE SONNETS";
  private static final Pattern BAD_CHARS = Pattern.compile("\uFEFF|\\p{Cntrl}"); // BOMs and control characters

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:h2:mem:shakespeare;INIT=CREATE SCHEMA IF NOT EXISTS " + SCHEMA + "\\; " +
            "SET SCHEMA " + SCHEMA + ";DB_CLOSE_DELAY=-1", "sa", ""
    );
  }

  /**
   * Creates the database by parsing {@link #SOURCE} and populating its contents into the database connected to via the
   * {@link #getConnection()} method.
   */
  public static void createDatabase() throws Exception {
    final Map<String, Integer> wordMap = new HashMap<>();
    final ExecutorService executor = Executors.newCachedThreadPool();
    try (Connection conn = getConnection()) {
      Statement stmt = conn.createStatement();
      stmt.execute("DROP SCHEMA " + SCHEMA);
      stmt.execute("CREATE SCHEMA " + SCHEMA);
      stmt.execute("SET SCHEMA " + SCHEMA);
      stmt.execute("CREATE TABLE \"text\" (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR UNIQUE, year INT)");
      stmt.execute("CREATE TABLE line (id INT PRIMARY KEY AUTO_INCREMENT, text_id INT, \"offset\" INT)");
      stmt.execute("CREATE TABLE word (id INT PRIMARY KEY AUTO_INCREMENT, \"value\" VARCHAR UNIQUE)");
      stmt.execute("CREATE TABLE line_word (id INT PRIMARY KEY AUTO_INCREMENT, line_id INT, word_id INT, " +
              "\"offset\" INT)"
      );

      PreparedStatement createBook = conn.prepareStatement("INSERT INTO \"text\" (name, year) VALUES (?,?)",
          Statement.RETURN_GENERATED_KEYS
      );
      PreparedStatement createLine = conn.prepareStatement("INSERT INTO line (text_id, \"offset\") VALUES (?,?)",
          Statement.RETURN_GENERATED_KEYS
      );
      PreparedStatement createWord = conn.prepareStatement("INSERT INTO word (\"value\") VALUES (?)",
          Statement.RETURN_GENERATED_KEYS
      );
      PreparedStatement createLineWord = conn.prepareStatement(
          "INSERT INTO line_word " +
              " (line_id, word_id, \"offset\") " +
              " VALUES (?,?,?)",
          Statement.NO_GENERATED_KEYS
      );

      try (InputStream stream = Database.class.getClassLoader().getResourceAsStream(SOURCE)) {
        assert stream != null : "No resource found at: " + SOURCE;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        int textId = 0;
        int lineOffset = 0;
        boolean inTheSonnets = false;
        while ((line = nextLine(reader)) != null) {
          if (inTheSonnets && IS_DIGITS.test(line)) {
            textId = doCreateBook(createBook, "Sonnet #" + line, 1609, executor);
            lineOffset = 0;
          } else if (IS_YEAR.test(line)) {
            String year = line;
            String title = nextLine(reader);
            String author = nextLine(reader);
            assert IS_AUTHOR.test(author) : (title + " " + year + " did not provide the right author: " + author);

            inTheSonnets = SONNETS_TITLE.equalsIgnoreCase(title);

            if (!inTheSonnets) {
              textId = doCreateBook(createBook, title, Integer.parseInt(year), executor);
              lineOffset = 0;
            }
          } else if (IS_COMMENT_START.test(line)) {
            while ((line = nextLine(reader)) != null && !IS_COMMENT_END.test(line)) {
              continue;
            }
            assert line != null : "No ending comment found";
          } else if (IS_THE_END.test(line)) {
            inTheSonnets = false;
          } else {
            assert (textId != 0) : "Processing, but no title provided";
            lineOffset += 1;
            createLine.setInt(1, textId);
            createLine.setInt(2, lineOffset);
            boolean createdLine = createLine.executeUpdate() == 1;
            assert createdLine : "Could not create line";
            ResultSet rs = createLine.getGeneratedKeys();
            boolean hasNext = rs.next();
            assert hasNext : "No result when getting generated keys for for line " + textId + "-" + lineOffset;
            int lineId = rs.getInt(1);
            rs.close();

            String[] words = line.split("\\s+");
            for (int i = 0; i < words.length; i++) {
              int wordOffset = i + 1;
              String word = words[i];
              word = word.replaceAll("(?!')\\p{Punct}", "");

              final int wordId;
              if (wordMap.containsKey(word)) {
                wordId = wordMap.get(word);
              } else {
                createWord.setString(1, word);
                boolean createdWord = createWord.executeUpdate() == 1;
                assert createdWord : "Could not create word: " + word;
                rs = createWord.getGeneratedKeys();
                hasNext = rs.next();
                assert hasNext : "Created word but still could not find it!";

                String newWord = word;
                executor.submit(() -> System.out.println("New word: " + newWord));
                wordId = rs.getInt(1);
                rs.close();
                wordMap.put(word, wordId);
              }
              createLineWord.setInt(1, lineId);
              createLineWord.setInt(2, wordId);
              createLineWord.setInt(3, wordOffset);
              boolean createdLineWord = createLineWord.executeUpdate() == 1;
              assert createdLineWord : "Could not create line-word";
            }
          }
        }
      }
    } finally {
      executor.shutdown();
      executor.awaitTermination(1L, TimeUnit.MINUTES);
    }
  }

  /**
   * Returns the next
   */
  public static String nextLine(BufferedReader reader) throws IOException {
    String line;
    while ((line = reader.readLine()) != null) {
      line = BAD_CHARS.matcher(line).replaceAll("");
      line = line.trim();
      if (line.isEmpty() || IS_WHITESPACE.test(line)) {
        continue;
      } else {
        break;
      }
    }
    return line;
  }

  public static void printDatabaseSizing() throws SQLException {
    System.out.println("SIZES");
    System.out.println("-----");
    try (Connection conn = getConnection()) {
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

  public static void printWordUsages() throws SQLException {
    int lineNumber = 0;
    try (Connection conn = getConnection()) {
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

  private static int doCreateBook(PreparedStatement createBook, String title, int year,
                                  ExecutorService executor
  ) throws SQLException {
    createBook.setString(1, title);
    createBook.setInt(2, year);
    boolean createdBook = createBook.executeUpdate() == 1;
    assert createdBook : "Could not create book";
    ResultSet rs = createBook.getGeneratedKeys();
    boolean hasNext = rs.next();
    assert hasNext : "No result when getting generated keys for " + title;
    int textId = rs.getInt(1);
    rs.close();

    executor.submit(() -> System.out.println(title));

    return textId;
  }

  public static void main(String[] args) throws Exception {
    createDatabase();
    printDatabaseSizing();

    long startMillis = System.currentTimeMillis();
    printWordUsages();
    long endMillis = System.currentTimeMillis();
    System.out.println("Took " + TimeUnit.MILLISECONDS.toSeconds(endMillis - startMillis) + " seconds");
  }


}
