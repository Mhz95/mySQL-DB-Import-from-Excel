import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	Connection conn = null;
	
	Database() {
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            
            // jdbc:SGBD://ip:port/database_name?user=username&password=pwd
            this.conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cscdb?user=root&serverTimezone=UTC");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	public void getIDs() {
		try {

			Statement stmt = this.conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM test;");

			while(rs.next()) {
				System.out.println(rs.getString("id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

}
