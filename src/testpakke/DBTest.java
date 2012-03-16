package testpakke;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTest {
	public static void main(String[] args) throws Exception, Exception {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://mysql.stud.ntnu.no/magnurod_venner";
			String user = "magnurod_test";
			String pw = "enbunt";
			con = DriverManager.getConnection(url, user, pw);
			System.out.println("Tilkoblingen fungerte.");
		} catch (SQLException ex) {
			System.out.println("Tilkobling feilet: " + ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out
					.println("Feilet under driverlasting: " + ex.getMessage());
			System.out.println(ex);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException ex) {
				System.out.println("Epic fail: " + ex.getMessage());
			}
		}
	}
}
