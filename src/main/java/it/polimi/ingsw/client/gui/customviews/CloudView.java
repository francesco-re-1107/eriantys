package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class CloudView extends VBox {

    public CloudView() {
        this(new StudentsContainer());
    }

    public CloudView(StudentsContainer cloud) {
        super();

        setId("cloud");
        setAlignment(Pos.CENTER);
        setupBackground();
        setSpacing(10);

        var currentHBox = new HBox();
        currentHBox.setAlignment(Pos.CENTER);
        currentHBox.setSpacing(10);
        getChildren().add(currentHBox);

        for(Student s : cloud.toList()) {
            var sv = new StudentView(s);

            currentHBox.getChildren().add(sv);

            if (currentHBox.getChildren().size() % 2 == 0) {
                currentHBox = new HBox();
                currentHBox.setAlignment(Pos.CENTER);
                currentHBox.setSpacing(10);
                getChildren().add(currentHBox);
            }
        }
    }

    private void setupBackground() {
        Image image = new Image(getClass().getResource("/assets/cloud.png").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(150, 150, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        setBackground(new Background(backgroundImage));
    }
}
