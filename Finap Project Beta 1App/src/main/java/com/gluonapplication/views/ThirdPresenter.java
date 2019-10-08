package com.gluonapplication.views;

import java.sql.Timestamp;

import com.gluonapplication.GluonApplication;
import com.gluonhq.charm.glisten.animation.FadeInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.control.ProgressIndicator;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

public class ThirdPresenter {

	// Values for form
	private String priorityValue;
	private String categoryValue;
	private Dialog<String> loadingDialog;

	@FXML
	private View third;

	@FXML
	private TextField fullnameField;

	@FXML
	private TextField emailField;

	@FXML
	private TextField subjectField;

	@FXML
	private MenuButton priorityMenuButton;

	@FXML
	private MenuItem lowPriority;

	@FXML
	private MenuItem normalPriority;

	@FXML
	private MenuItem importantPriority;

	@FXML
	private MenuItem criticalPriority;

	@FXML
	private MenuButton categoryMenuButton;

	@FXML
	private MenuItem hardwareCategory;

	@FXML
	private MenuItem softwareCategory;

	@FXML
	private MenuItem serverCategory;

	@FXML
	private MenuItem networkingCategory;

	@FXML
	private MenuItem newemployeeCategory;

	@FXML
	private MenuItem websiteCategory;

	@FXML
	private MenuItem changeCategory;

	@FXML
	private TextArea descriptionTextArea;

	@FXML
	private Button submitButton;

	@FXML
	private Label errorLabel;

	public void initialize() {
		third.setShowTransitionFactory(v -> new FadeInRightTransition(v));

		third.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK
						.button(e -> MobileApplication.getInstance().switchView("Primary View")));
				appBar.setTitleText("Create New Ticket");
				appBar.getActionItems().add(MaterialDesignIcon.LOCAL_PLAY
						.button(e -> MobileApplication.getInstance().switchView("Secondary View")));
				appBar.getActionItems().add(MaterialDesignIcon.BORDER_COLOR
						.button(e -> MobileApplication.getInstance().switchView("Third View")));
			}

			// Clear out entered fields
			fullnameField.setText("");
			emailField.setText("");
			subjectField.setText("");
			priorityMenuButton.setText("Priority");
			categoryMenuButton.setText("Category");
			priorityValue = "low";
			categoryValue = "software";
			descriptionTextArea.setText("");
			errorLabel.setText("");
			submitButton.setDisable(false);

		});

		// Loads ticket list contents on load
		third.setOnShown(e -> {
			// Auto-fill fields
			fullnameField.setText("" + GluonApplication.currentUser.getEname());
			emailField.setText("" + GluonApplication.currentUser.getEusername() + "@huebdesk.com");
		});

	}

	/*
	 * BEGIN functionality for submit button
	 */
	@FXML
	void submitButtonClick() {
			// disable button to prevent sending duplicates
			submitButton.setDisable(true);
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					//Code
					// continues if there are no empty fields
					if (!checkForEmptyFields()) {
						startTicketSubmission();
					} else {
						submitButton.setDisable(false);
						errorLabel.setText("All fields must be filled in order to submit.");
					}
				}
			});
			
	}

	/*
	 * checkForEmptyFields checks for empty fields. Returns true if there are empty
	 * fields
	 * 
	 * @return answer the answer to the question
	 */
	boolean checkForEmptyFields() {
		boolean answer = false;

		boolean f1 = fullnameField.getText() == "";
		boolean f2 = emailField.getText() == "";
		boolean f3 = subjectField.getText() == "";
		boolean f4 = priorityValue == "";
		boolean f5 = categoryValue == "";
		boolean f6 = descriptionTextArea.getText() == "";

		if (f1 || f2 || f3 || f4 || f5 || f6) {
			answer = true;
		} else {
			answer = false;
		}

		return answer;
	}

	/*
	 * submissionSuccesful runs when the ticket is succesfully sumbitted and changed
	 * the dialog to reflect it's success
	 * 
	 */
	void submissionSuccesful() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				// Clear out old text fields
				fullnameField.setText("");
				emailField.setText("");
				subjectField.setText("");
				priorityMenuButton.setText("Priority");
				categoryMenuButton.setText("Category");
				priorityValue = "low";
				categoryValue = "software";
				descriptionTextArea.setText("");
				errorLabel.setText("");
				submitButton.setDisable(false);
				errorLabel.setText("");
				submitButton.setText("Submitted.");
			}
		});
	}

	/*
	 * submissionFailed runs when the ticket fails to sumbit and changes the dialog
	 * to reflect it's failiure
	 * 
	 */
	void submissionFailed() {
		submitButton.setText("Submitted.");
		errorLabel.setText("Sorry, there was an error that prevented your ticket from being submitted");
		submitButton.setDisable(false);
	}

	/*
	 * startTicketSubmission create a loading dialog and then attempts to submit the
	 * ticket. The dialog has three states: processing, success, and failed. The
	 * dialog box initiates the actual ticket submission.
	 * 
	 */
	void startTicketSubmission() {
		submitButton.setText("Submitting...");
		errorLabel.setText("");

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					submitTicket();
				}
			});
	}

	/*
	 * submitTicket attempts to submit a ticket using the current fields in the
	 * form.
	 */
	void submitTicket() {
		// Retrieves current form fields
		String fullname = fullnameField.getText();
		String email = emailField.getText();
		String subject = subjectField.getText();
		String priority = priorityValue.toLowerCase();
		String category = categoryValue.toLowerCase();
		String description = descriptionTextArea.getText();

		// Creates metadata for ticket submission
		Timestamp creationDate = new Timestamp(System.currentTimeMillis());
		Timestamp modifiedDate = new Timestamp(System.currentTimeMillis());
		int requesterId = GluonApplication.currentUser.getEmployee_id();
		int technicianId = GluonApplication.currentUser.getAvaliableAdmin();
		Timestamp duedate = calculateDueDate(priority);
		String status = "open";
		int level = 1;

		// Attempts to submit ticket to database
		try {
			// submits the ticket with sql
			GluonApplication.currentUser.createDbTicket(priority, creationDate, modifiedDate, subject, description,
					requesterId, technicianId, duedate, status, category, level);

			// Updates dialog to reflect successful submission
			submissionSuccesful();

			// Returns to home view
			MobileApplication.getInstance().switchView("Primary View");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("submitTicket failed.");
			errorLabel.setText("Ticked submission failed. Ticket was not created in the database.");

			// Updates dialog to reflect successful submission
			submissionFailed();

			// Undisables the submit box
			submitButton.setDisable(false);
		}

		// TODO: Send an email with the details to the ticket.
		//
		//

	}

	/*
	 * Calcuate calculates the due date of the ticket based off priority
	 * 
	 * @Param the priority of the ticket
	 * 
	 * @return the due date of the ticket
	 */
	Timestamp calculateDueDate(String priority) {
		Timestamp answer = new Timestamp(System.currentTimeMillis());
		switch (priority) {
		case "low":
			// sets duedate to five days from current time
			answer.setTime(answer.getTime() + ((5 * 24 * 60 * 60) * 1000));
			break;
		case "normal":
			// sets duedate to three days from current time
			answer.setTime(answer.getTime() + ((3 * 24 * 60 * 60) * 1000));
			break;
		case "important":
			// sets duedate to 24 hours from current time
			answer.setTime(answer.getTime() + ((24 * 60 * 60) * 1000));
			break;
		case "critical":
			// sets duedate to four hours from current time
			answer.setTime(answer.getTime() + ((4 * 60 * 60) * 1000));
			break;
		default:
			// sets duedate to five days from current time
			answer.setTime(answer.getTime() + ((5 * 24 * 60 * 60) * 1000));
		}
		return answer;
	}

	/*
	 * BEGIN functionality for priority menubutton
	 */
	// sets to low priority
	@FXML
	void lowPriorityClick() {
		priorityValue = "low";
		priorityMenuButton.setText("Low");
	}

	// sets to normal priority
	@FXML
	void normalPriorityClick() {
		priorityValue = "normal";
		priorityMenuButton.setText("Normal");
	}

	// sets to important priority
	@FXML
	void importantPriorityClick() {
		priorityValue = "important";
		priorityMenuButton.setText("Important");
	}

	// sets to critical priority
	@FXML
	void criticalPriorityClick() {
		priorityValue = "critical";
		priorityMenuButton.setText("Critical");
	}

	/*
	 * BEGIN functionality for category menubutton
	 */
	// sets to hardware
	@FXML
	void hardwareCategoryClick() {
		categoryValue = "Hardware";
		categoryMenuButton.setText("Hardware");
	}

	// sets to software
	@FXML
	void softwareCategoryClick() {
		categoryValue = "Software";
		categoryMenuButton.setText("Software");
	}

	// sets to server
	@FXML
	void serverCategoryClick() {
		categoryValue = "server";
		categoryMenuButton.setText("Server");
	}

	// sets to networking
	@FXML
	void networkingCategoryClick() {
		categoryValue = "critical";
		categoryMenuButton.setText("Networking");
	}

	// sets to new employee
	@FXML
	void newemployeeCategoryClick() {
		categoryValue = "new employee";
		categoryMenuButton.setText("New employee");
	}

	// sets to website
	@FXML
	void websiteCategoryClick() {
		categoryValue = "website";
		categoryMenuButton.setText("Website");
	}

	// sets to change request
	@FXML
	void changeCategoryClick() {
		categoryValue = "change request";
		categoryMenuButton.setText("Change request");
	}

}
