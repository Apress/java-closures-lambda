import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.*;

/**
 * A {@link java.util.Spliterator} that traverses a {@link java.sql.ResultSet}. When the result set is exhausted,
 * it will be automatically closed. Implementing classes should extend {@link #processRow(java.sql.ResultSet)} to
 * specify how to convert a row into a result object.
 */
public abstract class ResultSetSpliterator<RESULT_T> extends Spliterators.AbstractSpliterator<RESULT_T> {

  private static final int CHARACTERISTICS = Spliterator.IMMUTABLE | Spliterator.NONNULL;
  private final ResultSet resultSet;
  private final AutoCloseable[] toClose;

  /**
   * Constructor.
   *
   * @param resultSet                 The result set to process; may not be {@code null}.
   * @param additionalCharacteristics relevant characteristics beyond {@link java.util.Spliterator#IMMUTABLE} and
   *                                  {@link java.util.Spliterator#NONNULL}, or {@code 0} if none
   * @param toClose                   Optional additional autocloseables (such as {@link java.sql.Connection}) to close
   *                                  when the result
   *                                  set is exhausted; may be {@code null} (equivalent to empty).
   */
  public ResultSetSpliterator(final ResultSet resultSet, final int additionalCharacteristics,
                              final AutoCloseable... toClose
  ) {
    super(Long.MAX_VALUE, CHARACTERISTICS | additionalCharacteristics);
    Objects.requireNonNull(resultSet, "result set");
    this.resultSet = resultSet;
    if (toClose == null) {
      this.toClose = new AutoCloseable[0];
    } else {
      this.toClose = toClose;
    }
  }

  /**
   * Given a {@link ResultSet} instance on a current row, return a {@code RESULT_T} instance for that row.
   * This code should not call {@link java.sql.ResultSet#next()} or otherwise mutate the result set: it should
   * be treated as read-only.
   *
   * @param resultSet The result set to load.
   * @return The result instance.
   * @throws SQLException If an error occurs.
   */
  protected abstract RESULT_T processRow(ResultSet resultSet) throws SQLException;

  /**
   * If a remaining element exists, performs the given action on it,
   * returning {@code true}; else returns {@code false}.  If this
   * Spliterator is {@link #ORDERED} the action is performed on the
   * next element in encounter order.  Exceptions thrown by the
   * action are relayed to the caller.
   *
   * @param action The action
   * @return {@code false} if no remaining elements existed
   * upon entry to this method, else {@code true}.
   * @throws NullPointerException if the specified action is null
   */
  @Override
  public boolean tryAdvance(final Consumer<? super RESULT_T> action) {
    Objects.requireNonNull(action, "action to be performed");
    try {
      if (resultSet.isClosed() || !resultSet.next()) {
        doClose();
        return false;
      }
      RESULT_T result = processRow(resultSet);
      if (result == null) {
        throw new NullPointerException("Returned null from processRow");
      } else {
        action.accept(result);
        return true;
      }
    } catch (SQLException sqle) {
      throw new RuntimeException("SQL Exception while processing result set", sqle);
    }
  }

  private void doClose() {
    try {
      if (resultSet != null && !resultSet.isClosed()) resultSet.close();
    } catch (SQLException ignore) {}
    for (int i = 0; i < toClose.length; i++) {
      try {
        AutoCloseable closeMe = toClose[i];
        toClose[i] = null;
        if (closeMe != null && closeMe != resultSet) closeMe.close();
      } catch (Exception ignore) {}
    }
  }

}

