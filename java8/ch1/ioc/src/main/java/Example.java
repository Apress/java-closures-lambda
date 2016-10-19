import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.sql.*;
import org.springframework.jdbc.core.*;

public class Example {
	private static final String sql = null;

	public static void main(String[] args) {
	}

	private void java7() {
		JdbcTemplate it = new JdbcTemplate();
		it.query(sql,
			new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, "foo");
				}
			},
			new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString(1);
				}
			}
		);
	}

	private void java8() {
		JdbcTemplate it = new JdbcTemplate();
		it.query(sql,
			ps -> ps.setString(1,"foo"),
			(rs, rowNum) -> rs.getString(1)
		);
	}
}
