package com.gluonapplication.views;

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

import huebdesk.items.Employees;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class SixthAdminPresenter {

	@FXML
	private View sixthAdmin;

	@FXML
	private CharmListView<Employees, Integer> charmlist1;

	public void initialize() {
		sixthAdmin.setShowTransitionFactory(v -> new FadeInRightTransition(v));

		sixthAdmin.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK
						.button(e -> MobileApplication.getInstance().switchView("Primary Admin View")));
				appBar.setTitleText("View Employees");
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
		sixthAdmin.setOnShown(e -> {
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
				ObservableList<Employees> allemployees = GluonApplication.currentAdmin.getAdminAllEmployees();
				if(!allemployees.isEmpty()){
					charmlist1.setItems(allemployees);
				}
				else {
					charmlist1.setPlaceholder(new Label("No tickets avaliable"));
				}

				// Set charm header structure
				charmlist1.setHeadersFunction(Employees::getEmployee_id);
				charmlist1.setHeaderComparator((s1, s2) -> s2.compareTo(s1));

				// Set list hearders
				charmlist1.setHeaderCellFactory(p9 -> new CharmListCell<Employees>() {

					private final Icon open, closed;
					{
						open = new Icon(MaterialDesignIcon.CHECK);
						open.getStyleClass().add("open");
						closed = new Icon(MaterialDesignIcon.CANCEL);
						closed.getStyleClass().add("closed");
					}

					@Override
					public void updateItem(Employees item, boolean empty) {
						super.updateItem(item, empty);

						// Setting titles for each
						try {
							if (item != null && !empty) {
								//final int ticket_id = item.getTicket_id();
								//final String status = item.gettstatus();

								// List tile
								ListTile tile = new ListTile();


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
				charmlist1.setCellFactory(p -> new CharmListCell<Employees>() { // ERROR HERE?

					private final Icon personicon;
					{
						personicon = new Icon(MaterialDesignIcon.PERSON);
						personicon.getStyleClass().add("person");
					}

					@Override
					public void updateItem(Employees item, boolean empty) {
						super.updateItem(item, empty);
			

						// try setting cell bodys
						try {
							if (item != null && !empty) {
								// Load variables
								//final String ename = item.
								final String name = item.getEname();
								final String company = item.getEcompany();
								final String locations = item.getElocation();
								final String username = item.getEusername();
								final String department = item.getEdepartment();
								final int id = item.getEmployee_id();

								// List tile
								ListTile tile = new ListTile();
								tile.setMinWidth(140);
								tile.textProperty().addAll(("#" + id), (username), (locations));
								tile.setPrimaryGraphic(personicon);
								setGraphic(tile);
								setText(name + " from " + company + ", " + department);

							} else {
								setGraphic(null);
								setText(null);
							}
						} catch (Exception e) {
							System.out.println("Error setting cell bodies");
						}
					}
				});

				// TODO: Use the code below to make the Employee objects interactable
				/*
				// Creates Listeners for each button. When pressed, the ticket will
				// open up in a new view (fourth view)
				charmlist1.selectedItemProperty().addListener((observable, olditem, newitem) -> {
						GluonApplication.currentAdminTicket = newitem;
						MobileApplication.getInstance().switchView("Fourth Admin View");
				});
				*/
			}
		});

	}

}
