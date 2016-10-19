import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.*;

public class IoCExample {

  static class SomeModel {
    public SomeModel(int value) {
    }
  }

  public static void main(String[] args) {
    (PreparedStatement ps) -> ps.setString(1, "some value for baz");
    (rs, rowNum) -> new SomeModel(rs.getInt(1));
  }

  public void doStuff() {
    Consumer<Iterable> printObjects = list -> {
      for (Object it : list) {
        System.out.println(it);
      }
    };
    Consumer<Iterable> printObjects2 = list -> list.forEach(System.out::println);


    JdbcTemplate jdbcTemplate = new JdbcTemplate(null);
    jdbcTemplate.query("SELECT bar FROM Foo WHERE baz = ?",
                       new PreparedStatementSetter() {
                         @Override
                         public void setValues(final PreparedStatement ps) throws SQLException {
                           ps.setString(1, "some value for baz");
                         }
                       },
                       new RowMapper<SomeModel>() {
                         @Override
                         public SomeModel mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                           return new SomeModel(rs.getInt(1));
                         }
                       }
    );

    jdbcTemplate.query("SELECT bar FROM Foo WHERE baz = ?",
                       ps -> ps.setString(1, "some value for baz"),
                       (rs, rowNum) -> new SomeModel(rs.getInt(1))
    );
  }


}
