package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class IslandView extends VBox {

    private ReducedIsland island;

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
                        2,
                        Tower.BLACK,
                        false
                )
        );
    }

    public IslandView(ReducedIsland island) {
        super();
        this.island = island;

        var size = Math.min(400, Math.max(island.size() * 50, 200));
        setWidth(size);
        setHeight(size);
        prefWidth(size);
        prefHeight(size);
        minWidth(size);
        minHeight(size);

        setId("island");

        Image image = new Image(getClass().getResource("/assets/island1.png").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(size, size, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        setBackground(new Background(backgroundImage));

        getChildren().add(getTowersView());
        getChildren().add(getStudentsView());
        //getChildren().add(getMotherNatureView());
    }

    private Node getStudentsView() {
        var studentsView = new HBox();

        studentsView.prefWidth(100);
        studentsView.prefHeight(100);
        studentsView.setSpacing(5);
        studentsView.setAlignment(Pos.TOP_CENTER);

        for(var s : Student.values()) {
            for(int i = 0; i < island.students().getCountForStudent(s); i++) {
                var sv = new StudentView(s);
                sv.setFitWidth(25);
                studentsView.getChildren().add(sv);
            }
        }

        return studentsView;
    }

    private Node getTowersView() {
        var towersView = new HBox();
        towersView.prefWidth(100);
        towersView.prefHeight(100);
        towersView.setSpacing(-15);
        towersView.setAlignment(Pos.TOP_CENTER);
        for(int i = 0; i < island.towersCount(); i++) {
            var tv = new TowerView(island.towerColor());
            tv.setFitHeight(60);
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
