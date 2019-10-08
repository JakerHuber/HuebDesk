package huebdesk.database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import huebdesk.items.Admins;
import huebdesk.items.Users;

// CRUD methods
public class DaoLogin {
	// Declare DB objects
	DbConnect conn = null;
	Statement stmt = null;

	// Constructor
	public DaoLogin() {
		// Connect to the PAPA SERVER
		conn = new DbConnect();
	}

	/*
	 * checkUsername tests if username exists or not
	 * 
	 * @Param eusername username to check for
	 * 
	 * @Return answer answer if it exists or not
	 * 
	 */
	public boolean checkUsername(String eusername) {
		boolean answer = false;

		// Get Username from database
		String usernameresult = getUsername(eusername);

		// Compare Username and return answer
		if (usernameresult.equals(eusername) && !usernameresult.equals("")) {
			answer = true;
		} else {
			answer = false;
		}
		return answer;
	}

	/*
	 * getUsername returns the username if it exists
	 * 
	 * @Param eusername the username to query for
	 * 
	 * @return usernameresult the username if it exists
	 */
	public String getUsername(String eusername) {
		ResultSet result = null;
		String usernameresult = "";

		try {
			// Identify statement parameters
			String sql = "SELECT eusername FROM j_hueb_employees WHERE eusername=?";

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Insert statement parameters
			preparedStatement.setString(1, eusername);

			// Execute statement
			result = preparedStatement.executeQuery();

			// Retrieve first username row
			result.next();
			usernameresult = result.getString("eusername");

			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getUsername failed.");
		}
		return usernameresult;
	}

	/*
	 * getID returns the employee_id if the username exists
	 * 
	 * @Param eusername the username to query for
	 * 
	 * @return idresult the employee_id
	 */
	public int getID(String eusername) {
		ResultSet result = null;
		int idresult = 0;

		try {
			// Identify statement parameters
			String sql = "SELECT employee_id FROM j_hueb_employees WHERE eusername=?";

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Insert statement parameters
			preparedStatement.setString(1, eusername);

			// Execute statement
			result = preparedStatement.executeQuery();

			// Retrieve first id row
			result.next();
			idresult = result.getInt("employee_id");

			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getID failed.");
		}
		return idresult;
	}

	/*
	 * checkPassword tests if the password is correct or not
	 * 
	 * @param eusername the username for the employee
	 * 
	 * @param epassword the unverified password to test
	 * 
	 * @return answer if the password is a match
	 * 
	 */
	public boolean checkPassword(String eusername, String epassword) {
		boolean answer = false;
		byte[] passwordtoHashed = null;

		// Gets employee ID from eusername
		int employee_id = getID(eusername);

		// Hash password that the user input
		passwordtoHashed = getHash(epassword, getSalt(employee_id));

		// Get Hashed password from database
		byte[] passwordresult = getPassword(employee_id);

		// Compare both hashed passwords
		if (Arrays.equals(passwordtoHashed, passwordresult)) {
			answer = true;
		} else {
			answer = false;
		}

		return answer;
	}

	/*
	 * getPassword returns the employee's hashed password if it exists
	 * 
	 * @param employee_id the employee's ID
	 * 
	 * @return passwordresult the hashed password
	 */

	public byte[] getPassword(int employee_id) {
		ResultSet result = null;
		byte[] passwordresult = null;

		try {
			// Identify statement parameters
			String sql = "SELECT epassword FROM j_hueb_employees WHERE employee_id=?";

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Insert statement parameters
			preparedStatement.setInt(1, employee_id);

			// Execute statement
			result = preparedStatement.executeQuery();

			// Retrieve first password row
			result.next();
			passwordresult = result.getBytes("epassword");

			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getPassword failed.");
		}

		return passwordresult;
	}

	/*
	 * getSalt returns the employee's salt if it exists
	 * 
	 * @param employee_id the employee's ID
	 * 
	 * @return saltresult the salt
	 * 
	 */

	public byte[] getSalt(int employee_id) {
		ResultSet result = null;
		byte[] saltresult = null;

		try {
			// Identify statement parameters
			String sql = "SELECT esalt FROM j_hueb_employees WHERE employee_id=?";

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Insert statement parameters
			preparedStatement.setInt(1, employee_id);

			// Execute statement
			result = preparedStatement.executeQuery();

			// Retrieve first password row
			result.next();
			saltresult = result.getBytes("esalt");

			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getSalt failed.");
		}

		return saltresult;
	}

	/*
	 * getHash returns a hashed version of any password given 
	 * the password string and a salt
	 * 
	 * @param password the password to be hashed
	 * 
	 * @param salt the salt to be added to the hash
	 * 
	 * @return hashresult the resulting SHA-256 hash
	 * 
	 */
	public byte[] getHash(String password, byte[] salt) {
		byte[] hashresult = null;

		// Attempt to hash password
		try {
			// Create SHA-256 message digest
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// Pass the salt to the digest for computation
			md.update(salt);

			// Generate the hash for the salted password
			hashresult = md.digest(password.getBytes(StandardCharsets.UTF_8));

		} catch (NoSuchAlgorithmException e) {
			System.out.println("getHash failed");
			e.printStackTrace();
		}

		return hashresult;
	}

	/*
	 * makeSafeSalt return a new secure random salt
	 * 
	 * @return the new salt
	 */
	public byte[] makeSafeSalt() {
		byte[] salt = null;
		
		// Generate random salt
		try {
		SecureRandom random = new SecureRandom();
		salt = new byte[16];
		random.nextBytes(salt);
		}
		catch(Exception e) {
			System.out.println("makeSafeSalt failed");
			e.printStackTrace();
		}

		return salt;
	}

	
	//TODO move setEmployeeSalt to the "Create users" Dao
	/*
	 * setEmployeeSalt Update to give an employee a secure salt in database
	 * 
	 * @param employee_id the employee getting the new salt
	 * 
	 * @param esalt the new salt
	 */
	public void setEmployeeSalt(int employee_id, byte[] esalt) {

		try {
			// Identify statement parameters
			String sql = "UPDATE j_hueb_employees SET esalt=? WHERE employee_id=?";

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Insert statement parameters
			preparedStatement.setBytes(1, esalt);
			preparedStatement.setInt(2, employee_id);

			// Execute statement
			preparedStatement.executeUpdate();

			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("setEmployeeSalt failed");
		}

	}

	/*
	 * setEmployeePassword Update to set the employee password in database
	 * 
	 * @param unhashedpassword the password to be hashed
	 * 
	 * @param employee_id the employee getting the new password
	 * 
	 * @param esalt the employee's salt to be added to the password
	 */

	public void setEmployeePassword(String unhashedpassword, int employee_id, byte[] esalt) {

		try {
			// Identify statement parameters
			String sql = "UPDATE j_hueb_employees SET epassword=? WHERE employee_id=?";

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Generate the hashed password
			byte[] epassword = getHash(unhashedpassword, esalt);

			// Insert statement parameters
			preparedStatement.setBytes(1, epassword);
			preparedStatement.setInt(2, employee_id);

			// Execute statement
			preparedStatement.executeUpdate();

			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("setEmployeePassword failed");
		}

	}
	
	/*
	 * getAdminObject returns all of the employee's parameters if it exists
	 * 
	 * @Param employee_id the employee ID to query for
	 * 
	 * @return admin the admin object
	 */
	public Admins getAdminObject(int employee_id) {
		ResultSet result = null;
		Admins admin = null;

		try {
			// Identify statement parameters
			String sql = "SELECT * FROM j_hueb_employees WHERE employee_id=?";

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Insert statement parameters
			preparedStatement.setInt(1, employee_id);

			// Execute statement
			result = preparedStatement.executeQuery();

			// Retrieve all columns of the first result
			result.next();
			String resultName = result.getString("ename");
			String resultLocation = result.getString("elocation");
			String resultUsername= result.getString("eusername");

			// insert database values into admin object
			admin = new Admins(employee_id, resultName, resultLocation, resultUsername);
			
			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getAdminObject failed.");
		}
		return admin;
	}
	
	/*
	 * getUserObject returns all of the employee's parameters if it exists
	 * 
	 * @Param employee_id the employee ID to query for
	 * 
	 * @return user the user object
	 */
	public Users getUserObject(int employee_id) {
		ResultSet result = null;
		Users user = null;

		try {
			// Identify statement parameters
			String sql = "SELECT * FROM j_hueb_employees WHERE employee_id=?";

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Insert statement parameters
			preparedStatement.setInt(1, employee_id);

			// Execute statement
			result = preparedStatement.executeQuery();

			// Retrieve all columns of the first result
			result.next();
			String resultName = result.getString("ename");
			String resultLocation = result.getString("elocation");
			String resultUsername= result.getString("eusername");

			// insert database values into admin object
			user = new Users(employee_id, resultName, resultLocation, resultUsername);
			
			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getUserObject failed.");
		}
		return user;
	}
	
	/*
	 * checkUsername tests if username exists or not
	 * 
	 * @Param eusername username to check for
	 * 
	 * @Return answer answer if it exists or not
	 * 
	 */
	public boolean checkAdmin(int employee_id) {
		boolean answer = false;

		// Get Username from database
		String roleresult = getRole(employee_id);

		// Compare Username and return answer
		if (roleresult.equals("admin")) {
			answer = true;
		} else {
			answer = false;
		}
		return answer;
	}
	
	/*
	 * getRole returns role of employee_id
	 * 
	 * @Param employee_id the employee id of the user
	 * 
	 * @Return roleresult the role of the user
	 * 
	 */
	public String getRole(int employee_id) {
		ResultSet result = null;
		String roleresult = "";

		try {
			// Identify statement parameters
			String sql = "SELECT r.rname FROM j_hueb_roles AS r INNER JOIN j_hueb_userroles AS u ON r.role_id = u.role_id WHERE u.employee_id=?";

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Insert statement parameters
			preparedStatement.setInt(1, employee_id);

			// Execute statement
			result = preparedStatement.executeQuery();

			// Retrieve first username row
			result.next();
			roleresult = result.getString("rname");

			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getRole failed.");
		}
		return roleresult;
	}

}
