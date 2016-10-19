import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Listing14 {

  public static class LambdaCollection<E> extends AbstractCollection<E> {

    private final int size;
    private final Listing14.LambdaCollection.IteratorFactory<E> factory;

    /**
     * Defines the means of acquiring an {@link java.util.Iterator}.
     */
    public interface IteratorFactory<E> {

      /**
       * Creates a new instance of an {@link java.util.Iterator}.
       * The iterator should be a fresh iterator each time this
       * method is called, but it should always iterate over the
       * same source.
       *
       * @return A new {@code Iterator}; never {@code null}.
       */
      Iterator<E> create();

    }

    public LambdaCollection(int size, IteratorFactory<E> factory) {
      this.size = size;
      this.factory = factory;
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<E> iterator() {
      return factory.create();
    }

    @Override
    public int size() {
      return size;
    }
  }

  public static Path generateFile() throws IOException {
    File file = File.createTempFile("example", "tmp");
    file.deleteOnExit();
    Path path = file.toPath();
    Files.write(path, "this\nis\nour\nfile".getBytes("UTF-8"));
    return path;
  }


  public static void main(String[] args) throws Exception {
    Path file = generateFile();
    Collection<String> collection = new LambdaCollection<>(
        (int) Files.lines(file).count(),
        () -> {
          try {
            return Files.lines(file).iterator();
          } catch (IOException ioe) {
            throw new UncheckedIOException("Error reading lines from " + file, ioe);
          }
        }
    );
  }

}

