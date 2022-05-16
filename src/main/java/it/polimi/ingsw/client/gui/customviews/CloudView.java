package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class CloudView extends FlowPane {

    public CloudView() {
        this(new StudentsContainer());
    }

    private static final Image cloudImage;

    static {
        cloudImage = new Image(CloudView.class.getResourceAsStream("/assets/cloud.png"));
    }

    public CloudView(StudentsContainer cloud) {
        super();

        setId("cloud");
        setAlignment(Pos.CENTER);
        setupBackground();
        setHgap(10);
        setVgap(7);
        setPadding(new Insets(20, 20, 20, 20));

        getStylesheets().add(getClass().getResource("/css/cloud_view.css").toExternalForm());

        for(Student s : cloud.toList()) {
            var sv = new StudentView(s);
            sv.setFitWidth(40);
            getChildren().add(sv);
        }
    }

    private void setupBackground() {
        BackgroundSize backgroundSize = new BackgroundSize(150, 150, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(cloudImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        setBackground(new Background(backgroundImage));
    }
}
