package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.cli.DrawingCharacters;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;

import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This view is used to show an island.
 */
public class IslandView extends BaseView {

    /**
     * The reduced island to display.
     */
    private final ReducedIsland island;

    /**
     * Whether mother nature is present on this island.
     */
    private final boolean motherNature;

    /**
     * Index of the island.
     * Used to display the island number.
     */
    private final int index;

    /**
     * WIDTH of the island.
     */
    public static final int ISLAND_WIDTH = 11;

    /**
     * HEIGHT of the island.
     */
    public static final int ISLAND_HEIGHT = 7;

    /**
     * Create a new IslandView with the given parameters.
     * @param island the reduced island to display.
     * @param index the index of the island.
     * @param motherNature whether mother nature is present on this island.
     */
    public IslandView(ReducedIsland island, int index, boolean motherNature) {
        this.island = island;
        this.index = index;
        this.motherNature = motherNature;
    }

    @Override
    public void draw() {
        drawBorders();
        drawIslandInfo();
        drawStudents();
        drawTowers();
    }

    /**
     * Draw towers on the island.
     */
    private void drawTowers() {
        cursor.saveCursorPosition();
        cursor.moveRelative(1, ISLAND_HEIGHT - 2);

        if(island.towersCount() > 0) {
            cursor.print(ansi()
                    .fg(Palette.TOWERS_COLOR_MAP.get(island.towerColor()))
                    .bg(Palette.ISLAND_BACKGROUND)
                    .a(DrawingCharacters.TOWER.repeat(island.towersCount()))
            );
        }

        cursor.restoreCursorPosition();
    }

    /**
     * Draw students on the island.
     */
    private void drawStudents() {
        cursor.saveCursorPosition();
        cursor.moveRelative(1, 2);

        if(island.students().getSize() > 24)
            drawCompactStudents();
        else
            drawExtendedStudents();

        cursor.restoreCursorPosition();
    }

    /**
     * Draw students on the island in an extended view.
     * Used if there are less than 24 students on the island.
     */
    private void drawExtendedStudents() {
        var rows = Utils.partition(island.students().toList(), ISLAND_WIDTH - 2);

        for (var row : rows) {
            for (var s : row)
                cursor.print(ansi()
                        .fg(Palette.STUDENTS_COLOR_MAP.get(s))
                        .bg(Palette.ISLAND_BACKGROUND)
                        .a(DrawingCharacters.STUDENT)
                );
            cursor.moveRelative(-(ISLAND_WIDTH - 2), 1);
        }
    }

    /**
     * Draw students on the island in a compact view.
     * Used if there are more than 24 students on the island.
     */
    private void drawCompactStudents() {
        var rows = Utils.partition(List.of(Student.values()), 2);

        for (var row : rows) {
            var rowLength = 0;
            for (var s : row) {
                var studString = ansi()
                        .fg(Palette.STUDENTS_COLOR_MAP.get(s))
                        .bg(Palette.ISLAND_BACKGROUND)
                        .a(DrawingCharacters.STUDENT)
                        .a(" ")
                        .fgBlack()
                        .bold()
                        .a(island.students().getCountForStudent(s))
                        .a(" ")
                        .reset();

                cursor.print(studString);
                rowLength += cursor.realLength(studString);
            }
            cursor.moveRelative(-rowLength, 1);
        }
    }

    /**
     * Draw island index, mother nature (if present) and no entry.
     */
    private void drawIslandInfo() {
        cursor.saveCursorPosition();

        cursor.moveRelative(1, 1);
        cursor.print(ansi()
                .fgBlack()
                .bg(Palette.ISLAND_BACKGROUND)
                .bold()
                .a(String.format("%d ", index)));

        //1 character
        if(motherNature)
            cursor.print(ansi()
                    .fg(Palette.MOTHER_NATURE)
                    .bg(Palette.ISLAND_BACKGROUND)
                    .a(DrawingCharacters.MOTHER_NATURE));
        else
            cursor.print(ansi()
                    .bg(Palette.ISLAND_BACKGROUND)
                    .a(DrawingCharacters.EMPTY));

        cursor.restoreCursorPosition();

        cursor.saveCursorPosition();
        cursor.moveRelative(1, 1);

        //move to last 2 chars of the first line
        cursor.moveRelative(ISLAND_WIDTH - 4 , 0);
        if(island.noEntry()) {
            cursor.print(ansi()
                    .bg(Palette.ISLAND_BACKGROUND)
                    .a(DrawingCharacters.NO_ENTRY)
            );
            cursor.moveRelative(-ISLAND_WIDTH + 1, -1);
        } else {
            cursor.restoreCursorPosition();
        }
    }

    /**
     * Draw borders of the island.
     */
    private void drawBorders() {
        cursor.saveCursorPosition();

        var borderColor = motherNature ? Palette.MOTHER_NATURE : Palette.ISLAND_BORDER;

        //upper border
        cursor.print(ansi()
                .fg(borderColor)
                .bg(Palette.ISLAND_BACKGROUND)
                .a(DrawingCharacters.ISLAND_CORNER_TOP_LEFT)
                .a(DrawingCharacters.ISLAND_BORDER_HORIZONTAL.repeat(ISLAND_WIDTH - 2))
                .a(DrawingCharacters.ISLAND_CORNER_TOP_RIGHT)
                .reset()
        );

        //right border
        for(int i = 0; i < ISLAND_HEIGHT - 2; i++) {
            cursor.moveRelative(-1, 1);
            cursor.print(ansi()
                    .fg(borderColor)
                    .bg(Palette.ISLAND_BACKGROUND)
                    .a(DrawingCharacters.ISLAND_BORDER_VERTICAL)
                    .reset()
            );
        }

        cursor.moveRelative(-ISLAND_WIDTH, 1);

        //lower border
        cursor.print(ansi()
                .fg(borderColor)
                .bg(Palette.ISLAND_BACKGROUND)
                .a(DrawingCharacters.ISLAND_CORNER_BOTTOM_LEFT)
                .a(DrawingCharacters.ISLAND_BORDER_HORIZONTAL.repeat(ISLAND_WIDTH - 2))
                .a(DrawingCharacters.ISLAND_CORNER_BOTTOM_RIGHT)
                .reset()
        );

        cursor.moveRelative(-ISLAND_WIDTH + 1, 0);

        //left border
        for(int i = 0; i < ISLAND_HEIGHT - 2; i++) {
            cursor.moveRelative(-1, -1);
            cursor.print(ansi()
                    .fg(borderColor)
                    .bg(Palette.ISLAND_BACKGROUND)
                    .a(DrawingCharacters.ISLAND_BORDER_VERTICAL)
                    .reset()
            );
        }

        cursor.restoreCursorPosition();
    }

}
