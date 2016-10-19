public class Listing3 {
  public static void main(String[] args) {
    Library library = Library.createSampleLibrary();

    library.getFeaturedBooks().forEach((book, msg) ->
                                           System.out.println(book + ": " + msg)
    );

  }
}
