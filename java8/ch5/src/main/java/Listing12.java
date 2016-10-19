import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.*;

public class Listing12 {

  public static void main(String[] args) throws Exception {
    Database.createDatabase();
    Connection conn = Database.getConnection();
    createStream(conn, Database.queryResults(conn)).map(WordUsage::toTSV).forEach(System.out::println);
  }

  public static Stream<WordUsage> createStream(Connection conn, ResultSet resultSet) throws SQLException {
    Spliterator<WordUsage> usages = new ResultSetSpliterator<WordUsage>(resultSet, Spliterator.DISTINCT, conn) {
      @Override
      protected WordUsage processRow(final ResultSet resultSet) throws SQLException {
        return WordUsage.fromResultSet(resultSet);
      }
    };
    Stream<WordUsage> stream = StreamSupport.stream(usages, true);
    return stream;
  }
}
