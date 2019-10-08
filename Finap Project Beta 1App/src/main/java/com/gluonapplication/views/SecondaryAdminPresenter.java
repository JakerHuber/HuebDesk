package com.gluonapplication.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gluonapplication.GluonApplication;
import com.gluonhq.charm.glisten.animation.FadeInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListCell;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import huebdesk.items.Notes;
import huebdesk.items.Tickets;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class SecondaryAdminPresenter {

	@FXML
	private View secondaryAdmin;

	@FXML
	private CharmListView<Tickets, String> charmlist1;

	public void initialize() {
		secondaryAdmin.setShowTransitionFactory(v -> new FadeInRightTransition(v));

		secondaryAdmin.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK
						.button(e -> MobileApplication.getInstance().switchView("Primary Admin View")));
				appBar.setTitleText("View Tickets");
                appBar.getActionItems().add(MaterialDesignIcon.LOCAL_PLAY.button(e -> 
                MobileApplication.getInstance().switchView("Secondary Admin View")));
                appBar.getActionItems().add(MaterialDesignIcon.PERSON.button(e -> 
                MobileApplication.getInstance().switchView("Sixth Admin View")));
                appBar.getActionItems().add(MaterialDesignIcon.PERSON_ADD.button(e -> 
                MobileApplication.getInstance().switchView("Third Admin View")));
			}
			
			// Set placeholder for ticket list
			charmlist1.setPlaceholder(new Label("Loading..."));
		
		});

		// Loads ticket list contents on load
		secondaryAdmin.setOnShown(e -> {
			createList();
		});		

	}

	/*
	 * Loads all tickets into list
	 */
	void createList() {
		// Reloads stuff
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				// Load data
				ObservableList<Tickets> alltickets = GluonApplication.currentAdmin.getAdminAllTickets();
				if(!alltickets.isEmpty()){
					charmlist1.setItems(alltickets);
				}
				else {
					charmlist1.setPlaceholder(new Label("No tickets avaliable"));
				}

				// Set charm header structure
				charmlist1.setHeadersFunction(Tickets::gettstatus);
				charmlist1.setHeaderComparator((s1, s2) -> s2.compareTo(s1));

				// Set list hearders
				charmlist1.setHeaderCellFactory(p9 -> new CharmListCell<Tickets>() {

					private final Icon open, closed;
					{
						open = new Icon(MaterialDesignIcon.CHECK);
						open.getStyleClass().add("open");
						closed = new Icon(MaterialDesignIcon.CANCEL);
						closed.getStyleClass().add("closed");
					}

					@Override
					public void updateItem(Tickets item, boolean empty) {
						super.updateItem(item, empty);

						// Setting titles for each
						try {
							if (item != null && !empty) {
								final int ticket_id = item.getTicket_id();
								final String status = item.gettstatus();

								// List tile
								ListTile tile = new ListTile();

								if (status.equals("open")) {
									tile.setPrimaryGraphic(open);
									tile.textProperty().addAll("Open", "");
								} else {
									tile.textProperty().addAll("Closed", "");
									tile.setPrimaryGraphic(closed);
								}

								setGraphic(tile);
								setText(null);
							} else {
								setGraphic(null);
								setText(null);
							}
						} catch (Exception e) {
							System.out.println("Error setting header titles");
						}
					}

				});

				// sets list cells
				charmlist1.setCellFactory(p -> new CharmListCell<Tickets>() { // ERROR HERE?

					private final Icon ticketicon;
					{
						ticketicon = new Icon(MaterialDesignIcon.LOCAL_PLAY);
						ticketicon.getStyleClass().add("ticket");
					}

					@Override
					public void updateItem(Tickets item, boolean empty) {
						super.updateItem(item, empty);

						// try setting cell bodys
						try {
							if (item != null && !empty) {
								// Load variables
								SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
								final int ticket_id = item.getTicket_id();
								final String subject = item.gettsubject();
								final String description = item.getDescription();
								final Date creationdate = item.getTicket_creation_date();
								final String creation = dateformat.format(creationdate);
								final Date duedate = item.getDue_date();
								final String due = dateformat.format(duedate);

								// Adds description. Appends ... if too long.
								String desc = "";
								if (description.length() > 128) {
									desc = description.substring(0, 124) + "... ";
								} else {
									desc = description;
								}

								// List tile
								ListTile tile = new ListTile();
								tile.textProperty().addAll(("#" + ticket_id + " " + subject), (desc),
										("Created: " + creation + "   Due: " + due));
								tile.setPrimaryGraphic(ticketicon);
								setGraphic(tile);
								setText(null);

							} else {
								setGraphic(null);
								setText(null);
							}
						} catch (Exception e) {
							System.out.println("Error setting cell bodies");
						}
					}
				});

				// Creates Listeners for each button. When pressed, the ticket will
				// open up in a new view (fourth view)
				charmlist1.selectedItemProperty().addListener((observable, olditem, newitem) -> {
						GluonApplication.currentAdminTicket = newitem;
						MobileApplication.getInstance().switchView("Fourth Admin View");
				});
			}
		});

	}

}
