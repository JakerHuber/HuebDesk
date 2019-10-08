package com.gluonapplication.views;

import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class ThirdAdminView {
    
    public View getView() {
        try {
            View view = FXMLLoader.load(ThirdAdminView.class.getResource("thirdAdmin.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
