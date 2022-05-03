package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class IslandView extends StackPane {

    private ReducedIsland island;

    private final VBox vbox;

    private final ImageView noEntry;

    public IslandView() {
        this(
                new ReducedIsland(
                        new StudentsContainer()
                                .addStudents(Student.BLUE, 1)
                                .addStudents(Student.YELLOW, 2)
                                .addStudents(Student.RED, 3)
                                .addStudents(Student.GREEN, 1)
                                .addStudents(Student.PINK, 2),
                        1,
                        3,
                        Tower.GREY,
                        false
                )
        );
    }

    public IslandView(ReducedIsland island) {
        super();
        this.island = island;

        vbox = new VBox();

        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        var size = Math.min(400, Math.max(island.size() * 50, 200));
        setWidth(size);
        setHeight(size);
        prefWidth(size);
        prefHeight(size);
        minWidth(size);
        minHeight(size);

        setAlignment(Pos.CENTER);

        setId("island");

        Image image = new Image(getClass().getResource("/assets/island1.png").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(size, size, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        setBackground(new Background(backgroundImage));

        vbox.getChildren().add(getTowersView());
        vbox.getChildren().add(getStudentsView());
        vbox.getChildren().add(getMotherNatureView());

        getChildren().add(vbox);

        noEntry = new ImageView(new Image(getClass().getResourceAsStream("/assets/no_entry.png")));
        noEntry.setFitWidth(200);
        noEntry.setFitHeight(200);
        noEntry.setVisible(!island.noEntry());
        noEntry.setOpacity(0.4);
        getChildren().add(noEntry);

    }

    private Node getMotherNatureView() {
        var mn = new MotherNatureView(MotherNatureView.State.DISABLED);
        mn.setFitWidth(50);
        mn.setFitHeight(100);

        return mn;
    }

    private Node getStudentsView() {
        var studentsView = new VBox();

        studentsView.prefWidth(100);
        studentsView.prefHeight(100);
        studentsView.setSpacing(5);
        studentsView.setAlignment(Pos.TOP_CENTER);

        var currentHBox = new HBox();
        currentHBox.setSpacing(5);
        currentHBox.setAlignment(Pos.CENTER);
        currentHBox.prefWidth(100);
        currentHBox.prefHeight(100);

        studentsView.getChildren().add(currentHBox);

        for (Student s : island.students().toList()) {
            var sv = new StudentView(s);
            sv.setFitWidth(30);
            currentHBox.getChildren().add(sv);

            if(currentHBox.getChildren().size() % 5 == 0) {
                currentHBox = new HBox();
                currentHBox.setSpacing(5);
                currentHBox.setAlignment(Pos.CENTER);
                currentHBox.prefWidth(100);
                currentHBox.prefHeight(100);

                studentsView.getChildren().add(currentHBox);
            }
        }

        return studentsView;
    }

    private Node getTowersView() {
        var towersView = new HBox();
        towersView.prefWidth(100);
        towersView.prefHeight(100);
        towersView.setSpacing(-18);
        towersView.setAlignment(Pos.TOP_CENTER);
        for(int i = 0; i < island.towersCount(); i++) {
            var tv = new TowerView(island.towerColor());
            tv.setFitWidth(36);
            towersView.getChildren().add(tv);
        }

        return towersView;
    }

    public void updateIsland(ReducedIsland island) {
        this.island = island;
    }

    public ReducedIsland getIsland() {
        return island;
    }
}