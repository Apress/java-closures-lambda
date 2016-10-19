import funjava.Result;
import funjava.ResultErrorHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.*;
import java.util.stream.*;

public class Listing7 {

  public static void main(String[] args) throws Exception {
    Database.createDatabase();
    try (Connection conn = Database.getConnection()) {
      createStream(Database.queryResults(conn), System.out::println).map(WordUsage::toTSV).forEach(System.out::println);
    }
  }

  public static Stream<WordUsage> createStream(ResultSet resultSet, Consumer<Exception> errorHandler)
      throws SQLException {
    return Stream.of(resultSet).flatMap(rs -> {
          Stream.Builder<Result<WordUsage>> builder = Stream.builder();
          try {
            while (rs.next()) {
              try {
                WordUsage usage = WordUsage.fromResultSet(rs);
                builder.add(Result.of(usage));
              } catch(SQLException sqle) {
                builder.add(Result.of(sqle));
              }
            }
          } catch (SQLException sqle) {
            builder.add(Result.of(sqle));
          } finally {
            try {
              rs.close();
            } catch (SQLException sqle) {
              builder.add(Result.of(sqle));
            }
          }
          return builder.build();
        }
    ).flatMap(new ResultErrorHandler<>(errorHandler));
  }
}

