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

import java.util.Random;

public class IslandView extends StackPane {

    private ReducedIsland island;

    private final VBox vbox;

    private ImageView noEntry;

    private final double size;

    private MotherNatureView motherNatureView;

    private VBox studentsViewVBox;

    private HBox studentsViewCurrentHBox;

    public IslandView() {
        this(
                new ReducedIsland(
                        new StudentsContainer()
                                .addStudents(Student.GREEN, 1)
                                .addStudents(Student.RED, 1)
                                .addStudents(Student.PINK, 1),
                        1,
                        0,
                        Tower.GREY,
                        false
                )
        );
    }

    public IslandView(ReducedIsland island) {
        super();
        this.island = island;

        vbox = new VBox();

        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);

        size = Math.min(350, Math.max(150 + island.size() * 25, 175));

        setWidth(size);
        setHeight(size);
        setPrefWidth(size);
        setPrefHeight(size);
        setMinWidth(size);
        setMinHeight(size);
        setMaxWidth(size);
        setMaxHeight(size);

        setDisable(true);

        setAlignment(Pos.CENTER);

        setId("island");

        setupBackground();

        setupTowersView();
        setupStudents();
        setupMotherNatureView();

        getChildren().add(vbox);

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

        vbox.getChildren().add(towersView);
    }

    private void setupStudents() {
        studentsViewVBox = new VBox();

        studentsViewVBox.setMaxWidth(size);
        studentsViewVBox.setMaxHeight(size);
        studentsViewVBox.setSpacing(3);
        studentsViewVBox.setAlignment(Pos.TOP_CENTER);

        studentsViewCurrentHBox = new HBox();
        studentsViewCurrentHBox.setSpacing(3);
        studentsViewCurrentHBox.setAlignment(Pos.CENTER);
        studentsViewCurrentHBox.prefWidth(size);
        studentsViewCurrentHBox.prefHeight(100);

        studentsViewVBox.getChildren().add(studentsViewCurrentHBox);

        for (Student s : island.students().toList()) {
            addStudent(s);
        }

        vbox.getChildren().add(studentsViewVBox);
    }

    public void addStudent(Student student) {

        var studentsPerRow = 5 + Math.floor(island.size() / 2.5);
        var sv = new StudentView(student);
        sv.setFitWidth(Math.min(27, size/8));
        studentsViewCurrentHBox.getChildren().add(sv);

        if(studentsViewCurrentHBox.getChildren().size() % studentsPerRow == 0) {
            studentsViewCurrentHBox = new HBox();
            studentsViewCurrentHBox.setSpacing(3);
            studentsViewCurrentHBox.setAlignment(Pos.CENTER);
            studentsViewCurrentHBox.prefWidth(100);
            studentsViewCurrentHBox.prefHeight(size);

            studentsViewVBox.getChildren().add(studentsViewCurrentHBox);
        }
    }

    private void setupNoEntry() {
        noEntry = new ImageView(new Image(getClass().getResourceAsStream("/assets/no_entry.png")));
        noEntry.setFitWidth(size/1.4);
        noEntry.setFitHeight(size/1.4);
        noEntry.setOpacity(1);

        setOnMouseEntered(event -> {
            FadeTransition ft = new FadeTransition(Duration.millis(150), noEntry);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();
        });

        setOnMouseExited(event -> {
            FadeTransition ft = new FadeTransition(Duration.millis(150), noEntry);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        });
        noEntry.setVisible(island.noEntry());
        getChildren().add(noEntry);
    }

    private void setupBackground() {
        //random number from 1 to 3
        var random = new Random().nextInt(3) + 1;
        Image image = new Image(getClass().getResource("/assets/island" + random + ".png").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(size, size, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        setBackground(new Background(backgroundImage));

        //setBackground(new Background(new BackgroundFill(Paint.valueOf("#f5f5f5"), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void setupMotherNatureView() {
        motherNatureView = new MotherNatureView();
        motherNatureView.setFitWidth(Math.min(50, size/5));

        vbox.getChildren().add(motherNatureView);
    }

    public MotherNatureView getMotherNatureView() {
        return motherNatureView;
    }

    public void updateIsland(ReducedIsland island) {
        this.island = island;
    }

    public ReducedIsland getIsland() {
        return island;
    }
}
