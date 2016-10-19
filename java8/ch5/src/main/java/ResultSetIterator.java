import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * An {@link java.util.Iterator} that traverses a {@link java.sql.ResultSet}. When the result set is exhausted,
 * it will be automatically closed. Implementing classes should extend {@link #processRow(java.sql.ResultSet)} to
 * specify how to convert a row into a result object.
 */
public abstract class ResultSetIterator<RESULT_T> implements Iterator<RESULT_T> {

  private final ResultSet resultSet;
  private final AutoCloseable[] toClose;
  private volatile Boolean hasNext = null;

  /**
   * Constructor.
   *
   * @param resultSet The result set to process; may not be {@code null}.
   * @param toClose   Optional additional autocloseables (such as {@link java.sql.Connection}) to close
   *                  when the result
   *                  set is exhausted; may be {@code null} (equivalent to empty).
   */
  public ResultSetIterator(final ResultSet resultSet, final AutoCloseable... toClose) {
    Objects.requireNonNull(resultSet, "result set");
    this.resultSet = resultSet;
    if (toClose == null) {
      this.toClose = new AutoCloseable[0];
    } else {
      this.toClose = toClose;
    }
  }

  /**
   * Given a {@link java.sql.ResultSet} instance on a current row, return a {@code RESULT_T} instance for that row.
   * This code should not call {@link java.sql.ResultSet#next()} or otherwise mutate the result set: it should
   * be treated as read-only.
   *
   * @param resultSet The result set to load.
   * @return The result instance.
   * @throws java.sql.SQLException If an error occurs.
   */
  protected abstract RESULT_T processRow(ResultSet resultSet) throws SQLException;


  private void doClose() {
    try {
      if (!resultSet.isClosed()) resultSet.close();
    } catch (SQLException ignore) {}
    for (int i = 0; i < toClose.length; i++) {
      try {
        AutoCloseable closeMe = toClose[i];
        toClose[i] = null;
        if (closeMe != null && closeMe != resultSet) closeMe.close();
      } catch (Exception ignore) {}
    }
  }

  /**
   * Returns {@code true} if the iteration has more elements.
   * (In other words, returns {@code true} if {@link #next} would
   * return an element rather than throwing an exception.)
   *
   * @return {@code true} if the iteration has more elements
   */
  @Override
  public boolean hasNext() {
    try {
      if (hasNext == null) {
        hasNext = !resultSet.isClosed() && resultSet.next();
      }
    } catch (SQLException sqle) {
      throw new RuntimeException(
          "Could not determine if we have a next element", sqle
      );
    }
    if (!hasNext) doClose();
    return hasNext;
  }

  /**
   * Returns the next element in the iteration.
   *
   * @return the next element in the iteration
   * @throws java.util.NoSuchElementException if the iteration has no more elements
   */
  @Override
  public RESULT_T next() {
    if (!hasNext()) {
      throw new NoSuchElementException("No elements remaining");
    }
    try {
      return processRow(resultSet);
    } catch (SQLException sqle) {
      throw new RuntimeException("Error while processing elements", sqle);
    } finally {
      hasNext = null;
    }
  }
}

