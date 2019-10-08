package com.gluonapplication.views;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PrimaryPresenter {
	
    @FXML
    private View primary;

    @FXML
    private Label label;
    
	@FXML
	public AppBar tabs;
	
	@FXML
	private Button viewticketbtn;
	
	@FXML
	private Button newticketbtn;

    public void initialize() {
    	primary.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        primary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().getDrawer().open()));
                appBar.setTitleText("Home");
                appBar.getActionItems().add(MaterialDesignIcon.LOCAL_PLAY.button(e -> 
                MobileApplication.getInstance().switchView("Secondary View")));
                appBar.getActionItems().add(MaterialDesignIcon.BORDER_COLOR.button(e -> 
                MobileApplication.getInstance().switchView("Third View")));
            }
        });
		
    }
    
    @FXML
    void buttonClick() {
        label.setText("Hello JavaFX Universe!");
    }
    
    /*
     * Changes view to secondary view
     */
    @FXML
    void newticketbtnClick() {
    	MobileApplication.getInstance().switchView("Third View");
    }
    
    /*
     * Changes view to third view
     */
    @FXML
    void viewticketbtnClick() {
    	MobileApplication.getInstance().switchView("Secondary View");
    }
    
}
