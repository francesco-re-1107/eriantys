package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.common.requests.SelectCloudRequest;
import it.polimi.ingsw.server.model.StudentsContainer;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;

public class CloudsPane extends FlowPane {

    public CloudsPane() {
        this(new ArrayList<>());
    }

    public CloudsPane(List<StudentsContainer> clouds) {
        super();

        setAlignment(Pos.CENTER);
        setHgap(20);
        setVgap(20);

        setClouds(clouds);
    }

    public void setClouds(List<StudentsContainer> clouds) {
        getChildren().clear();

        for (StudentsContainer cloud : clouds) {
            var cv = new CloudView(cloud);
            cv.setMaxWidth(155.0);
            cv.setMaxHeight(155.0);
            cv.setMinWidth(155.0);
            cv.setMinHeight(155.0);
            //TODO: move to GameController
            cv.setOnMouseClicked(e -> Client.getInstance()
                    .forwardGameRequest(
                            new SelectCloudRequest(cloud),
                            () -> {},
                            err -> Utils.LOGGER.info("Error selecting cloud " + err.getMessage())
                    ));

            getChildren().add(cv);
        }
    }
}
