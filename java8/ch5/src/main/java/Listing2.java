import java.util.stream.*;

public class Listing2 {

  public static void main(String[] args) {
    Stream<PojoWordUsage> pojos = Stream.of(/* INSERT POJOS HERE */);
    pojos.map(pojo -> {
          pojo.setTextName("New Text Name");
          return pojo;
        }
    );

    Stream<WordUsage> fujis = Stream.of(/* INSERT FUJIS HERE */);
    fujis.map(fuji -> fuji.withTextName("New Text Name"));
  }


}
