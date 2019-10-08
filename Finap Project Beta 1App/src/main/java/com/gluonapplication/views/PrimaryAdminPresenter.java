package com.gluonapplication.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PrimaryAdminPresenter {
	
    @FXML
    private View primaryAdmin;

    @FXML
    private Label label;
    
	@FXML
	public AppBar tabs;
	
	@FXML
	private Button viewticketbtn;
	
	@FXML
	private Button viewemployeesbtn;
	
	@FXML
	private Button newemployeebtn;
	
	@FXML
	private Button viewanalyticsbtn;

    public void initialize() {
    	primaryAdmin.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        primaryAdmin.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().getDrawer().open()));
                appBar.setTitleText("Admin Home");
                appBar.getActionItems().add(MaterialDesignIcon.LOCAL_PLAY.button(e -> 
                MobileApplication.getInstance().switchView("Secondary Admin View")));
                appBar.getActionItems().add(MaterialDesignIcon.PERSON.button(e -> 
                MobileApplication.getInstance().switchView("Sixth Admin View")));
                appBar.getActionItems().add(MaterialDesignIcon.PERSON_ADD.button(e -> 
                MobileApplication.getInstance().switchView("Third Admin View")));
            }
        });
		
    }
    
    /*
     * Changes view to secondary view
     */
    @FXML
    void viewticketbtnClick() {
    	MobileApplication.getInstance().switchView("Secondary Admin View");
    }
    
    /*
     * Changes view to third view
     */
    @FXML
    void viewemployeesbtnClick() {
    	MobileApplication.getInstance().switchView("Sixth Admin View");
    }
    
    /*
     * Changes view to secondary view
     */
    @FXML
    void newemployeebtnClick() {
    	MobileApplication.getInstance().switchView("Third Admin View");
    }
    
    /*
     * Changes view to secondary view
     */
    @FXML
    void viewanalyticsbtnClick() {
    	
    }
    
}
