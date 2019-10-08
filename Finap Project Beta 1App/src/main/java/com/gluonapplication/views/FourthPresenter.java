package com.gluonapplication.views;

import com.gluonapplication.GluonApplication;
import com.gluonhq.charm.glisten.animation.FadeInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FourthPresenter {

	@FXML
	private View fourth;

	@FXML
	private Label titleLabel;

	@FXML
	private TextField statusField;

	@FXML
	private TextField createddateField;

	@FXML
	private TextField fullnameField;

	@FXML
	private TextField emailField;

	@FXML
	private TextField subjectField;

	@FXML
	private TextField priorityField;

	@FXML
	private TextField categoryField;

	@FXML
	private TextArea descriptionTextArea;

	@FXML
	private Button closeTicketButton;

	@FXML
	private Button viewNotesButton;

	@FXML
	private Label errorLabel;

	public void initialize() {

		fourth.setShowTransitionFactory(v -> new FadeInRightTransition(v));

		fourth.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
					MobileApplication.getInstance().switchView("Secondary View");
				}));
				appBar.setTitleText("View Ticket");
				titleLabel.setText("Loading ticket data...");
				errorLabel.setText(" ");

				// clear fields
				statusField.setText(" ");
				createddateField.setText(" ");
				fullnameField.setText(" ");
				emailField.setText(" ");
				subjectField.setText(" ");
				priorityField.setText(" ");
				categoryField.setText(" ");
				descriptionTextArea.setText(" ");

				// Loads ticket list contents on load
				fourth.setOnShown(e -> {
					// Auto fill form on load
					autoFillForm();
				});
			}
		});

	}

	/*
	 * autoFillForm Fills form with current ticket's data
	 */
	@FXML
	void autoFillForm() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				titleLabel.setText("Ticket #" + GluonApplication.currentUserTicket.getTicket_id());
				statusField.setText(GluonApplication.currentUserTicket.gettstatus());
				createddateField.setText("" + GluonApplication.currentUserTicket.getTicket_creation_date());
				fullnameField.setText(GluonApplication.currentUser.getEname());
				emailField.setText(GluonApplication.currentUser.getEusername() + "@huebdesk.com");
				subjectField.setText(GluonApplication.currentUserTicket.gettsubject());
				priorityField.setText(GluonApplication.currentUserTicket.getSeverity());
				categoryField.setText(GluonApplication.currentUserTicket.getttype());
				descriptionTextArea.setText(GluonApplication.currentUserTicket.getDescription());
				if (GluonApplication.currentUserTicket.gettstatus().equals("closed")) {
					closeTicketButton.setDisable(true);
					titleLabel.requestFocus();
				} else {
					closeTicketButton.setDisable(false);
					titleLabel.requestFocus();
				}
			}
		});
	}

	/*
	 * addNoteButtonClick
	 */
	@FXML
	void viewNotesButtonClick() {
		MobileApplication.getInstance().switchView("Fifth View");
	}

	/*
	 * closeTicketButtonClick
	 */
	@FXML
	void closeTicketButtonClick() {
		// Disable close ticket button
		closeTicketButton.setDisable(true);

		// Run ticket closing process
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				startTicketClosing();
			}
		});
	}

	/*
	 * deletionSuccesful runs when the ticket is succesfully deleted
	 * 
	 * 
	 */
	void closingSuccesful() {
		// Keeps close ticket button disabled
		closeTicketButton.setDisable(true);
		closeTicketButton.setText("Closed");
		statusField.setText("closed");
		errorLabel.setText(" ");

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Code to run
			}
		});

	}

	/*
	 * deletionFailed runs when the ticket fails to sumbit and changes the dialog to
	 * reflect it's failiure
	 * 
	 */
	void closingFailed() {
		// Keeps close ticket button disabled
		closeTicketButton.setDisable(false);
		closeTicketButton.setText("Close ticket");
		errorLabel.setText("Error: Unable to close ticket");

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Code to run
			}
		});
	}

	/*
	 * startTicketClosingcreate updates the close button's text and attempts to
	 * close the ticket.
	 * 
	 */
	void startTicketClosing() {
		closeTicketButton.setText("Closing...");
		errorLabel.setText(" ");
		// Run ticket closing process
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				closeTicket();
			}
		});
	}

	/*
	 * closeTicket attempts to close a ticket form.
	 */
	void closeTicket() {
		// Attempts to submit ticket to database
		try {
			// closes the ticket with sql
			GluonApplication.currentUser.closeDbTicket(GluonApplication.currentUserTicket.getTicket_id());

			// Updates dialog to reflect successful submission
			closingSuccesful();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("closeTicket failed.");

			// Updates dialog to reflect successful submission
			closingFailed();
		}

	}

}
