package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.server.model.Student;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

public class IslandCircularPane extends Pane {

    private List<ReducedIsland> islands;

    private StudentSelectContextMenu studentSelectContextMenu;
    
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
        if(getChildren().isEmpty()) return;

        var increment = 360 / getChildren().size();
        var degree = -90;
        var islandSize = 200.0;

        var maxSize = getChildren().stream()
                .mapToDouble(n -> n.getBoundsInLocal().getWidth())
                .max();

        if (maxSize.isPresent()) {
            islandSize = maxSize.getAsDouble();
        }

        var radius = Math.min(getWidth()/2, getHeight()/2) - islandSize/2;

        for (Node node : getChildren()) {
            double x = radius * Math.cos(Math.toRadians(degree)) + getWidth() / 2;
            double y = radius * Math.sin(Math.toRadians(degree)) + getHeight() / 2;
            layoutInArea(node, x - node.getBoundsInLocal().getWidth() / 2, y - node.getBoundsInLocal().getHeight() / 2, getWidth(), getHeight(), 0.0, HPos.LEFT, VPos.TOP);
            degree += increment;
        }

    }

    private void addIsland(ReducedIsland i) {
        var iv = new IslandView(i, getChildren().size());

        getChildren().add(iv);
    }

    public void setIslands(List<ReducedIsland> islands) {
        this.islands = islands;
        getChildren().clear();

        for (var i : islands)
            addIsland(i);
    }

    public void setMotherNaturePosition(int index) {
        //reset
        for (var iv : getChildren())
            ((IslandView) iv).getMotherNatureView().setState(MotherNatureView.State.INVISIBLE);

        var iv = (IslandView) getChildren().get(index);
        iv.getMotherNatureView().setState(MotherNatureView.State.ENABLED);
    }

    public void arrangeIslandsForMotherNatureMovement(int mnIndex, int steps, IntConsumer listener){
        //disable all islands
        for (int i = 0; i < getChildren().size(); i++) {
            var iv = (IslandView) getChildren().get(i);
            iv.setDisable(true);

            //var isPossibleStep = (i > mnIndex && i <= mnIndex + steps) ||
            //                        (i < ((mnIndex + steps) % islands.size()) && (i + steps) >= islands.size());

            var isPossibleStep = (i <= mnIndex + steps && i > mnIndex) || (i <= (mnIndex + steps) % islands.size() && i <= mnIndex && mnIndex + steps >= islands.size());

            if (mnIndex == i)
                iv.getMotherNatureView().setState(MotherNatureView.State.ENABLED);
            else if (isPossibleStep)
                iv.getMotherNatureView().setState(MotherNatureView.State.DISABLED);
            else
                iv.getMotherNatureView().setState(MotherNatureView.State.INVISIBLE);

            var stepsMade = mnIndex < i ? i - mnIndex : islands.size() - Math.abs(i - mnIndex);
            if(isPossibleStep) {
                iv.setDisable(false);
                iv.setOnMouseClicked(e -> listener.accept(stepsMade));
            }
        }
    }

    public void arrangeIslandsForPlacingStudents(ReducedPlayer myPlayer, BiConsumer<Student, IslandView> listener) {
        for (var n : getChildren()) {
            var iv = (IslandView) n;
            iv.setDisable(false);

            iv.setOnMouseClicked(e -> {
                if(studentSelectContextMenu != null)
                    studentSelectContextMenu.hide();

                studentSelectContextMenu = new StudentSelectContextMenu(
                        myPlayer.entrance(),
                        s -> listener.accept(s, iv)
                );

                studentSelectContextMenu.show(iv, e.getScreenX(), e.getScreenY());
            });
        }
    }
}
