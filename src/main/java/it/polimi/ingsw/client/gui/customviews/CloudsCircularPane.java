package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.StudentsContainer;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class CloudsCircularPane extends Pane {

    private List<StudentsContainer> clouds;

    public CloudsCircularPane() {
        this(new ArrayList<>());
    }

    public CloudsCircularPane(List<StudentsContainer> clouds) {
        super();
        this.clouds = clouds;

        for (StudentsContainer c : clouds) {
            addCloud(c);
        }
    }

    @Override
    protected void layoutChildren() {
        if(getChildren().size() == 0) return;

        var increment = 360 / getChildren().size();
        var degree = -90;
        var cloudSize = 165.0;

        var radius = cloudSize/2 + 20;

        if(getChildren().size() == 1) {
            radius = 0;
        }

        for (Node node : getChildren()) {
            double x = radius * Math.cos(Math.toRadians(degree)) + getWidth() / 2;
            double y = radius * Math.sin(Math.toRadians(degree)) + getHeight() / 2;
            layoutInArea(node, x - node.getBoundsInLocal().getWidth() / 2, y - node.getBoundsInLocal().getHeight() / 2, getWidth(), getHeight(), 0.0, HPos.LEFT, VPos.TOP);
            degree += increment;
        }
    }

    public void addCloud(StudentsContainer c) {
        var cv = new CloudView(c);
        cv.setMaxWidth(175.0);
        cv.setMaxHeight(175.0);

        getChildren().add(cv);
    }
}
