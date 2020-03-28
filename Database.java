import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Database {
	
	Connection conn = null;
	static Vector data = new Vector();
	
	Database() {
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            
            // jdbc:SGBD://ip:port/database_name?user=username&password=pwd
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Database1", "root","Alya@mysql16");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	public void createTable(GeneralTable gtable) {
		try {

			Statement stmt = this.conn.createStatement();
			
			String query = "CREATE TABLE IF NOT EXISTS "+ gtable.getTableName() +" (" + 
					" id INT AUTO_INCREMENT PRIMARY KEY,";
			
			for(int i = 0; i<gtable.getHeaders().length; i++) {
				query += " " + gtable.getHeaders()[i];
				switch(gtable.getTypes()[i]) {
				case "STRING":
					query += " VARCHAR(255)";
					break;
				case "NUMERIC":
					query += " NUMERIC";
					break;
				case "BOOLEAN":
					 query += " BOOLEAN";
	                                 break;
				default:
					break;
				}
				if((i + 1) != gtable.getHeaders().length) {
					query += ",";
				}				
				
			}
			query += ")  ENGINE=INNODB;";

// Other types we may configure - Mhz
			
//					"    title VARCHAR(255) NOT NULL,\n" + 
//					"    start_date DATE,\n" + 
//					"    due_date DATE,\n" + 
//					"    status TINYINT NOT NULL,\n" + 
//					"    priority TINYINT NOT NULL,\n" + 
//					"    description TEXT,\n" + 
//					"    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" + 
//					")  ENGINE=INNODB;";

			int rs = stmt.executeUpdate(query);
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	public void addTuples(GeneralTable gtable) {
		try {

			Statement stmt = this.conn.createStatement();
			
			for(int r = 0; r<gtable.getRows().size(); r++) {
				
			String query = "INSERT INTO "+ gtable.getTableName() +" (";
			
			for(int i = 0; i<gtable.getHeaders().length; i++) {
				query += gtable.getHeaders()[i];
				if((i + 1) != gtable.getHeaders().length) {
					query += ",";
				}				
				
			}
			query += ")  VALUES(";
					
			for(int c = 0; c<gtable.getHeaders().length; c++) {
				if(gtable.getRows().get(r)[c] instanceof String) {
					query += "'" + gtable.getRows().get(r)[c] + "'";
				} else {
					query += gtable.getRows().get(r)[c];
				}
				if((c + 1) != gtable.getHeaders().length) {
					query += ",";
				}				
				
			}		
										
			query += ");";
		
			
			int rs = stmt.executeUpdate(query);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	//Jtable output
	/*public String[][] getTuples(GeneralTable gtable) {
		
		try {

			Statement stmt = this.conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM "+ gtable.getTableName() +";");
			int totalColumns_rs = rs.getMetaData().getColumnCount();
			boolean b = rs.last(); 
			int totalRows_rs = rs.getRow();
			rs.beforeFirst();
			
			String[][]data = new String[totalRows_rs][totalColumns_rs];
			int j=0;
			
			while(rs.next()) {
				
				for(int i = 0; i<gtable.getHeaders().length; i++) {
					data[j][i]=rs.getString((gtable.getHeaders()[i]) );}
				
				j++;
					
				}
			return data;	
			}

		 catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
				
	}*/

	//original 
	public void getTuples(GeneralTable gtable) {
		try {

			Statement stmt = this.conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM "+ gtable.getTableName() +";");

			while(rs.next()) {
				for(int i = 0; i<gtable.getHeaders().length; i++) {
					System.out.print(rs.getString(gtable.getHeaders()[i]) + " ");			
				}
				System.out.println();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
		public Vector getTableTuples(GeneralTable gtable) {
		try {

			Statement stmt = this.conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM "+ gtable.getTableName() +";");
			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();
			data.clear();  
		    while (rs.next()) {
		        Vector row = new Vector(columns);
		        for (int i = 2; i <= columns; i++) {
		         row.addElement(rs.getObject(i));
		        }
		        data.addElement(row);
		     }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
				
	}

}
