import java.beans.ExceptionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.*;

public class Listing22 {

  /**
   * Given a {@link java.sql.Connection}, autocommit a transaction using Java's {@code try-with-resources}.
   */
  public static AutoCloseable autoCommit(Connection c) throws SQLException {
    if (c == null) return () -> {};
    return c::commit;
  }

  // Create concurrency interfaces from lambdas
  Runnable runMe = () -> System.out.println("Ran!");
  Callable<Long> callMe = System::currentTimeMillis;
  ThreadFactory t = Thread::new;

  // Implement listener interfaces
  ExceptionListener listener = Exception::printStackTrace;

}
