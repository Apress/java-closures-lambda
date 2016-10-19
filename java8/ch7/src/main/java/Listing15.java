import java.util.*;

public class Listing15 {

  public static class SomeBean {
    private int id = 0;
    private Optional<String> name = Optional.empty();

    public SomeBean() {
    }

    public SomeBean(final int id, final String name) {
      this.id = id;
      this.name = Optional.ofNullable(name);
    }

    public int getId() {
      return id;
    }

    public void setId(final int id) {
      this.id = id;
    }

    @Deprecated
    public String getName() {
      return name.orElseThrow(() -> new NullPointerException("No name provided for: " + this.toString()));
    }

    public Optional<String> getNameOptional() {
      return name;
    }

    public void setName(final String name) {
      this.name = Optional.ofNullable(name);
    }

    public void setName(final Optional<String> name) {
      this.name = (name == null ? Optional.empty() : name);
    }

    @Override
    public String toString() {
      return "SomeBean{" +
          "id=" + id +
          ", name=" + name.orElse(null) +
          '}';
    }
  }

}
