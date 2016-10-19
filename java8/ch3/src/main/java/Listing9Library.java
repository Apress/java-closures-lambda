public class Listing9Library extends Listing8Library {

  @Override
  public String getFeaturedBookMessage(final Book featuredBook) {
    return getFeaturedBooks().compute(featuredBook, (book, msg) -> {
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
