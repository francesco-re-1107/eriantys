package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.cli.DrawingCharacters;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.server.model.StudentsContainer;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This view shows a cloud of students
 */
public class CloudView extends BaseView{

    /**
     * Cloud to be displayed
     */
    private final StudentsContainer cloud;

    /**
     * Cloud index, printed in the cloud
     */
    private final int index;

    /**
     * Cloud WIDTH
     */
    private static final int CLOUD_WIDTH = 9;

    /**
     * Cloud HEIGHT
     */
    private static final int CLOUD_HEIGHT = 5;

    /**
     * Create a CloudView with the given cloud and index
     * @param cloud cloud to be displayed
     * @param index cloud index in the round
     */
    public CloudView(StudentsContainer cloud, int index) {
        this.cloud = cloud;
        this.index = index;
    }

    @Override
    public void draw() {
        cursor.saveCursorPosition();
        cursor.paintBackground(Palette.CLOUD_BACKGROUND, CLOUD_WIDTH, CLOUD_HEIGHT);
        cursor.restoreCursorPosition();

        paintVertices();
        drawCloudInfo();
        drawStudents();
    }

    /**
     * Paint the vertices of the cloud with the island background color to obtain a more rounded shape
     */
    private void paintVertices() {
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

    /**
     * Draw the students in the cloud
     */
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

    /**
     * Draw the cloud index
     */
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
