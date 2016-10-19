import java.util.*;

/**
 * Created by RCFischer on 11/11/14.
 */
public class ShakespeareLine {

  private final int position;
  private final String[] words;

  public ShakespeareLine(final int textPosition, final String line) {
    Objects.requireNonNull(line, "line");
    words = line.split("\\s+");

    if(textPosition < 1) {
      throw new IllegalArgumentException("Invalid text position: " + textPosition);
    }
    this.position = textPosition;
  }

  public int getTextPosition() {
    return position;
  }

  public List<ShakespeareWord> getWords() {
    List<ShakespeareWord> swords = new ArrayList<>();
    for(int i = 0; i < words.length; i++) {
      swords.add(new ShakespeareWord(i+1, words[i]));
    }
    return swords;
  }

}
