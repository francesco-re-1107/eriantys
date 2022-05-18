package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.StudentsContainer;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class shows the clouds of the game
 */
public class CloudsPane extends FlowPane {

    public CloudsPane() {
        this(new ArrayList<>());
    }

    public CloudsPane(List<StudentsContainer> clouds) {
        super();

        setAlignment(Pos.CENTER);
        setHgap(20);
        setVgap(20);

        setClouds(clouds, c -> {});
    }

    /**
     * Set the clouds to show
     * @param clouds
     */
    public void setClouds(List<StudentsContainer> clouds, Consumer<StudentsContainer> listener) {
        getChildren().clear();

        for (StudentsContainer cloud : clouds) {
            var cv = new CloudView(cloud);
            cv.setMaxWidth(155.0);
            cv.setMaxHeight(155.0);
            cv.setMinWidth(155.0);
            cv.setMinHeight(155.0);
            cv.setOnMouseClicked(e -> listener.accept(cloud));

            getChildren().add(cv);
        }
    }
}
