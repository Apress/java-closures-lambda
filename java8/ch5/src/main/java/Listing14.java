import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.*;

public class Listing14 {

  public static void main(String[] args) throws Exception {
    Database.createDatabase();
    Connection conn = Database.getConnection();
    createStream(conn, Database.queryResults(conn)).map(WordUsage::toTSV).forEach(System.out::println);
  }

  public static Stream<WordUsage> createStream(Connection conn, ResultSet resultSet) throws SQLException {
    Iterator<WordUsage> usages = new ResultSetIterator<WordUsage>(resultSet, conn) {
      @Override
      protected WordUsage processRow(final ResultSet resultSet) throws SQLException {
        return WordUsage.fromResultSet(resultSet);
      }
    };
    Spliterator<WordUsage> spliterator = Spliterators.spliteratorUnknownSize(usages,
        Spliterator.NONNULL | Spliterator.DISTINCT | Spliterator.IMMUTABLE
    );
    Stream<WordUsage> stream = StreamSupport.stream(spliterator, true);
    return stream;
  }
}
