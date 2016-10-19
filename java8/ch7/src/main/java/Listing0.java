import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.*;

public class Listing0 {

  public InputStream acquireInputStream() {
return  null;
  }

  public void oldTryWithResource() {
    InputStream in = null;
    try {
      in = acquireInputStream();
      // Do something
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException closeException) {
          // What now?
        }
      }
    }
  }

  public void closeQuiety(InputStream in) {
    if (in != null) {
      try {
        in.close();
      } catch (IOException ignored) {}
      ;
    }
  }

  public void java7TryWithResource() {
    try (InputStream in = acquireInputStream()) {
      // Do something
    } catch (IOException exception) {
      // What now?
    }
  }

  public void lambdaWithTryWithResourceUnchecked() {
    Consumer<Object> consumer = it -> {
      try (InputStream in = acquireInputStream()) {
        // Do something
      } catch (IOException exception) {
        throw new UncheckedIOException("something went wrong!", exception);
      }
    };
  }

}

