import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection {

	private static Connection conn = null;
	
	private OracleConnection() { }
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		if(conn == null) {
			Class.forName("oracle.jdbc.driver.OracleDriver");


			conn = DriverManager.getConnection(
					//"jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = 10.1.114.69)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = 10.1.114.71)(PORT = 1521))(LOAD_BALANCE = yes)(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = pvlie)))",
					"jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = 10.1.114.152)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = 10.1.114.164)(PORT = 1521))(LOAD_BALANCE = yes)(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = HVLIE)))",
					"93245848",
					"trocar123");
	}
		
		return conn;
	}
	
	public static void closeConnection() throws SQLException {
		if(conn != null) {
			conn.close();
		}
	}
}
