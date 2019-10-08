package huebdesk.items;

import java.sql.Timestamp;

/**
 * @author Jake
 *
 */
public class Tickets {
	// Fields
	private int ticket_id;
	private String severity;
	private Timestamp ticket_creation_date;
	private Timestamp ticket_modified_date;
	private String tsubject;
	private String description;
	private int trequester_employee_id;
	private int tassigned_tech_admin_id;
	private Timestamp due_date;
	private String tstatus;
	private String ttype;
	private int level;
	
	/*
	 * CREATE TABLE IF NOT EXISTS j_hueb_tickets (
	 *  
	 * ticket_id INTEGER(8)  NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	 * 
	 * severity VARCHAR(255),
	 * 
	 * ticket_creation_date TIMESTAMP,
	 * 
	 * ticket_modified_date TIMESTAMP,
	 * 
	 * tsubject VARCHAR(255),
	 * 
	 * description VARCHAR(255),
	 * 
	 * trequester_employee_id INTEGER(8) ,
	 * FOREIGN KEY (trequester_employee_id)
	 * REFERENCES j_hueb_employees (employee_id),
	 * 
	 * tassigned_tech_admin_id INTEGER(8) ,
	 * FOREIGN KEY (tassigned_tech_admin_id)
	 * REFERENCES j_hueb_admins (admin_id),
	 * 
	 * due_date TIMESTAMP,
	 *  
	 * tstatus VARCHAR(255),
	 * 
	 * ttype VARCHAR(255),
	 * 
	 * level INTEGER(1),
	 * 
	 * )
	 * 
	*/
	
	// Constructor
	public Tickets(int ticket_id, String severity, Timestamp ticket_creation_date, Timestamp ticket_modified_date,
			String tsubject, String description, int trequester_employee_id, int tassigned_tech_admin_id,
			Timestamp due_date, String tstatus, String ttype, int level) {
		this.ticket_id = ticket_id;
		this.severity = severity;
		this.ticket_creation_date = ticket_creation_date;
		this.ticket_modified_date = ticket_modified_date;
		this.tsubject = tsubject;
		this.description = description;
		this.trequester_employee_id = trequester_employee_id;
		this.tassigned_tech_admin_id = tassigned_tech_admin_id;
		this.due_date = due_date;
		this.tstatus = tstatus;
		this.ttype = ttype;
		this.level = level;
	}
	
	// Getters and Setters
	public int getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(int ticket_id) {
		this.ticket_id = ticket_id;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public Timestamp getTicket_creation_date() {
		return ticket_creation_date;
	}
	public void setTicket_creation_date(Timestamp ticket_creation_date) {
		this.ticket_creation_date = ticket_creation_date;
	}
	public Timestamp getTicket_modified_date() {
		return ticket_modified_date;
	}
	public void setTicket_modified_date(Timestamp ticket_modified_date) {
		this.ticket_modified_date = ticket_modified_date;
	}
	public String gettsubject() {
		return tsubject;
	}
	public void settsubject(String tsubject) {
		this.tsubject = tsubject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTrequester_employee_id() {
		return trequester_employee_id;
	}
	public void setTrequester_employee_id(int trequester_employee_id) {
		this.trequester_employee_id = trequester_employee_id;
	}
	public int gettassigned_tech_admin_id() {
		return tassigned_tech_admin_id;
	}
	public void settassigned_tech_admin_id(int tassigned_tech_admin_id) {
		this.tassigned_tech_admin_id = tassigned_tech_admin_id;
	}
	public Timestamp getDue_date() {
		return due_date;
	}
	public void setDue_date(Timestamp due_date) {
		this.due_date = due_date;
	}
	public String gettstatus() {
		return tstatus;
	}
	public void settstatus(String tstatus) {
		this.tstatus = tstatus;
	}
	public String getttype() {
		return ttype;
	}
	public void setttype(String ttype) {
		this.ttype = ttype;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tickets [ticket_id=" + ticket_id + ", severity=" + severity + ", ticket_creation_date="
				+ ticket_creation_date + ", ticket_modified_date=" + ticket_modified_date + ", tsubject=" + tsubject
				+ ", description=" + description + ", trequester_employee_id=" + trequester_employee_id
				+ ", tassigned_tech_admin_id=" + tassigned_tech_admin_id + ", due_date=" + due_date + ", tstatus="
				+ tstatus + ", ttype=" + ttype + ", level=" + level + "]";
	}
	
	
	
}
