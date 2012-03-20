package testpakke;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Venner");
			while(rs.next()){
				String s = rs.getString(3);
				System.out.println(s);
			}
			
			if (rs != null){
				rs.close();
			}
			
		} catch (SQLException ex) {
			System.out.println("Tilkobling feilet: " + ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out.println("Feilet under driverlasting: " + ex.getMessage());
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
