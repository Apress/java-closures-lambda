import java.util.*;

/**
 * Special case of a {@link ShakespeareText} containing the specific information for the sonnets.
 */
public class ShakespeareSonnet extends ShakespeareText {

  public ShakespeareSonnet(int number, List<ShakespeareLine> lines) {
    super("Sonnet #" + number, 1609, lines);
    if (number < 1 || number > 175) {
      throw new IllegalArgumentException("Egregiously wrong number for the sonnet: " + number);
    }
  }
}
