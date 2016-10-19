public class Main {

  public static void main(String[] args) throws Exception {
    Library library = Library.createSampleLibrary();
    String booksString =
        library.getBooks().parallelStream().map(Book::toString).map(str -> str + "\n").reduce("", String::concat);
  }
}

