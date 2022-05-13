package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.common.requests.SelectCloudRequest;
import it.polimi.ingsw.server.model.StudentsContainer;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CloudsPane extends VBox {

    private List<StudentsContainer> clouds;

    public CloudsPane() {
        this(new ArrayList<>());
    }

    public CloudsPane(List<StudentsContainer> clouds) {
        super();
        this.clouds = clouds;

        setAlignment(Pos.CENTER);
        setSpacing(20);

        setClouds(clouds);
    }

    public void setClouds(List<StudentsContainer> clouds) {
        this.clouds = clouds;
        getChildren().clear();

        var currentHBox = new HBox();
        currentHBox.setAlignment(Pos.CENTER);
        currentHBox.setSpacing(20);

        getChildren().add(currentHBox);

        for (StudentsContainer cloud : clouds) {
            var cv = new CloudView(cloud);
            cv.setMaxWidth(155.0);
            cv.setMaxHeight(155.0);
            cv.setMinWidth(155.0);
            cv.setMinHeight(155.0);
            cv.setOnMouseClicked(e -> Client.getInstance()
                    .forwardGameRequest(
                            new SelectCloudRequest(cloud),
                            () -> {},
                            err -> Utils.LOGGER.info("Error selecting cloud " + err.getMessage())
                    ));

            currentHBox.getChildren().add(cv);

            //new line
            if(currentHBox.getChildren().size() % 2 == 0) {
                currentHBox = new HBox();
                currentHBox.setAlignment(Pos.CENTER);
                currentHBox.setSpacing(20);

                getChildren().add(currentHBox);
            }
        }
    }
}
