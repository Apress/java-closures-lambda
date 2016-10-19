import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.*;

public class Listing6 {

  public static void main(String[] args) throws Exception {
    Database.createDatabase();
    try (Connection conn = Database.getConnection()) {
      createStream(Database.queryResults(conn)).map(WordUsage::toTSV).forEach(System.out::println);
    }
  }

  public static Stream<WordUsage> createStream(ResultSet resultSet) throws SQLException {
    Stream<ResultSet> rsStream = Stream.of(resultSet).onClose(() -> {
          try {
            resultSet.close();
          } catch (SQLException ignore) {}
        }
    );
    return rsStream.flatMap(rs -> {
          Stream.Builder<WordUsage> builder = Stream.builder();
          try {
            while (rs.next()) {
              WordUsage usage = WordUsage.fromResultSet(rs);
              builder.add(usage);
            }
          } catch (SQLException sqle) {
            // TODO Handle exceptions
          }
          return builder.build();
        }
    );
  }
}

