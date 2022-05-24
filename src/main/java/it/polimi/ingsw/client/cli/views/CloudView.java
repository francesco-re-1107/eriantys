package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.cli.DrawingCharacters;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.server.model.StudentsContainer;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

public class CloudView extends BaseView{

    private static final int CLOUD_WIDTH = 9;

    private static final int CLOUD_HEIGHT = 5;

    private final StudentsContainer cloud;
    private final int index;

    public CloudView(StudentsContainer cloud, int index) {
        this.cloud = cloud;
        this.index = index;
    }

    @Override
    public void draw() {
        cursor.saveCursorPosition();
        cursor.paintBackground(Palette.CLOUD_BACKGROUND, CLOUD_WIDTH, CLOUD_HEIGHT);
        cursor.restoreCursorPosition();

        paintEdges();
        drawCloudInfo();
        drawStudents();
    }

    private void paintEdges() {
        cursor.saveCursorPosition();
        cursor.paintBackground(Palette.ISLAND_BACKGROUND, 1, 1);
        cursor.restoreCursorPosition();

        cursor.saveCursorPosition();
        cursor.moveRelative(CLOUD_WIDTH - 1, 0);
        cursor.paintBackground(Palette.ISLAND_BACKGROUND, 1, 1);
        cursor.restoreCursorPosition();

        cursor.saveCursorPosition();
        cursor.moveRelative(CLOUD_WIDTH - 1, CLOUD_HEIGHT - 1);
        cursor.paintBackground(Palette.ISLAND_BACKGROUND, 1, 1);
        cursor.restoreCursorPosition();

        cursor.saveCursorPosition();
        cursor.moveRelative(0, CLOUD_HEIGHT - 1);
        cursor.paintBackground(Palette.ISLAND_BACKGROUND, 1, 1);
        cursor.restoreCursorPosition();

    }

    private void drawStudents() {
        cursor.saveCursorPosition();

        cursor.moveRelative(3, 2);
        var rows = Utils.partition(cloud.toList(), 2);
        for (var row : rows) {
            if(row.size() == 1) {
                cursor.moveRelative(1, 0);
                cursor.print(ansi()
                        .bg(Palette.CLOUD_BACKGROUND)
                        .fg(Palette.STUDENTS_COLOR_MAP.get(row.get(0)))
                        .a(DrawingCharacters.STUDENT)
                        .reset());

                cursor.moveRelative(-2, 1);
            } else {
                cursor.print(ansi()
                        .bg(Palette.CLOUD_BACKGROUND)
                        .fg(Palette.STUDENTS_COLOR_MAP.get(row.get(0)))
                        .a(DrawingCharacters.STUDENT)
                        .a(" ")
                        .fg(Palette.STUDENTS_COLOR_MAP.get(row.get(1)))
                        .a(DrawingCharacters.STUDENT)
                        .reset());
                cursor.moveRelative(-3, 1);
            }
        }

        cursor.restoreCursorPosition();
    }

    private void drawCloudInfo() {
        cursor.saveCursorPosition();

        cursor.moveRelative(4, 1);
        cursor.print(ansi()
                .bg(Palette.CLOUD_BACKGROUND)
                .fg(Ansi.Color.BLACK)
                .bold()
                .a(index)
                .reset());

        cursor.restoreCursorPosition();
    }

}
