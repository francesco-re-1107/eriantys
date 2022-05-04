package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class IslandCircularPane extends Pane {

    private List<ReducedIsland> islands;

    public IslandCircularPane(){
        this(new ArrayList<>());
    }

    public IslandCircularPane(List<ReducedIsland> islands) {
        super();
        this.islands = islands;

        for (var i : islands) {
            addIsland(i);
        }
    }

    @Override
    protected void layoutChildren() {
        var increment = 360 / getChildren().size();
        var degree = -90;
        var islandSize = 200.0;
        var radius = Math.min(getWidth()/2, getHeight()/2) - islandSize/2;

        for (Node node : getChildren()) {
            double x = radius * Math.cos(Math.toRadians(degree)) + getWidth() / 2;
            double y = radius * Math.sin(Math.toRadians(degree)) + getHeight() / 2;
            layoutInArea(node, x - node.getBoundsInLocal().getWidth() / 2, y - node.getBoundsInLocal().getHeight() / 2, getWidth(), getHeight(), 0.0, HPos.LEFT, VPos.TOP);
            degree += increment;
        }

    }

    public void addIsland(ReducedIsland i) {
        islands.add(i);
        var iv = new IslandView(i);

        iv.setOnMouseClicked(event -> iv.addStudent(Student.RED));

        getChildren().add(iv);
    }
}
