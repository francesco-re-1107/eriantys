package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CloudsCircularPane extends Pane {

    private List<StudentsContainer> clouds;

    public CloudsCircularPane() {
        this(new ArrayList<>());
    }

    public CloudsCircularPane(List<StudentsContainer> clouds) {
        super();
        this.clouds = clouds;

        var c = new StudentsContainer()
                .addStudents(Student.BLUE, 1)
                .addStudents(Student.GREEN, 1)
                .addStudents(Student.PINK, 1);
        var l = Arrays.asList(c, c, c);


        for (StudentsContainer cloud : l) {
            var cv = new CloudView(cloud);
            cv.setMaxWidth(175.0);
            cv.setMaxHeight(175.0);

            getChildren().add(cv);
        }
    }

    @Override
    protected void layoutChildren() {
        var increment = 360 / getChildren().size();
        var degree = -90;
        var cloudSize = 175.0;
        var radius = cloudSize/2 + 20;

        for (Node node : getChildren()) {
            double x = radius * Math.cos(Math.toRadians(degree)) + getWidth() / 2;
            double y = radius * Math.sin(Math.toRadians(degree)) + getHeight() / 2;
            layoutInArea(node, x - node.getBoundsInLocal().getWidth() / 2, y - node.getBoundsInLocal().getHeight() / 2, getWidth(), getHeight(), 0.0, HPos.LEFT, VPos.TOP);
            degree += increment;
        }

    }

}
