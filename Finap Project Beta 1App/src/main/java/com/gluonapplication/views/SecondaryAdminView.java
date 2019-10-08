package com.gluonapplication.views;

import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class SecondaryAdminView {
    
    public View getView() {
        try {
            View view = FXMLLoader.load(SecondaryAdminView.class.getResource("secondaryAdmin.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
