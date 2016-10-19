import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.*;

public class Listing1 {

  public static void main(String[] args) {
    Consumer<Object> consumer = null;
    try (InputStream in = acquireInputStream()) {
      consumer = it -> {
        try {
          System.out.println("Trying to read!");
          in.read();
        } catch (IOException ioe) {
          throw new UncheckedIOException("read error", ioe);
        }
      };
    } catch (IOException ioe) {
      throw new UncheckedIOException("input stream error", ioe);
    }
    consumer.accept(new Object());
  }

  public static InputStream acquireInputStream() {
    return new InputStream() {
      @Override
      public int read() throws IOException {
        return 1;
      }

      @Override
      public void close() throws IOException {
        System.out.println("Closing!");
        super.close();
      }
    };
  }


}

