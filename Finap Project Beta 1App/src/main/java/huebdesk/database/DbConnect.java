package huebdesk.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbConnect {
	// Code database URL
	static final String DB_URL = "website";
	
	// Database credentials
	static final String USER = "username", PASS = "password";

	public Connection connect() throws SQLException {

		return DriverManager.getConnection(DB_URL, USER, PASS);

	}
}
