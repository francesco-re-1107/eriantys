package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class shows an island
 */
public class IslandView extends StackPane {

    private final ReducedIsland island;

    private final int index;

    private VBox elementsVbox;

    private final double size;

    private MotherNatureView motherNatureView;

    private FlowPane studentsFlowPane;

    private static final List<Image> islandImages = new ArrayList<>();

    private static final Image noEntryImage;

    //load images statically
    static {
        noEntryImage = new Image(IslandView.class.getResource("/assets/no_entry.png").toExternalForm());
        for(int i = 1; i <= 3; i++)
            islandImages.add(new Image(IslandView.class.getResourceAsStream("/assets/island" + i + ".png")));
    }

    public IslandView() {
        this(new ReducedIsland(
                        new StudentsContainer(),
                        1,
                        0,
                        Tower.GREY,
                        false
                ),
                0
        );
    }

    public IslandView(ReducedIsland island, int index) {
        super();
        this.island = island;
        this.index = index;

        size = Math.min(350, Math.max(150 + island.size() * 25, 175));

        setMaxWidth(size);
        setMaxHeight(size);

        setupElements();

        setDisable(true);
        setAlignment(Pos.CENTER);
        setId("island");
        setupBackground(index);

        getStylesheets().add(getClass().getResource("/css/island_view.css").toExternalForm());
    }

    private void setupElements() {
        elementsVbox = new VBox();
        elementsVbox.setSpacing(5);
        elementsVbox.setAlignment(Pos.CENTER);

        setupTowersView();
        setupStudents();
        setupMotherNatureView();
        getChildren().add(elementsVbox);

        setupNoEntry();
    }

    private void setupTowersView() {
        var towersView = new HBox();
        towersView.prefWidth(100);
        towersView.prefHeight(100);
        towersView.setSpacing(-15);
        towersView.setAlignment(Pos.TOP_CENTER);

        for(int i = 0; i < island.towersCount(); i++) {
            var tv = new TowerView(island.towerColor());
            tv.setFitWidth(Math.min(37, size/6));
            towersView.getChildren().add(tv);
        }

        elementsVbox.getChildren().add(towersView);
    }

    private void setupStudents() {
        studentsFlowPane = new FlowPane();

        studentsFlowPane.setMaxWidth(size);
        studentsFlowPane.setMaxHeight(size);
        studentsFlowPane.setHgap(3);
        studentsFlowPane.setVgap(3);
        studentsFlowPane.setAlignment(Pos.TOP_CENTER);

        elementsVbox.getChildren().add(studentsFlowPane);

        for (Student s : island.students().toList()) {
            addStudent(s);
        }
    }

    private void setupMotherNatureView() {
        motherNatureView = new MotherNatureView();
        motherNatureView.setFitWidth(Math.min(50, size/5));

        elementsVbox.getChildren().add(motherNatureView);
    }

    /**
     * Add a student to the island
     */
    public void addStudent(Student student) {
        var sv = new StudentView(student);
        sv.setFitWidth(Math.min(27, size/6));

        var students = new ArrayList<>(studentsFlowPane.getChildren());
        students.add(sv);

        //order students by their colors
        students.sort(Comparator.comparingInt(s -> ((StudentView) s).getStudent().ordinal()));

        studentsFlowPane.getChildren().clear();
        studentsFlowPane.getChildren().addAll(students);
    }

    private void setupNoEntry() {
        if(!island.noEntry()) return;

        var noEntry = new ImageView(noEntryImage);
        noEntry.setFitWidth(size);
        noEntry.setFitHeight(size);
        noEntry.setOpacity(1);

        setOnMouseEntered(event -> {
            FadeTransition ft = new FadeTransition(Duration.millis(150), noEntry);
            ft.setFromValue(1);
            ft.setToValue(0.25);
            ft.play();
        });

        setOnMouseExited(event -> {
            FadeTransition ft = new FadeTransition(Duration.millis(150), noEntry);
            ft.setFromValue(0.25);
            ft.setToValue(1);
            ft.play();
        });
        getChildren().add(noEntry);

    }

    private void setupBackground(int index) {
        //very strange formula to get the same image for the same index but having them distributed in a strange way
        var image = islandImages.get((int)Math.abs(Math.cos(index) * 10) % islandImages.size());

        BackgroundSize backgroundSize = new BackgroundSize(size, size, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        setBackground(new Background(backgroundImage));
    }

    /**
     * @return mother nature view of this island
     */
    public MotherNatureView getMotherNatureView() {
        return motherNatureView;
    }

    /**
     * @return the index of the island
     */
    public int getIndex() {
        return index;
    }
}
