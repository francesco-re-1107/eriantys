package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.server.model.Student;
import javafx.animation.FadeTransition;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * This class shows the islands in a circular way, and manages various game states
 */
public class IslandsCircularPane extends Pane {

    private List<ReducedIsland> islands;

    private StudentSelectContextMenu studentSelectContextMenu;
    
    public IslandsCircularPane(){
        this(new ArrayList<>());
    }

    public IslandsCircularPane(List<ReducedIsland> islands) {
        super();
        this.islands = islands;

        for (var i : islands) {
            addIsland(i);
        }
    }

    @Override
    protected void layoutChildren() {
        if(getChildren().isEmpty()) return;

        var increment = 360.0 / getChildren().size();

        var degree = -90.0;
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

    /**
     * Set the islands to show
     * @param islands
     */
    public void setIslands(List<ReducedIsland> islands) {
        //if islands doesn't change, just set all islands as disabled
        if(islands.equals(this.islands)) {
            for (var iv : getChildren())
                iv.setDisable(true);
            return;
        }

        this.islands = islands;
        setOpacity(0.0);
        getChildren().clear();

        for (var i : islands)
            addIsland(i);

        var ft = new FadeTransition(javafx.util.Duration.millis(100), this);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    /**
     * Show mother nature on the given island index
     * @param index
     */
    public void setMotherNaturePosition(int index) {
        //reset
        for (var iv : getChildren())
            ((IslandView) iv).getMotherNatureView().setState(MotherNatureView.State.INVISIBLE);

        var iv = (IslandView) getChildren().get(index);
        iv.getMotherNatureView().setState(MotherNatureView.State.ENABLED);
    }

    /**
     * Arrange islands in order to show the player the possibility to move mother nature
     * @param mnIndex current mother nature position
     * @param steps max number of steps to move
     * @param listener listener to call when the player moves the mother nature
     */
    public void arrangeIslandsForMotherNatureMovement(int mnIndex, int steps, IntConsumer listener){
        //disable all islands
        for (int i = 0; i < getChildren().size(); i++) {
            var iv = (IslandView) getChildren().get(i);
            iv.setDisable(true);

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

    /**
     * Arrange islands in order to show the player the possibility to place students
     * @param myPlayer used to show the students available to place
     * @param listener called when a student is placed
     */
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

    /**
     * Arrange islands in order to show the player the possibility to select an island
     * @param listener
     */
    public void arrangeIslandsForPlayingCharacterCard(Consumer<IslandView> listener) {
        for (var n : getChildren()) {
            var iv = (IslandView) n;
            iv.setDisable(false);
            iv.setOnMouseClicked(e -> listener.accept(iv));
        }
    }
}
