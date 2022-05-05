package it.polimi.ingsw.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws Exception  {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_menu.fxml"));

        Scene scene = new Scene(root);

        stage.setMinWidth(1600);
        stage.setMinHeight(900);
        stage.setMaximized(true);
        stage.setTitle("Eriantys");
        stage.setScene(scene);
        stage.show();
    }

}
