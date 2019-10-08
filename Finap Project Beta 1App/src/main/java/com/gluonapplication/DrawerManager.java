package com.gluonapplication;


import static com.gluonapplication.GluonApplication.LOGIN_VIEW;
import static com.gluonapplication.GluonApplication.PRIMARY_VIEW;
import static com.gluonapplication.GluonApplication.SECONDARY_VIEW;
import static com.gluonapplication.GluonApplication.THIRD_VIEW;
import static com.gluonapplication.GluonApplication.FOURTH_VIEW;
import static com.gluonapplication.GluonApplication.FIFTH_VIEW;
import static com.gluonapplication.GluonApplication.PRIMARY_ADMIN_VIEW;
import static com.gluonapplication.GluonApplication.SECONDARY_ADMIN_VIEW;
import static com.gluonapplication.GluonApplication.THIRD_ADMIN_VIEW;
import static com.gluonapplication.GluonApplication.FOURTH_ADMIN_VIEW;
import static com.gluonapplication.GluonApplication.FIFTH_ADMIN_VIEW;
import static com.gluonapplication.GluonApplication.SIXTH_ADMIN_VIEW;

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.LifecycleService;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.control.NavigationDrawer.ViewItem;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.scene.image.Image;

public class DrawerManager {

	public static void buildDrawer(MobileApplication app) {
        NavigationDrawer drawer = app.getDrawer();
        
        // Drawer Header
        NavigationDrawer.Header header = new NavigationDrawer.Header("HuebDesk",
                "Helpdesk Mobile 1.01",
                new Avatar(21, new Image(DrawerManager.class.getResourceAsStream("/icon.png"))));
        drawer.setHeader(header);
        
        // Add View to Drawer
        final Item primaryItem = new ViewItem("Home", MaterialDesignIcon.HOME.graphic(), PRIMARY_VIEW, ViewStackPolicy.SKIP);
        final Item secondaryItem = new ViewItem("View Current Tickets", MaterialDesignIcon.LOCAL_PLAY.graphic(), SECONDARY_VIEW);
        final Item thirdItem = new ViewItem("Create New Ticket", MaterialDesignIcon.BORDER_COLOR.graphic(), THIRD_VIEW);
        final Item fourthItem = new ViewItem("View Ticket", MaterialDesignIcon.DASHBOARD.graphic(), FOURTH_VIEW);
        final Item fifthItem = new ViewItem("View Notes", MaterialDesignIcon.DASHBOARD.graphic(), FIFTH_VIEW);
        final Item primaryAdminItem = new ViewItem("Admin Home", MaterialDesignIcon.HOME.graphic(), PRIMARY_ADMIN_VIEW, ViewStackPolicy.SKIP);
        final Item secondaryAdminItem = new ViewItem("View Tickets", MaterialDesignIcon.LOCAL_PLAY.graphic(), SECONDARY_ADMIN_VIEW);
        final Item thirdAdminItem = new ViewItem("Create New Employee", MaterialDesignIcon.PERSON_ADD.graphic(), THIRD_ADMIN_VIEW);
        final Item fourthAdminItem = new ViewItem("Modify Ticket", MaterialDesignIcon.DASHBOARD.graphic(), FOURTH_ADMIN_VIEW);
        final Item fifthAdminItem = new ViewItem("View Notes", MaterialDesignIcon.DASHBOARD.graphic(), FIFTH_ADMIN_VIEW);
        final Item sixthAdminItem = new ViewItem("View Employees", MaterialDesignIcon.PERSON.graphic(), SIXTH_ADMIN_VIEW);
        
        // Add different drawers for users and admins
        if(GluonApplication.adminview) {
        	drawer.getItems().setAll(primaryAdminItem, secondaryAdminItem, thirdAdminItem, sixthAdminItem); // Admin drawer
        }
        else {
            drawer.getItems().setAll(primaryItem, secondaryItem, thirdItem); // Users drawer
        }
        
        // Sign out special detail
        final Item loginItem = new Item("Sign out", MaterialDesignIcon.ACCOUNT_CIRCLE.graphic());
        loginItem.selectedProperty().addListener((obs, ov, nv) -> {
        	if (nv) {
        		GluonApplication.currentAdmin = null;
        		GluonApplication.currentUser = null;
        		GluonApplication.adminview = false;
        		MobileApplication.getInstance().switchView(LOGIN_VIEW);
        	}
        	});
        
        drawer.getItems().add(loginItem);
      
        // Quit action
        if (Platform.isDesktop()) {
            final Item quitItem = new Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
            quitItem.selectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
                }
            });
            drawer.getItems().add(quitItem);
        }
    }
}