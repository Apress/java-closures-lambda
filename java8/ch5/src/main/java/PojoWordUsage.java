/**
 * Functional-friendly class for storing results of querying the word usage database.
 */
public class PojoWordUsage {

  private String textName;
  private int lineOffset;
  private String word;
  private int wordOffset;

  public PojoWordUsage(final String textName, final int lineOffset, final String word, final int wordOffset) {
    this.textName = textName;
    this.lineOffset = lineOffset;
    this.word = word;
    this.wordOffset = wordOffset;
  }

  public String getTextName() {
    return textName;
  }

  public void setTextName(final String textName) {
    this.textName = textName;
  }

  public PojoWordUsage withTextName(String textName) {
    return new PojoWordUsage(textName, lineOffset, word, wordOffset);
  }

  public int getLineOffset() {
    return lineOffset;
  }

  public void setLineOffset(final int lineOffset) {
    this.lineOffset = lineOffset;
  }

  public PojoWordUsage withLineOffset(int lineOffset) {
    return new PojoWordUsage(textName, lineOffset, word, wordOffset);
  }

  public String getWord() {
    return word;
  }

  public void setWord(final String word) {
    this.word = word;
  }

  public PojoWordUsage withWord(String word) {
    return new PojoWordUsage(textName, lineOffset, word, wordOffset);
  }

  public int getWordOffset() {
    return wordOffset;
  }

  public void setWordOffset(final int wordOffset) {
    this.wordOffset = wordOffset;
  }

  public PojoWordUsage withWordOffset(int wordOffset) {
    return new PojoWordUsage(textName, lineOffset, word, wordOffset);
  }

}
