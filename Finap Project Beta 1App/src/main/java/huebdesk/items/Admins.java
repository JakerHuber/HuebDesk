package huebdesk.items;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Jake
 *
 */
public class Admins extends Employees {
	// Fields

	// Constructor used for retrieving Admins from database
	public Admins(int employee_id, String ename, String elocation, String eusername) {
		super(employee_id, ename, elocation, eusername);
	}

	// Methods
	// preparedStatement.setNull(2, java.sql.Types.INTEGER);

	/*
	 * CreateDbUser creates a new user in the database. This is the method that
	 * should be used to create new users. This method creates an entry in the
	 * employees, employeemanagers, employeeDepartment, and userroles tables.
	 * 
	 */
	
	
	public void createDbUser(String ename, String elocation, String eusername, String unhashedpassword, String ecompany, String edepartment, String emanager, int role_id) {
		// Attempts to make database entries for new user
		try {
			byte[] esalt = makeSafeSalt();
			byte[] epassword = getHash(unhashedpassword, esalt);
			createDbEmployee(ename, elocation, eusername, epassword, esalt, ecompany, edepartment, emanager);
			Thread.sleep(6 * 1000); // waits for Employee to be created
			int employee_id = getID(ename); // gets auto_assigned id
			createDbUserRole(employee_id, role_id); // sets user role
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("createDbUser failed");
		}

	}

	/*
	 * createDbEmployee creates a new employee in the database
	 * 
	 */
	public void createDbEmployee(String ename, String elocation, String eusername, byte[] epassword, byte[] esalt, String ecompany, String edepartment, String emanager) {
		try {
			// Identify statement parameters
			String columns = "(ename, elocation, eusername, epassword, esalt, ecompany, edepartment, emanager)";
			String values = "(?, ?, ?, ?, ?, ?, ?, ?)";
			String sql = "INSERT INTO j_hueb_employees " + columns + " VALUES " + values;

			// Create Prepared Statement
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);

			// Insert statement parameters
			preparedStatement.setString(1, ename);
			preparedStatement.setString(2, elocation);
			preparedStatement.setString(3, eusername);
			preparedStatement.setBytes(4, epassword);
			preparedStatement.setBytes(5, esalt);
			preparedStatement.setString(6, ecompany);
			preparedStatement.setString(7, edepartment);
			preparedStatement.setString(8, emanager);

			// Execute statement
			preparedStatement.executeUpdate();

			// Close DB connection
			conn.connect().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("createDbEmployee failed");
		}
	}

	/*
	 * createDbUserRole creates a new userrole row
	 * 
	 * @Param employee_id the employee being assigned a role
	 * 
	 * @Param role_id the role_id of the employee
	 */
	public void createDbUserRole(int employee_id, int role_id) {
		try {
			String columns = "(employee_id, role_id)";
			String values = "(?, ?)";
			String sql = "INSERT INTO j_hueb_userroles " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, employee_id);
			preparedStatement.setInt(2, role_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("createDbUserRole failed");
		}
	}

	/*
	 * createDbEmployeeManager creates a new employeemanager row
	 * 
	 * @Param employee_id the employee being assigned a manager
	 * 
	 * @Param manager_id the manager of the employee
	 */
	public void createDbEmployeeManager(int employee_id, int manager_id) {
		try {
			String columns = "(employee_id, manager_id)";
			String values = "(?, ?)";
			String sql = "INSERT INTO j_hueb_employeemanagers " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, employee_id);
			
			// Checks if manager is null, if so sends null to database
			if (manager_id != 0) {
				preparedStatement.setInt(2, manager_id);
			} else {
				preparedStatement.setNull(2, java.sql.Types.INTEGER);
			}
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("createDbUserRole failed");
		}
	}

	/*
	 * createDbEmployeeDepartment creates a new employeedepartment row
	 * 
	 * @Param employee_id the employee being assigned a department
	 * 
	 * @Param department_id the department of the employee
	 */
	public void createDbEmployeeDepartment(int employee_id, int department_id) {
		try {
			String columns = "(employee_id, department_id)";
			String values = "(?, ?)";
			String sql = "INSERT INTO j_hueb_employeedepartments " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, employee_id);

			// Checks if department is null, if so sends null to database
			if (department_id != 0) {
				preparedStatement.setInt(2, department_id);
			} else {
				preparedStatement.setNull(2, java.sql.Types.INTEGER);
			}
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("createDbEmployeeDepartment failed");
		}
	}

	/*
	 * createDbEmployeeAsset creates a new employeeasset row
	 * 
	 * @Param employee_id the employee being assigned an asset
	 * 
	 * @Param asset_id the asset of the employee
	 */
	public void createDbEmployeeAsset(int employee_id, int asset_id) {
		try {
			String columns = "(employee_id, asset_id)";
			String values = "(?, ?)";
			String sql = "INSERT INTO j_hueb_employeeassets " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, employee_id);
			
			// Checks if asset is null, does not add it
			if (asset_id != 0) {
				preparedStatement.setInt(2, asset_id);
				preparedStatement.executeUpdate();
			}
		
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("createDbEmployeeAsset failed");
		}
	}
	

	/*
	 * createDbAsset adds an asset to the database
	 * 
	 */
	public void createDbAsset(Timestamp aorder_date, int ainvoice_id, String atype, Double avalue, String avendor, String amodel) {
		try {
			String columns = "(aorder_date, ainvoice_id, atype, avalue, avendor, amodel)";
			String values = "(?, ?, ?, ?, ?, ?)";
			String sql = "INSERT INTO j_hueb_assets " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setTimestamp(1, aorder_date);
			preparedStatement.setInt(2, ainvoice_id);
			preparedStatement.setString(3, atype);
			preparedStatement.setDouble(4, avalue);
			preparedStatement.setString(5, avendor);
			preparedStatement.setString(6, amodel);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("createDbAsset failed");
		}
	}
	
	/*
	 * createDbCompany adds a company to the database
	 * 
	 */
	public void createDbCompany(String cname, int ceo_id, String clocation) {
		try {
			String columns = "(cname, ceo_id, clocation)";
			String values = "(?, ?, ?)";
			String sql = "INSERT INTO j_hueb_companies " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setString(1, cname);
			preparedStatement.setInt(2, ceo_id);
			preparedStatement.setString(3, clocation);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("createDbCompany failed");
		}
	}
	
	/*
	 * createDbDepartment adds a department to the database
	 * 
	 */
	public void createDbDepartment(String dname, int dmanager_id, String dlocation) {
		try {
			String columns = "(dname, dmanager_id, dlocation)";
			String values = "(?, ?, ?)";
			String sql = "INSERT INTO j_hueb_departments " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setString(1, dname);
			preparedStatement.setInt(2, dmanager_id);
			preparedStatement.setString(3, dlocation);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("createDbDepartment failed");
		}
	}
	

	
	/*
	 * 
	 * 
	 * END Database Object Creation
	 * 
	 * 
	 * Start Database Getters, Setters, Adders, Deleters
	 * 
	 */

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
			String sql = "SELECT employee_id FROM j_hueb_employees WHERE eusername=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setString(1, eusername);
			result = preparedStatement.executeQuery();
			result.next();
			idresult = result.getInt("employee_id");
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getID failed.");
		}
		return idresult;
	}

	/*
	 * getDbNextAutoincrement gets the next avaliable employee_id for creating a new
	 * employee
	 * 
	 * @return auto_increment the next avaliable employee_id
	 */
	public int getDbNextAutoincrement() {
		ResultSet result = null;
		int auto_increment = 0;
		try {
			String sql = "SELECT MAX(employee_id) FROM j_hueb_employees";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			result = preparedStatement.executeQuery();
			result.next();
			auto_increment = result.getInt(1);
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getDbNextAutoincrement failed.");
		}

		return auto_increment + 1;
	}

	/*
	 * getManagerID gets the manager_id
	 * 
	 * @param employee_id the employee being looked up
	 * 
	 * @return manager_id the employees manager
	 * 
	 */
	public int getManagerID(int employee_id) {
		ResultSet result = null;
		int manager_id = 0;
		try {
			String sql = "SELECT manager_id FROM j_hueb_employeemanagers WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, employee_id);
			result = preparedStatement.executeQuery();
			result.next();
			manager_id = result.getInt("manager_id");
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getManagerID failed.");
		}
		return manager_id;
	}

	/*
	 * setManagerID sets the manager_id
	 * 
	 * @param employee_id the employee being edited
	 * 
	 * @param manager_id the employees new manager
	 * 
	 */
	public void setManagerID(int employee_id, int manager_id) {
		try {
			String sql = "UPDATE j_hueb_employeemanagers SET manager_id=? WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, manager_id);
			preparedStatement.setInt(2, employee_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("setManagerID failed");
		}
	}

	/*
	 * getManagerName returns the manager's name
	 * 
	 * @param employee_id the manager being looked up
	 * 
	 * @return manager_name the manager's name
	 * 
	 */
	public String getManagerName(int manager_id) {
		ResultSet result = null;
		String manager_ename = "";
		try {
			String sql = "SELECT ename FROM j_hueb_employees WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, manager_id);
			result = preparedStatement.executeQuery();
			result.next();
			manager_ename = result.getString("ename");
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getManagerName failed.");
		}
		return manager_ename;
	}

	/*
	 * getEmployeeDepartmentID gets the department_id of the employee
	 * 
	 * @param employee_id the employee being looked up
	 * 
	 * @return department_id the employee's department
	 * 
	 */
	public int getEmployeeDepartmentID(int employee_id) {
		ResultSet result = null;
		int department_id = 0;
		try {
			String sql = "SELECT department_id FROM j_hueb_employeedepartments WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, employee_id);
			result = preparedStatement.executeQuery();
			result.next();
			department_id = result.getInt("department_id");
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getEmployeeDepartmentID failed.");
		}
		return department_id;
	}

	/*
	 * setEmployeeDepartmentID sets the department_id
	 * 
	 * @param employee_id the employee being edited
	 * 
	 * @param department_id the employees new department
	 * 
	 */
	public void setEmployeeDepartmentID(int employee_id, int department_id) {
		try {
			String sql = "UPDATE j_hueb_employeedepartments SET department_id=? WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, department_id);
			preparedStatement.setInt(2, employee_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("setEmployeeDepartmentID failed");
		}
	}

	/*
	 * getDepartmentName returns the department's name
	 * 
	 * @param department_id the department being looked up
	 * 
	 * @return department_name the department's name
	 * 
	 */
	public String getDepartmentName(int department_id) {
		ResultSet result = null;
		String department_name = "";
		try {
			String sql = "SELECT dname FROM j_hueb_departments WHERE department_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, department_id);
			result = preparedStatement.executeQuery();
			result.next();
			department_name = result.getString("dname");
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getDepartmentName failed.");
		}
		return department_name;
	}

	/*
	 * getEmployeeAssets gets all the assets of the employee
	 * 
	 * @param employee_id the employee being looked up
	 * 
	 * @return resultlist the list of the employee's assets
	 * 
	 */
	public ResultSet getEmployeeAssets(int employee_id) {
		ResultSet resultlist = null;
		try {
			String sql = "SELECT a.* FROM j_hueb_assets AS a INNER JOIN j_hueb_employeeassets AS e ON a.asset_id = e.asset_id WHERE e.employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, employee_id);
			resultlist = preparedStatement.executeQuery();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getEmployeeAssets failed.");
		}
		return resultlist;
	}
	
	/*
	 * getUserEmployeeName gets the user's name
	 * 
	 * @param employee_id the employee being looked up
	 * 
	 * @return manager_id the employees manager
	 * 
	 */
	public String getUserEmployeeName(int employee_id) {
		ResultSet result = null;
		String name = "";
		try {
			String sql = "SELECT ename FROM j_hueb_employees WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, employee_id);
			result = preparedStatement.executeQuery();
			result.next();
			name = result.getString("ename");
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getManagerID failed.");
		}
		return name;
	}

	/*
	 * addEmployeeAsset adds an asset to the employee's list
	 * 
	 * @param employee_id the employee being edited
	 * 
	 * @param asset_id the employee's new asset
	 * 
	 */
	public void addEmployeeAsset(int employee_id, int asset_id) {
		try {
			String columns = "(employee_id, asset_id)";
			String values = "(?, ?)";
			String sql = "INSERT INTO j_hueb_employeeassets " + columns + " VALUES " + values;
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, employee_id);
			
			// Checks if asset is null, does not add it
			if (asset_id != 0) {
				preparedStatement.setInt(2, asset_id);
				preparedStatement.executeUpdate();
			}
		
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("addEmployeeAsset failed");
		}
	}
	
	/*
	 * removeEmployeeAsset removes an asset from the employee's list
	 * 
	 * @param employee_id the employee being edited
	 * 
	 * @param asset_id the employee's removed asset
	 * 
	 */
	public void removeEmployeeAsset(int employee_id, int asset_id) {
		try {
			String sql = "DELETE FROM j_hueb_employeeassets WHERE employee_id=? AND asset_id=? ";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, employee_id);
			preparedStatement.setInt(2, asset_id);
			preparedStatement.executeUpdate();
		
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("removeEmployeeAsset failed");
		}
	}
	
	/*
	 * removeDbTicket removes a ticket from the database
	 * 
	 * @param employee_id the employee removing the ticket
	 * 
	 * @param ticket_id the ticket to be removed
	 * 
	 */
	public void removeDbTicket(int ticket_id) {
		try {
			String sql = "DELETE FROM j_hueb_tickets WHERE ticket_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, ticket_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("removeDbTicket failed");
		}
	}
	
	/*
	 * closeAdminDbTicket sets the status of a ticket from the database from open to
	 * closed
	 * 
	 * @param ticket_id the ticket to be closed
	 * 
	 */
	public void closeAdminDbTicket(int ticket_id) {
		try {
			String sql = "UPDATE j_hueb_tickets SET tstatus='closed' WHERE ticket_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, ticket_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("closeDbTicket failed");
		}
	}
	
	/*
	 * removeDbNote removes a note from the database
	 * 
	 * @param employee_id the employee removing the note
	 * 
	 * @param note_id the note to be removed
	 * 
	 */
	public void removeDbNote(int note_id) {
		try {
			String sql = "DELETE FROM j_hueb_notes WHERE note_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, note_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("removeDbNote failed");
		}
	}
	
	/*
	 * getAdminAllTickets gets all the tickets in the database that are either
	 * "open" or "closed"
	 * 
	 * @return observableTicketList the list of tickets
	 */
	public ObservableList<Tickets> getAdminAllTickets() {
		// local variables
		ResultSet results = null;
		ArrayList<Tickets> ticketList = new ArrayList<Tickets>();
		ObservableList<Tickets> observableTicketList = null;

		// attempts sql query
		try {
			String sql = "SELECT * FROM j_hueb_tickets";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			results = preparedStatement.executeQuery();

			// Parse through list
			while (results.next()) {
				// grabs values from each row in database table
				int ticketIdValue = results.getInt("ticket_id");
				String severityValue = results.getString("tseverity");
				Timestamp creationValue = results.getTimestamp("tcreation_date");
				Timestamp modifiedValue = results.getTimestamp("tmodified_date");
				String subjectValue = results.getString("tsubject");
				String descriptionValue = results.getString("tdescription");
				int requesterIdValue = results.getInt("trequester_id");
				int assignedAdminIdValue = results.getInt("ttechnician_id");
				Timestamp duedateValue = results.getTimestamp("tdue_date");
				String statusValue = results.getString("tstatus");
				String typeValue = results.getString("ttype");
				int levelValue = results.getInt("tlevel");

				// enters values from row into new ticket object
				Tickets ticket = new Tickets(ticketIdValue, severityValue, creationValue, modifiedValue, subjectValue,
						descriptionValue, requesterIdValue, assignedAdminIdValue, duedateValue, statusValue, typeValue,
						levelValue);

				// adds ticket to ticket list
				ticketList.add(ticket);
			}

			// close connection
			conn.connect().close();

			// turn ticket list into obervablelist
			observableTicketList = FXCollections.observableArrayList(ticketList);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getUsersOpenTickets failed.");
		}

		return observableTicketList;
	}

	/*
	 * getAdminTicketNotes gets all the notes that were created the
	 * ticket.
	 * 
	 * TODO: modify sql statement to only retrieve notes that are related only to
	 * tickets the user submitted.
	 * 
	 * @param ticket_id the ticket being looked-up for matching notes
	 * 
	 * @return observableNotesList the list of notes
	 */
	public ObservableList<Notes> getAdminTicketNotes(int ticket_id) {
		// local variables
		ResultSet results = null;
		ArrayList<Notes> notesList = new ArrayList<Notes>();
		ObservableList<Notes> observableNotesList = null;

		// attempts sql query
		try {
			String sql = "SELECT n.* FROM j_hueb_notes AS n INNER JOIN j_hueb_ticketnotes AS t ON t.note_id = n.note_id WHERE t.ticket_id=? ORDER BY n.ncreation_date ASC";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, ticket_id);
			results = preparedStatement.executeQuery();

			// Parse through list
			while (results.next()) {
				// grabs values from each row in database table
				int noteIDValue = results.getInt("note_id");
				Timestamp creationValue = results.getTimestamp("ncreation_date");
				Timestamp modifiedValue = results.getTimestamp("nmodified_date");
				String emailCcValue = results.getString("nemail_cc");
				String emailBccValue = results.getString("nemail_bcc");
				String bodyValue = results.getString("nbody");
				int authorIdValue = results.getInt("nauthor_id");

				// enters values from row into new note object
				Notes note = new Notes(noteIDValue, creationValue, modifiedValue, emailCcValue, emailBccValue,
						bodyValue, authorIdValue);

				// adds note to note list
				notesList.add(note);
			}

			// close connection
			conn.connect().close();

			// turn note list into obervablelist
			observableNotesList = FXCollections.observableArrayList(notesList);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getTicketNotes failed.");
		}

		return observableNotesList;
	}
	
	/*
	 * getAdminAuthorName returns the name of the author of the note
	 * 
	 * @param author_id the author being looked up
	 * 
	 * @return author_name the author's name
	 * 
	 */
	public String getAdminAuthorName(int author_id) {
		ResultSet result = null;
		String author_ename = "";
		try {
			String sql = "SELECT ename FROM j_hueb_employees WHERE employee_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, author_id);
			result = preparedStatement.executeQuery();
			result.next();
			author_ename = result.getString("ename");
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getAuthorName failed.");
		}
		return author_ename;
	}
	
	/*
	 * getAdminAllEmployees gets all the employees in the database
	 * 
	 * @return observableEmployeeList the list of employees
	 */
	public ObservableList<Employees> getAdminAllEmployees() {
		// local variables
		ResultSet results = null;
		ArrayList<Employees> employeeList = new ArrayList<Employees>();
		ObservableList<Employees> observableTicketList = null;

		// attempts sql query
		try {
			String sql = "SELECT * FROM j_hueb_employees";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			results = preparedStatement.executeQuery();

			// Parse through list
			while (results.next()) {
				// grabs values from each row in database table
				int employeeIdValue = results.getInt("employee_id");
				String enameValue = results.getString("ename");
				String elocationValue = results.getString("elocation");
				String eusernameValue = results.getString("eusername");
				String ecompany = results.getString("ecompany");
				String edepartment = results.getString("edepartment");
				String emanager = results.getString("emanager");

				// enters values from row into new ticket object
				Employees employee = new Employees(employeeIdValue, enameValue, elocationValue, eusernameValue, ecompany, edepartment, emanager);

				// adds ticket to ticket list
				employeeList.add(employee);
			}

			// close connection
			conn.connect().close();

			// turn ticket list into obervablelist
			observableTicketList = FXCollections.observableArrayList(employeeList);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getAdminAllEmployees failed.");
		}

		return observableTicketList;
	}

	// OLD (CORRECT) WAY OF CREATING USER
	/*
	 * CreateDbUser creates a new user in the database. This is the method that
	 * should be used to create new users. This method creates an entry in the
	 * employees, employeemanagers, employeeDepartment, and userroles tables.
	 * 
	 */
	//String ecompany, String edepartment, String emanager/
	/*
	public void createDbUser(String ename, String elocation, String eusername, String unhashedpassword, int role_id,
			int manager_id, int department_id, int asset_id) {
		// Attempts to make database entries for new user
		try {
			byte[] esalt = makeSafeSalt();
			byte[] epassword = getHash(unhashedpassword, esalt);
			createDbEmployee(ename, elocation, eusername, epassword, esalt);
			Thread.sleep(5 * 1000); // waits for Employee to be created
			int employee_id = getID(ename); // gets auto_assigned id
			createDbUserRole(employee_id, role_id); // sets user role
			createDbEmployeeManager(employee_id, manager_id);
			createDbEmployeeDepartment(employee_id, department_id);
			createDbEmployeeAsset(employee_id, asset_id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("createDbUser failed");
		}

	}
	*/
	// END OLD (CORREC) WAY OF CREATING DB USER
	
	// Getters and setters

}
