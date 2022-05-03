package it.polimi.ingsw.client.gui.customviews;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class IslandCircularPane extends Pane {

    public IslandCircularPane(){

        for (int i = 0; i < 12; i++){
            var iv = new IslandView();
            iv.setMaxHeight(100.0);
            iv.setMaxWidth(100.0);

            getChildren().add(iv);
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
}
