package com.gluonapplication.views;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

import com.gluonapplication.DrawerManager;
import com.gluonapplication.GluonApplication;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.mvc.View;

import huebdesk.database.DaoLogin;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPresenter {

	// Local Variables
	private static int failedAttempts;
	private static Instant lastinstant;
	private static DaoLogin db;

	@FXML
	private View login;
	
	@FXML
	private Label label;

	@FXML
	private TextField usernameTextField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Button signinButton;

	@FXML
	private Label errorLabel;

	public void initialize() {
		// Remove unsightly buttons

		// Load screen
		login.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setTitleText("Login");

				// Initialize variables
				failedAttempts = 0;
				lastinstant = Instant.now();
				db = new DaoLogin();

				// Clear fields
				usernameTextField.setText("");
				passwordField.setText("");
				errorLabel.setText("");
				label.setText("Sign in with your work account");
				signinButton.setDisable(false);

				// Reset session variables
				GluonApplication.currentUserTicket = null;
				GluonApplication.currentUser = null;
				GluonApplication.currentAdmin = null;
			}
		});

	}

	// START Concurrency

	// END concurrency

	@FXML
	void loginButtonClick() {
		signinButton.setText("Signing in...");
		signinButton.setDisable(true);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				login();
			}
		});
	}

	/*
	 * UsernameFieldonEnter moves user cursor to passwordfield when "enter" is
	 * pressed
	 */
	@FXML
	public void UsernameFieldonEnter(ActionEvent ae) {
		passwordField.requestFocus();
	}

	/*
	 * PasswordFieldonEnter Attempts to login when "enter" key is pressed
	 */
	@FXML
	public void PasswordFieldonEnter(ActionEvent ae) {
		loginButtonClick();
	}

	/*
	 * login Attempts to sign in
	 */
	void login() {
		// Retrieve and store username and passsword from Textfield
		String username = usernameTextField.getText();
		String password = passwordField.getText();

		// Checks if username is correct
		if (db.checkUsername(username)) {
			// Checks if password is correct and continues if true
			if (db.checkPassword(username, password)) {
				loginSuccess(username);
			} else {
				loginFailed();
			}
		} else {
			loginFailed();
		}

	}

	/*
	 * loginSuccess runs on successful login
	 * 
	 * @param username of successfully authenticated user
	 */
	void loginSuccess(String username) {
		signinButton.setText("Signed in");
		signinButton.setDisable(true);
		label.setText("Sign in successful. Please wait one moment...");

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Code
				// Reset fields
				failedAttempts = 0;

				// Get full user info to be used for whole session
				int currentEmployeeID = db.getID(username);

				// Check if the current user is an admin, and open either user or admin view
				if (db.checkAdmin(currentEmployeeID)) {
					GluonApplication.currentAdmin = db.getAdminObject(currentEmployeeID);// Create Admin object
					GluonApplication.adminview = true; // Enable admin drawer
					DrawerManager.buildDrawer(GluonApplication.getInstance()); // refresh drawer
					MobileApplication.getInstance().switchView("Primary Admin View");
				} else {
					GluonApplication.currentUser = db.getUserObject(currentEmployeeID);// Create User object
					GluonApplication.adminview = false; // Enable user drawer
					DrawerManager.buildDrawer(GluonApplication.getInstance()); // refresh drawer
					MobileApplication.getInstance().switchView("Primary View");
				}
			}
		});

	}

	/*
	 * loginFailed runs on failed login
	 */
	void loginFailed() {
		// Prompt user that the password is incorrect
		signinButton.setDisable(false);
		signinButton.setText("Sign in");
		errorLabel.setText(
				"Your account or password is incorrect. If you don't remember your password, contact your Administrator.");
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Code
				// Test for multiple unsuccessful attempts
				Instant instant = Instant.now();
				Duration duration = Duration.between(lastinstant, instant);
				lastinstant = instant;
				if (duration.getSeconds() < 60) {
					failedAttempts++;
				} else {
					failedAttempts = 0;
				}

				// Disables login after 6 consecutive unsuccessful attempts
				if (failedAttempts >= 6) {
					disableLogin();
					Timer timer = new Timer();
					timer.schedule(new Cooldown(), 60 * 1000); // Wait time in mili | 60 = seconds
					errorLabel.setText(
							"Too many unsuccessful sign in attempts. Sign in has been disabled temporarily. If you don't remember your password, contact your Administrator.");
				}
			}
		});
		
	}

	/*
	 * Disables Login Screen
	 */
	void disableLogin() {
		usernameTextField.setText(null);
		passwordField.setText(null);
		usernameTextField.setDisable(true);
		passwordField.setDisable(true);
	}

	/*
	 * Enables Login Screen
	 */
	void enableLogin() {
		usernameTextField.setText(null);
		passwordField.setText(null);
		usernameTextField.setDisable(false);
		passwordField.setDisable(false);
	}

	/*
	 * Class for timer
	 */
	class Cooldown extends TimerTask {

		@Override
		public void run() {
			enableLogin();
		}

	}

}
