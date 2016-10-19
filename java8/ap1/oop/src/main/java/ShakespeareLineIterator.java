import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;

/**
 * Iterates through the Shakespeare anthology, providing the lines of the anthology.
 */
public class ShakespeareLineIterator implements Iterator<String> {

  private static final String SOURCE = "shakespeare.txt";
  private static final Predicate<String> IS_WHITESPACE = Pattern.compile("^\\s*$").asPredicate();
  private static final Pattern BAD_CHARS = Pattern.compile("\uFEFF|\\p{Cntrl}"); // BOMs and control characters
  private static final Predicate<String> IS_COMMENT_START = Pattern.compile("^<<").asPredicate();
  private static final Predicate<String> IS_COMMENT_END = Pattern.compile(">>$").asPredicate();

  private final BufferedReader reader;
  private volatile boolean closed = false;
  private volatile String nextElement = null;
  private volatile IOException exception = null;

  /**
   * Constructs an instance of the class which draws from the packaged complete works of Shakespeare text.
   *
   * @throws IOException If there is an exception getting access to the resource.
   */
  public ShakespeareLineIterator() throws IOException {
    InputStream inputStream = ShakespeareLineIterator.class.getClassLoader().getResourceAsStream(SOURCE);
    this.reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
  }

  private void explodeIfException() {
    if (exception != null) {
      throw new UncheckedIOException("error reading the stream", exception);
    }
  }


  /**
   * Returns the next line of the text, filtering out whitespace, bad characters, comments, etc.
   * Assigns {@link #nextElement} to the next line, or to {@code null} if there are no lines remaining.
   */
  private String nextLine() {
    explodeIfException();
    if (closed) return nextElement = null;
    try {
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

      if (line != null && IS_COMMENT_START.test(line)) {
        while ((line = reader.readLine()) != null) {
          if (IS_COMMENT_END.test(line)) break;
        }
        if (line != null) return nextLine();
      }

      if (line == null) {
        reader.close();
        closed = true;
      }

      return nextElement = line;
    } catch (IOException ioe) {
      this.exception = ioe;
      explodeIfException();
      throw new IllegalStateException("This code should never be reached!");
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
    explodeIfException();
    if (nextElement != null) return true;
    if (closed) return false;
    if (nextLine() != null) return true;
    return false;
  }

  /**
   * Returns the next element in the iteration.
   *
   * @return the next element in the iteration
   * @throws java.util.NoSuchElementException if the iteration has no more elements
   */
  @Override
  public String next() {
    explodeIfException();
    if (nextElement != null || nextLine() != null) {
      String toReturn = nextElement;
      nextElement = null;
      return toReturn;
    }
    throw new NoSuchElementException("No more lines to be read!");
  }

  /**
   * Provides the element that will be returned by {@link #next()} without progressing the iterator.
   *
   * @return What will be the next element, or {@code null} if there is no next element.
   */
  public String peek() {
    explodeIfException();
    if (nextElement != null) return nextElement;
    if (closed) return null;
    return nextLine();
  }
}
