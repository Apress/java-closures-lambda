import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.*;

public class Listing5 {


  public static void main(String[] args) throws Exception {
    Database.createDatabase();
    try (Connection conn = Database.getConnection()) {
      createStream(Database.queryResults(conn)).map(WordUsage::toTSV).forEach(System.out::println);
    }
  }

  public static Stream<WordUsage> createStream(ResultSet rs) throws SQLException {
    Stream.Builder<WordUsage> builder = Stream.builder();
    while (rs.next()) {
      WordUsage usage = WordUsage.fromResultSet(rs);
      builder.add(usage);
    }
    return builder.build();
  }


}
