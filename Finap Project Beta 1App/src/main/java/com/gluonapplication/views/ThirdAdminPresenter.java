package com.gluonapplication.views;

import com.gluonapplication.GluonApplication;
import com.gluonhq.charm.glisten.animation.FadeInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;

public class ThirdAdminPresenter {

	// Values for form
	private String roleValue;
	private Dialog<String> loadingDialog;

	@FXML
	private View thirdAdmin;

	@FXML
	private TextField fullnameField;

	@FXML
	private TextField locationField;

	@FXML
	private TextField managerField;

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private TextField companyField;

	@FXML
	private TextField departmentField;

	@FXML
	private MenuButton roleMenuButton;

	@FXML
	private MenuItem userRole;

	@FXML
	private MenuItem adminRole;

	@FXML
	private Button submitButton;

	@FXML
	private Label errorLabel;

	public void initialize() {
		thirdAdmin.setShowTransitionFactory(v -> new FadeInRightTransition(v));

		thirdAdmin.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK
						.button(e -> MobileApplication.getInstance().switchView("Primary Admin View")));
				appBar.setTitleText("Create New Employee");
                appBar.getActionItems().add(MaterialDesignIcon.LOCAL_PLAY.button(e -> 
                MobileApplication.getInstance().switchView("Secondary Admin View")));
                appBar.getActionItems().add(MaterialDesignIcon.PERSON.button(e -> 
                MobileApplication.getInstance().switchView("Sixth Admin View")));
                appBar.getActionItems().add(MaterialDesignIcon.PERSON_ADD.button(e -> 
                MobileApplication.getInstance().switchView("Third Admin View")));
			}

			// Clear out entered fields
			fullnameField.setText("");
			locationField.setText("");
			managerField.setText("");
			usernameField.setText("");
			passwordField.setText("");
			companyField.setText("");
			departmentField.setText("");
			roleMenuButton.setText("Role");
			roleValue = "user";
			errorLabel.setText("");
			submitButton.setDisable(false);

		});

		thirdAdmin.setOnShown(e -> {

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
				// Code
				// continues if there are no empty fields
				if (!checkForEmptyFields()) {
					startEmployeeSubmission();
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
		boolean f2 = locationField.getText() == "";
		boolean f3 = managerField.getText() == "";
		boolean f4 = usernameField.getText() == "";
		boolean f5 = passwordField.getText() == "";
		boolean f6 = companyField.getText() == "";
		boolean f7 = departmentField.getText() == "";
		boolean f8 = roleValue == "";

		if (f1 || f2 || f3 || f4 || f5 || f6 || f7 || f8) {
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
		submitButton.setText("Submit");
		submitButton.setDisable(false);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Returns to home view
				MobileApplication.getInstance().switchView("Primary Admin View");

			}
		});
	}

	/*
	 * startEmployeeSubmission starts the process of creating an employee
	 * 
	 */
	void startEmployeeSubmission() {
		submitButton.setText("Creating employee...");

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				createEmployee();
			}
		});

	}

	/*
	 * submitTicket attempts to submit a ticket using the current fields in the
	 * form.
	 */
	void createEmployee() {
		// Retrieves current form fields
		String fullname = fullnameField.getText();
		String location = locationField.getText();
		String username = usernameField.getText();
		String password = passwordField.getText();
		String company = companyField.getText();
		String department = departmentField.getText();
		String manager = managerField.getText();
		errorLabel.setText("");
		
		int role_id = 1;
		if (roleValue.equals("admin")) {
			role_id = 2;
		}

		// Attempts to submit create employee in database
		try {

			// Create employee SQL statement
			System.out.println("Current Admin: " + GluonApplication.currentAdmin);
			GluonApplication.currentAdmin.createDbUser(fullname, location, username, password, company, department,
					manager, role_id);

			// Updates dialog to reflect successful submission
			submissionSuccesful();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("createEmployee failed.");
			errorLabel.setText("Employee creation failed. Employee was not created in the database.");

			// Undisables the submit box
			submitButton.setText("Submit");
			submitButton.setDisable(false);
		}

	}

	/*
	 * BEGIN functionality for role menubutton
	 */
	// sets to low priority
	@FXML
	void userRoleClick() {
		roleValue = "user";
		roleMenuButton.setText("User");
	}

	// sets to normal priority
	@FXML
	void adminRoleClick() {
		roleValue = "admin";
		roleMenuButton.setText("Admin");
	}

}