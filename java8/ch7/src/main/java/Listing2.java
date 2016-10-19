import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.*;

public class Listing2 {

  public static InputStream createInputStream() throws IOException {
    return new ByteArrayInputStream("foobar".getBytes());
  }

  public static void call(Consumer<Object> c) {
    c.accept(new Object());
  }

  public static void main(String[] args) {
    call(it -> {
          try (InputStream in = createInputStream()) {
            throw new IOException("And with a kiss, I die!");
          } catch (IOException e) {
            throw new UncheckedIOException("Error working with input stream", e);
          }
        }
    );
  }
}
