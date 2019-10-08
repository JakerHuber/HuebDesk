package huebdesk.items;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Jake
 *
 */
public class Users extends Employees {
	// Fields

	// Constructor used for retrieving Users from database
	public Users(int employee_id, String ename, String elocation, String eusername) {
		super(employee_id, ename, elocation, eusername);
	}

	// Methods
	/*
	 * closeDbTicket sets the status of a ticket from the database from open to
	 * closed
	 * 
	 * @param ticket_id the ticket to be closed
	 * 
	 */
	public void closeDbTicket(int ticket_id) {
		try {
			String sql = "UPDATE j_hueb_tickets SET tstatus='closed' WHERE ticket_id=? AND trequester_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, ticket_id);
			preparedStatement.setInt(2, employee_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("closeDbTicket failed");
		}
	}

	/*
	 * removeDbTicket removes a ticket from the database
	 * 
	 * @param ticket_id the ticket to be removed
	 * 
	 */
	public void removeDbTicket(int ticket_id) {
		try {
			String sql = "DELETE FROM j_hueb_tickets WHERE ticket_id=? AND trequester_id=? ";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, ticket_id);
			preparedStatement.setInt(2, employee_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("removeDbTicket failed");
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
			String sql = "DELETE FROM j_hueb_notes WHERE note_id=? AND nauthor_id=? ";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, note_id);
			preparedStatement.setInt(2, employee_id);
			preparedStatement.executeUpdate();
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("removeDbNote failed");
		}
	}

	/*
	 * getAvaliableAdmin gets the employee_id of the admin with the least amount of
	 * troubleshoot tickets.
	 * 
	 * @return avaliable_admin_id the id of the avaliable admin
	 * 
	 */
	public int getAvaliableAdmin() {
		ResultSet resultlist = null;
		int avaliable_admin_id = 0; // defauts to first admin
		try {
			String sql = "SELECT e.employee_id, coalesce(c.totaltickets, 0) AS total_tickets "
					+ "FROM (SELECT r.employee_id FROM j_hueb_userroles AS r INNER JOIN j_hueb_roles AS u ON r.role_id = u.role_id WHERE u.rname='admin') AS e "
					+ "LEFT JOIN (SELECT ttechnician_id, COUNT(*) AS totaltickets FROM `j_hueb_tickets` GROUP BY ttechnician_id ORDER BY totaltickets ASC) AS c "
					+ "ON e.employee_id = c.ttechnician_id ORDER BY total_tickets ASC";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			resultlist = preparedStatement.executeQuery();
			resultlist.next();
			avaliable_admin_id = resultlist.getInt("employee_id");
			conn.connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getAvaliableAdmin failed.");
		}
		return avaliable_admin_id;
	}

	/*
	 * getUsersOpenTickets gets all the tickets that were created by the current
	 * user and have the tstatus= "open"
	 * 
	 * @return observableTicketList the list of tickets made by the user.
	 */
	public ObservableList<Tickets> getUsersOpenTickets() {
		// local variables
		ResultSet results = null;
		ArrayList<Tickets> ticketList = new ArrayList<Tickets>();
		ObservableList<Tickets> observableTicketList = null;

		// attempts sql query
		try {
			String sql = "SELECT * FROM j_hueb_tickets WHERE trequester_id=? AND tstatus='open'";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, this.getEmployee_id());
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
	 * getUsersClosedTickets gets all the tickets that were created by the current
	 * user and have the tstatus= "closed"
	 * 
	 * @return observableTicketList the list of tickets made by the user.
	 */
	public ObservableList<Tickets> getUsersClosedTickets() {
		// local variables
		ResultSet results = null;
		List<Tickets> ticketList = new ArrayList<Tickets>();
		ObservableList<Tickets> observableTicketList = null;

		// attempts sql query
		try {
			String sql = "SELECT * FROM j_hueb_tickets WHERE trequester_id=? AND tstatus='closed'";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, this.getEmployee_id());
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
			System.out.println("getUsersClosedTickets failed.");
		}

		return observableTicketList;
	}

	/*
	 * getUsersAllTickets gets all the tickets that were created by the current user
	 * and have the tstatus= "open" or "closed"
	 * 
	 * @return observableTicketList the list of tickets made by the user.
	 */
	public ObservableList<Tickets> getUsersAllTickets() {
		// local variables
		ResultSet results = null;
		ArrayList<Tickets> ticketList = new ArrayList<Tickets>();
		ObservableList<Tickets> observableTicketList = null;

		// attempts sql query
		try {
			String sql = "SELECT * FROM j_hueb_tickets WHERE trequester_id=?";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, this.getEmployee_id());
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
	 * getTicketNotes gets all the notes that were created by the user for their
	 * ticket.
	 * 
	 * TODO: modify sql statement to only retrieve notes that are related only to
	 * tickets the user submitted.
	 * 
	 * @param ticket_id the ticket being looked-up for matching notes
	 * 
	 * @return observableNotesList the list of notes made by the user.
	 */
	public ObservableList<Notes> getTicketNotes(int ticket_id) {
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
	 * getAuthorName returns the name of the author Is used for displaying notes of
	 * tickets
	 * 
	 * @param author_id the author being looked up
	 * 
	 * @return author_name the author's name
	 * 
	 */
	public String getAuthorName(int author_id) {
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
	 * getNote gets the note of the note_id
	 * 
	 * @param note_id the note being got
	 * 
	 * @return note the note the user wants
	 */
	public Notes getNote(int note_id) {
		// local variables
		ResultSet results = null;
		Notes note = null;

		// attempts sql query
		try {
			String sql = "SELECT * FROM j_hueb_notes WHERE note_id=? AND nauthor_id=? ";
			PreparedStatement preparedStatement = conn.connect().prepareStatement(sql);
			preparedStatement.setInt(1, note_id);
			preparedStatement.setInt(2, employee_id);
			results = preparedStatement.executeQuery();

			// Parse through list
			results.next();

			// grabs values from each row in database table
			int noteIDValue = results.getInt("note_id");
			Timestamp creationValue = results.getTimestamp("ncreation_date");
			Timestamp modifiedValue = results.getTimestamp("nmodified_date");
			String emailCcValue = results.getString("nemail_cc");
			String emailBccValue = results.getString("nemail_bcc");
			String bodyValue = results.getString("nbody");
			int authorIdValue = results.getInt("nauthor_id");

			// enters values from row into new note object
			note = new Notes(noteIDValue, creationValue, modifiedValue, emailCcValue, emailBccValue, bodyValue,
					authorIdValue);

			// close connection
			conn.connect().close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getNote failed.");
		}

		return note;
	}

}
