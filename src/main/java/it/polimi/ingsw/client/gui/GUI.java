package it.polimi.ingsw.client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws Exception  {
        Button btn = new Button("OK");
        Scene scene = new Scene(btn, 200, 250);

        stage.setTitle("Eriantys");
        stage.setScene(scene);
        stage.show();
    }

}
