import java.util.stream.*;

public class Listing3 {


  public static void main(String[] args) {
    WordUsage template = new WordUsage("some text", 0, "word", 1);
    PojoWordUsage pojoTemplate = new PojoWordUsage("some text", 0, "word", 1);
    Stream<Integer> lineOffsets = Stream.of(/* INSERT LINE OFFSETS HERE*/);

    Stream<PojoWordUsage> assignedPojos = lineOffsets.map(offset -> {
          pojoTemplate.setLineOffset(offset);
          return pojoTemplate;
        }
    );
    Stream<WordUsage> assignedFujis = lineOffsets.map(template::withLineOffset);
  }


}
