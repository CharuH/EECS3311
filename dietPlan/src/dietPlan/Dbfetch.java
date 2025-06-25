package dietPlan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dbfetch {
	private static final String URL = "jdbc:mysql://localhost:3306/3311_database";
    private static final String USER = "root";
    private static final String PASSWORD = "adminRomeo";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
	
	    

}
