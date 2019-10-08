package com.gluonapplication.views;

import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class PrimaryAdminView {

    public View getView() {
        try {
            View view = FXMLLoader.load(PrimaryAdminView.class.getResource("primaryAdmin.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
