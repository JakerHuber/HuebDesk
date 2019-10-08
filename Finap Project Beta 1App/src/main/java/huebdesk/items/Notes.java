package huebdesk.items;

import java.sql.Timestamp;
/**
 * @author Jake
 *
 */
public class Notes {
	// Fields
	private int note_id;
	private Timestamp note_creation_date;
	private Timestamp note_modified_date;
	private String nemail_cc; 
	private String nemail_bcc; 
	private String body;
	private int nauthor_employee_id;
	
	/*
	 * CREATE TABLE IF NOT EXISTS j_hueb_notes (
	 * 
	 * note_id INTEGER(8)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	 * 
	 * note_creation_date Timestamp,
	 * 
	 * note_modified_date Timestamp,
	 * 
	 * nemail_cc VARCHAR(320),
	 * 
	 * nemail_bcc VARCHAR(320),
	 * 
	 * body VARCHAR(800),
	 * 
	 * nauthor_employee_id INTEGER(8) ,
	 * FOREIGN KEY (nauthor_employee_id)
	 * REFERENCES j_hueb_employees (employee_id)
	 * 
	 * )
	 * 
	 */

	// Constructor
	public Notes(int note_id, Timestamp note_creation_date, Timestamp note_modified_date,
			String nemail_cc, String nemail_bcc, String body, int nauthor_employee_id) {
		super();
		this.note_id = note_id;
		this.note_creation_date = note_creation_date;
		this.note_modified_date = note_modified_date;
		this.nemail_cc = nemail_cc;
		this.nemail_bcc = nemail_bcc;
		this.body = body;
		this.nauthor_employee_id = nauthor_employee_id;
	}

	// Getters and setters
	public int getNote_id() {
		return note_id;
	}

	public void setNote_id(int note_id) {
		this.note_id = note_id;
	}

	public Timestamp getNote_creation_date() {
		return note_creation_date;
	}

	public void setNote_creation_date(Timestamp note_creation_date) {
		this.note_creation_date = note_creation_date;
	}

	public Timestamp getNote_modified_date() {
		return note_modified_date;
	}

	public void setNote_modified_date(Timestamp note_modified_date) {
		this.note_modified_date = note_modified_date;
	}

	public String getnemail_cc() {
		return nemail_cc;
	}

	public void setnemail_cc(String nemail_cc) {
		this.nemail_cc = nemail_cc;
	}

	public String getnemail_bcc() {
		return nemail_bcc;
	}

	public void setnemail_bcc(String nemail_bcc) {
		this.nemail_bcc = nemail_bcc;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getNauthor_employee_id() {
		return nauthor_employee_id;
	}

	public void setNauthor_employee_id(int nauthor_employee_id) {
		this.nauthor_employee_id = nauthor_employee_id;
	}

	@Override
	public String toString() {
		return "Notes [note_id=" + note_id + ", note_creation_date=" + note_creation_date + ", note_modified_date="
				+ note_modified_date + ", nemail_cc=" + nemail_cc
				+ ", nemail_bcc=" +nemail_bcc + ", body=" + body + ", nauthor_employee_id="
				+ nauthor_employee_id + "]";
	}

}
