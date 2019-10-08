package com.gluonapplication.views;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.gluonapplication.GluonApplication;
import com.gluonhq.charm.glisten.animation.FadeInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CardCell;
import com.gluonhq.charm.glisten.control.CardPane;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import huebdesk.items.Notes;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class FifthPresenter {

	// Values for form
	private Dialog<String> createNoteDialog;

	@FXML
	private View fifth;

	@FXML
	private CardPane<Notes> cardPane1;

	public void initialize() {

		fifth.setShowTransitionFactory(v -> new FadeInRightTransition(v));

		// Floating action button
		FloatingActionButton fab = new FloatingActionButton(MaterialDesignIcon.CREATE.text, e -> {
			openNoteCreationMenu();
		});
		fab.showOn(fifth);
		

		fifth.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
					MobileApplication.getInstance().switchToPreviousView();
				}));

				appBar.setTitleText("View Messages");
				
			}
			
			// Set placeholder for note list
			cardPane1.setPlaceholder(new Label("Loading..."));
		});
		
		// Loads ticket list contents on load
		fifth.setOnShown(e -> {
			// Create note list
			createList();
		});
		
		fifth.setOnHidden(g -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cardPane1.getItems().clear();
				}
			});
		});

	}

	/*
	 * openNoteCreationMenu
	 */
	@FXML
	void openNoteCreationMenu() {
		// Create the Dialog box and it's contents
		createNoteDialog = new Dialog<String>(true);
		createNoteDialog.setTitle(new Label("Create New Message"));

		// Creates form to be placed the Dialog box
		VBox wrapper = new VBox();
		wrapper.setAlignment(Pos.TOP_CENTER);
		wrapper.setStyle("-fx-background-color: #e5e5e5;");
		VBox notebox = new VBox();
		notebox.setStyle("-fx-background-color: #ffffff;");
		notebox.setPadding(new Insets(16.0, 16.0, 16.0, 16.0));
		notebox.setSpacing(16);
		notebox.setAlignment(Pos.TOP_LEFT);
		notebox.setMaxWidth(960);
		TextField emailcc = new TextField();
		TextField emailbcc = new TextField();
		emailcc.setFloatText("Email CC");
		emailbcc.setFloatText("Email BCC");
		emailcc.prefWidth(200.0);
		emailbcc.prefWidth(200.0);
		TextArea notebody = new TextArea();
		notebody.prefHeight(200.0);
		notebody.prefWidth(200.0);
		notebody.setPromptText("Message");
		notebody.setWrapText(true);
		Label statusLabel = new Label(" ");
		statusLabel.setWrapText(true);

		// Creates "create" and "cancel" button
		Button createButton = new Button("Create");

		createButton.setOnAction(e -> {
			statusLabel.setText("Creating message...");
			createNote(emailcc.getText(), emailbcc.getText(), notebody.getText());
			createNoteDialog.hide();
		});

		// Adds all form items to VBox
		notebox.getChildren().addAll(emailcc, emailbcc, notebody, statusLabel);
		wrapper.getChildren().addAll(notebox);

		// Adds VBox to Dialog
		createNoteDialog.setContent(wrapper);

		// Adds create and Cancel Button to Dialog
		createNoteDialog.getButtons().addAll(createButton);

		// Shows the dialog
		createNoteDialog.showAndWait();
	}

	/*
	 * createNote creates a new note in the database
	 */
	void createNote(String emailcc, String emailbcc, String notebody) {
		// does not submit if there is no text in the message
		if (!notebody.equals("")) {
			// TODO: Create database snapshot because it is possible for the first
			// query to pass and then next query to fail.

			// Find next avaliable note_id
			int note_id = GluonApplication.currentUser.getDbNoteNextAutoincrement();

			// Create database note
			GluonApplication.currentUser.createDbNote(note_id, new Timestamp(System.currentTimeMillis()),
					new Timestamp(System.currentTimeMillis()), emailcc, emailbcc, notebody,
					GluonApplication.currentUser.getEmployee_id());

			// Wait 5 seconds for creation of note
			try {
				Thread.sleep(5 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Create database note-ticket connection in junction table
			GluonApplication.currentUser.createDbNoteTicket(GluonApplication.currentUserTicket.getTicket_id(), note_id);

		} else {

		}
	}

	/*
	 * createList creates a new note list the database
	 */
	void createList() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				// Load data
				ObservableList<Notes> allnotes = GluonApplication.currentUser.getTicketNotes(GluonApplication.currentUserTicket.getTicket_id());
				if(!allnotes.isEmpty()){
					cardPane1.getItems().setAll(allnotes);
				}
				else {
					cardPane1.setPlaceholder(new Label("This ticket has no notes"));
				}

				// sets list cells
				cardPane1.setCellFactory(p -> new CardCell<Notes>() {

					private final Icon adminavatar;
					private final Icon useravatar;
					{
						useravatar = new Icon(MaterialDesignIcon.ACCOUNT_CIRCLE);
						adminavatar = new Icon(MaterialDesignIcon.ACCOUNT_BOX);
					}

					@Override
					public void updateItem(Notes item, boolean empty) {
						super.updateItem(item, empty);

						// Set cell formatting
						setMaxWidth(Double.MAX_VALUE);
						setMaxHeight(Double.MAX_VALUE);
						setStyle("-fx-padding: 8; -fx-background-color: white");
						// setPrefHeight(100);
						setMinWidth(400);
						setMinHeight(Region.USE_PREF_SIZE);
						setWrapText(true);

						// Code to make notes clickable and usable across views
						// TODO: Create "Delete ticket button"
						/*
						 * setOnMousePressed(e -> { GluonApplication.currentUserNote =
						 * GluonApplication.currentUser.getNote(item.getNote_id()); });
						 */

						// try setting cell bodys
						try {
							if (item != null && !empty) {

								// Load variables
								// final int noteIDValue = item.getNote_id();
								final Timestamp creationValue = item.getNote_creation_date();
								final Timestamp modifiedValue = item.getNote_modified_date();
								// final String emailCcValue = item.getnemail_cc();
								// final String emailBccValue = item.getnemail_bcc();
								final String bodyValue = item.getBody();
								final int authorIdValue = item.getNauthor_employee_id();
								final String authorname = GluonApplication.currentUser.getAuthorName(authorIdValue);
								SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
								final String creation = dateformat.format(creationValue);
								final String modified = dateformat.format(modifiedValue);

								// List tile
								ListTile tile = new ListTile();

								// TODO: display email cc and bcc list
								// Add data from ticket to Card for display
								tile.textProperty().addAll(authorname, "Created: " + creation, "Modified: " + modified);

								tile.setMinWidth(150);
								tile.setPrefSize(100, 100);
								tile.setMaxWidth(Double.MAX_VALUE);
								tile.setMaxHeight(Double.MAX_VALUE);
								tile.setWrapText(true);

								// Add details
								if (item.getNauthor_employee_id() == GluonApplication.currentUser.getEmployee_id()) {
									tile.setPrimaryGraphic(useravatar);
								} else {
									tile.setPrimaryGraphic(adminavatar);
								}
								setGraphic(tile);
								setText(bodyValue);

							} else {
								setGraphic(null);
								setText(null);
							}

						} catch (Exception e) {
							System.out.println("Error creating cards for cardlist");
						}
					}
				});

			}
		});
	}

}
