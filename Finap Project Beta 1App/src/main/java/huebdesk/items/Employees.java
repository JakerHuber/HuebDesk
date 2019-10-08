package huebdesk.items;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;

import huebdesk.database.DbConnect;

/**
 * @author Jake
 *
 */
public class Employees {
	// Fields
	protected static DbConnect conn = null;
	protected int employee_id;
	protected String ename;
	protected String elocation; 
	protected String eusername; 
	protected String ecompany;
	protected String edepartment;
	protected String emanager;

	/*
	 * CREATE TABLE IF NOT EXISTS j_hueb_employees (
	 * 
	 * employee_id INTEGER(8) NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	 * 
	 * ename VARCHAR(255), 
	 * 
	 * elocation VARCHAR(255), 
	 * 
	 * eusername VARCHAR(32), 
	 * 
	 * epassword BINARY(32), 
	 * 
	 * esalt BINARY(16), 
	 * 
	 * )
	 * 
	*/
	
	// Constructor used for retrieving employee from database
	public Employees(int employee_id, String ename, String elocation, String eusername, String ecompany, String edepartment, String emanager) {
		this.employee_id = employee_id;
		this.ename = ename;
		this.elocation = elocation;
		this.eusername = eusername;
		this.ecompany = ecompany;
		this.edepartment = edepartment;
		this.emanager = emanager;
		conn = new DbConnect();
	}
	
	// Alternate Constructor for displaying employee info
	public Employees(int employee_id, String ename, String elocation, String eusername) {
		this.employee_id = employee_id;
		this.ename = ename;
		this.elocation = elocation;
		this.eusername = eusername;
		conn = new DbConnect();
	}
	
	// Methods
	
	/*
	 * createDbDepartment adds a department to the database
	 * 
	 */
	public void createDbTicket(String tseverity, Timestamp tcreation_date, Timestamp tmodified_date, 
			String tsubject, String tdescription, int trequester_id, int ttechnician_id, Timestamp tdue_date, String tstatus, String ttype, int tlevel) {
		try {
			String columns = "(tseverity, tcreation_date, tmodified_date, tsubject, tdescription, trequester_id, ttechnician_id, tdue_date, tstatus, ttype, tlevel)";
			String values = "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			String sql = "INSERT INTO j_hueb_tickets " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setString(1, tseverity);
			preparedStatement.setTimestamp(2, tcreation_date);
			preparedStatement.setTimestamp(3, tmodified_date);
			preparedStatement.setString(4, tsubject);
			preparedStatement.setString(5, tdescription);
			preparedStatement.setInt(6, trequester_id);
			preparedStatement.setInt(7, ttechnician_id);
			preparedStatement.setTimestamp(8, tdue_date);
			preparedStatement.setString(9, tstatus);
			preparedStatement.setString(10, ttype);
			preparedStatement.setInt(11, tlevel);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("createDbTicket failed");
		}
	}
	
	/*
	 * createDbNote adds a note to the database
	 * 
	 */ 
	public void createDbNote(int note_id, Timestamp ncreation_date, Timestamp nmodified_date, String nemail_cc, String nemail_bcc,
			String nbody, int nauthor_id) {
		try {
			String columns = "(note_id, ncreation_date, nmodified_date, nemail_cc, nemail_bcc, nbody, nauthor_id)";
			String values = "(?, ?, ?, ?, ?, ?, ?)";
			String sql = "INSERT INTO j_hueb_notes " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, note_id);
			preparedStatement.setTimestamp(2, ncreation_date);
			preparedStatement.setTimestamp(3, nmodified_date);
			preparedStatement.setString(4, nemail_cc);
			preparedStatement.setString(5, nemail_bcc);
			preparedStatement.setString(6, nbody);
			preparedStatement.setInt(7, nauthor_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("createDbNote failed");
		}
	}
	
	/*
	 * getDbNoteNextAutoincrement gets the next avaliable note_id for creating a new
	 * note
	 * 
	 * @return auto_increment the next avaliable note_id
	 */
	public int getDbNoteNextAutoincrement() {
		ResultSet result = null;
		int auto_increment = 0;
		try {
			String sql = "SELECT MAX(note_id) FROM j_hueb_notes";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			result = preparedStatement.executeQuery();
			result.next();
			auto_increment = result.getInt(1);
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getDbNoteNextAutoincrement failed.");
		}

		return auto_increment + 1;
	}
	
	/*
	 * createDbNote adds a note to the database
	 * 
	 */ 
	public void createDbNoteTicket(int ticket_id, int note_id) {
		try {
			String columns = "(ticket_id, note_id)";
			String values = "(?, ?)";
			String sql = "INSERT INTO j_hueb_ticketnotes " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, ticket_id);
			preparedStatement.setInt(2, note_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("createDbNoteTicket failed");
		}
	}
	
	/*
	 *
	 * END Database Object Creation  
	 * 
	 * BEGIN Employee Getters and Setters
	 *
	 */
	
	
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
	 * @return salt the new salt
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
	
	/*
	 * refreshFieldsFromDb checks the database fields for the employee
	 * and corrects them in the employee object
	 * 
	 * 
	 */
	public void refreshFieldsFromDb() {
		ResultSet result = null;

		try {
			// Identify statement parameters
			String sql = "SELECT * FROM j_hueb_employees WHERE employee_id=?";

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Insert statement parameters
			preparedStatement.setInt(1, employee_id);

			// Execute statement
			result = preparedStatement.executeQuery();

			// Retrieve fields from first row
			result.next();
			this.ename = result.getString("ename");
			this.elocation = result.getString("elocation");
			this.eusername = result.getString("eusername");

			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("refreshFieldsFromDb failed.");
		}

	}
	
	// Checking password
	/*
	 * checkPassword tests if the password is correct or not
	 * 
	 * @param epassword the unverified password to test
	 * 
	 * @returns answer answers true if the password match
	 */
	public boolean checkPassword(String epassword) {
		boolean answer = false;
		byte[] passwordtoHashed = null;
		
		try {

		// Hash password that the user input
		passwordtoHashed = getHash(epassword, getEsalt());

		// Get Hashed password from database
		byte[] passwordresult = getepassword();

		// Compare both hashed passwords
		if (Arrays.equals(passwordtoHashed, passwordresult)) {
			answer = true;
		} else {
			answer = false;
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("checkPassword failed.");
		}

		return answer;
	}

	/*
	 * Getters and setters
	 * WARNING: All setters assume the input is in the correct format. Please use wisely. 
	 * 
	 * 
	 * 
	*/  
	
	public int getEmployee_id() {
		return employee_id;
	}
	
	/**
	 * @return the ecompany
	 */
	public String getEcompany() {
		return ecompany;
	}

	/**
	 * @param ecompany the ecompany to set
	 */
	public void setEcompany(String ecompany) {
		this.ecompany = ecompany;
	}

	/**
	 * @return the edepartment
	 */
	public String getEdepartment() {
		return edepartment;
	}

	/**
	 * @param edepartment the edepartment to set
	 */
	public void setEdepartment(String edepartment) {
		this.edepartment = edepartment;
	}

	/**
	 * @return the emanager
	 */
	public String getEmanager() {
		return emanager;
	}

	/**
	 * @param emanager the emanager to set
	 */
	public void setEmanager(String emanager) {
		this.emanager = emanager;
	}

	/**
	 * @param employee_id the employee_id to set
	 */
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	public String getEname() {
		return ename;	
	}

	public void setEname(String ename) {
		try {
			this.ename = ename;
			String sql = "UPDATE j_hueb_employees SET ename=? WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setString(1, ename);
			preparedStatement.setInt(2, getEmployee_id());
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("setEname failed");
		}
	}

	public String getElocation() {
		return elocation;
	}

	public void setElocation(String elocation) {
		try {
			this.elocation = elocation;
			String sql = "UPDATE j_hueb_employees SET elocation=? WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setString(1, elocation);
			preparedStatement.setInt(2, getEmployee_id());
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("setElocation failed");
		}
	}
	
	public String getEusername() {
		return eusername;
	}

	public void setEusername(String eusername) {
		try {
			this.eusername = eusername;
			String sql = "UPDATE j_hueb_employees SET eusername=? WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setString(1, eusername);
			preparedStatement.setInt(2, getEmployee_id());
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("setEusername failed");
		}
	}

	public byte[] getepassword() {
		ResultSet result = null;
		byte[] epassword = null;
		try {
			String sql = "SELECT epassword FROM j_hueb_employees WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, getEmployee_id());
			result = preparedStatement.executeQuery();
			result.next();
			epassword = result.getBytes("epassword");
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getepassword failed.");
		}

		return epassword;
	}

	public void setepassword(byte[] epassword) {
		try {
			String sql = "UPDATE j_hueb_employees SET epassword=? WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql); 
			preparedStatement.setBytes(1, epassword); 
			preparedStatement.setInt(2, getEmployee_id());
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("setepassword failed");
		}
	}
	
	public void setepassword(String unhashedpassword) {
		try {
			String sql = "UPDATE j_hueb_employees SET epassword=? WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			byte[] epassword = getHash(unhashedpassword, getEsalt());
			preparedStatement.setBytes(1, epassword);
			preparedStatement.setInt(2, getEmployee_id());
			preparedStatement.executeUpdate();
			conn.connect().close();
			setepassword(epassword);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("setepassword failed");
		}
	}
	
	public byte[] getEsalt() {
		ResultSet result = null;
		byte[] esalt = null;
		try {
			String sql = "SELECT esalt FROM j_hueb_employees WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, getEmployee_id());
			result = preparedStatement.executeQuery();
			result.next();
			esalt = result.getBytes("esalt");
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getSalt failed.");
		}
		return esalt;
	}

	public void setEsalt(byte[] esalt) {
		try {
			String sql = "UPDATE j_hueb_employees SET esalt=? WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql); 
			preparedStatement.setBytes(1, esalt); 
			preparedStatement.setInt(2, getEmployee_id());
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("setEsalt failed");
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Employees [employee_id=" + employee_id + ", ename=" + ename + ", elocation=" + elocation
				+ ", eusername=" + eusername + ", epassword=" + "]";
	}
	
	

}
