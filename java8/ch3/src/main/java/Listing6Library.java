public class Listing6Library extends Listing5Library {
  /**
   * Performs various clean-up tasks on all the features messages.
   */
  public void cleanUpFeaturedMessages() {
    this.getFeaturedBooks().replaceAll((book, msg) -> {
          // Set a default message
          if (msg == null || msg.isEmpty()) {
            msg = "Featured " + book.getGenre().toString().toLowerCase() + " book by " + book.getAuthor();
          }

          // Remove trailing periods unless they are a bad elipsis
          if (msg.endsWith(".") && !msg.endsWith("...")) {
            msg = msg.substring(0, msg.length() - 1);
          }

          return msg;
        }
    );
  }

}
