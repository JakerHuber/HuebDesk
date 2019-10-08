package com.gluonapplication;


import com.gluonapplication.views.LoginView;
import com.gluonapplication.views.PrimaryView;
import com.gluonapplication.views.SecondaryView;
import com.gluonapplication.views.ThirdView;
import com.gluonapplication.views.FourthView;
import com.gluonapplication.views.FifthView;
import com.gluonapplication.views.PrimaryAdminView;
import com.gluonapplication.views.SecondaryAdminView;
import com.gluonapplication.views.ThirdAdminView;
import com.gluonapplication.views.FourthAdminView;
import com.gluonapplication.views.FifthAdminView;
import com.gluonapplication.views.SixthAdminView;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;

import huebdesk.items.Admins;
import huebdesk.items.Notes;
import huebdesk.items.Tickets;
import huebdesk.items.Users;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GluonApplication extends MobileApplication {

	public static final String LOGIN_VIEW = HOME_VIEW;
	public static final String PRIMARY_VIEW = "Primary View";
    public static final String SECONDARY_VIEW = "Secondary View";
    public static final String THIRD_VIEW = "Third View";
    public static final String FOURTH_VIEW = "Fourth View";
    public static final String FIFTH_VIEW = "Fifth View";
	public static final String PRIMARY_ADMIN_VIEW = "Primary Admin View";
    public static final String SECONDARY_ADMIN_VIEW = "Secondary Admin View";
    public static final String THIRD_ADMIN_VIEW = "Third Admin View";
    public static final String FOURTH_ADMIN_VIEW = "Fourth Admin View";
    public static final String FIFTH_ADMIN_VIEW = "Fifth Admin View";
    public static final String SIXTH_ADMIN_VIEW = "Sixth Admin View";
    
	// Employee account used across whole login session
	public static Admins currentAdmin;
	public static Users currentUser;
	public static Tickets currentUserTicket;
	public static Tickets currentAdminTicket;
	public static Notes currentUserNote;
	public static Notes currentAdminNote;
	public static boolean adminview;
    
    @Override
    public void init() {
    	addViewFactory(LOGIN_VIEW, () -> new LoginView().getView());
    	addViewFactory(PRIMARY_VIEW, () -> new PrimaryView().getView());
        addViewFactory(SECONDARY_VIEW, () -> new SecondaryView().getView());
        addViewFactory(THIRD_VIEW, () -> new ThirdView().getView());
        addViewFactory(FOURTH_VIEW, () -> new FourthView().getView());
        addViewFactory(FIFTH_VIEW, () -> new FifthView().getView());
    	addViewFactory(PRIMARY_ADMIN_VIEW, () -> new PrimaryAdminView().getView());
        addViewFactory(SECONDARY_ADMIN_VIEW, () -> new SecondaryAdminView().getView());
        addViewFactory(THIRD_ADMIN_VIEW, () -> new ThirdAdminView().getView());
        addViewFactory(FOURTH_ADMIN_VIEW, () -> new FourthAdminView().getView());
        addViewFactory(FIFTH_ADMIN_VIEW, () -> new FifthAdminView().getView());
        addViewFactory(SIXTH_ADMIN_VIEW, () -> new SixthAdminView().getView());
        
        DrawerManager.buildDrawer(this);
        
        currentAdmin = null;
        currentUser = null;
        adminview = false;
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        scene.getStylesheets().add(GluonApplication.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(GluonApplication.class.getResourceAsStream("/icon.png")));
        
    }
   
}
