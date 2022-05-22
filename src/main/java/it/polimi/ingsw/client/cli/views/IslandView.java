package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.cli.DrawingCharacters;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;

import static org.fusesource.jansi.Ansi.ansi;

public class IslandView extends BaseView {

    public static final int ISLAND_WIDTH = 11;

    public static final int ISLAND_HEIGHT = 7;

    private final ReducedIsland island;

    private final boolean motherNature;

    private final int index;

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

    private void drawTowers() {
        cursor.saveCursorPosition();
        cursor.moveRelative(1, ISLAND_HEIGHT - 2);

        cursor.print(ansi()
                .fg(Palette.WIN)//Palette.TOWERS_COLOR_MAP.get(island.towerColor()))
                .bg(Palette.ISLAND_BACKGROUND)
                .a(DrawingCharacters.TOWER.repeat(island.towersCount()))
        );

        cursor.restoreCursorPosition();
    }

    private void drawStudents() {
        cursor.saveCursorPosition();
        cursor.moveRelative(1, 2);

        if(island.students().getSize() > 24) { //max students an island can hold
            //do something
        } else {

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

        cursor.restoreCursorPosition();
    }

    private void drawIslandInfo() {
        cursor.saveCursorPosition();

        cursor.moveRelative(1, 1);
        cursor.print(ansi()
                .fgBlack()
                .bg(Palette.ISLAND_BACKGROUND)
                .bold()
                .a(String.format("I%d ", index)));

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

    private void drawBorders() {
        cursor.saveCursorPosition();

        //upper border
        cursor.print(ansi()
                .fg(Palette.ISLAND_BORDER)
                .bg(Palette.ISLAND_BACKGROUND)
                .a("╭" + "─".repeat(ISLAND_WIDTH - 2) + "╮")
        );

        //right border
        for(int i = 0; i < ISLAND_HEIGHT - 2; i++) {
            cursor.moveRelative(-1, 1);
            cursor.print(ansi()
                    .fg(Palette.ISLAND_BORDER)
                    .bg(Palette.ISLAND_BACKGROUND)
                    .a("│")
            );
        }

        cursor.moveRelative(-ISLAND_WIDTH, 1);

        //lower border
        cursor.print(ansi()
                .fg(Palette.ISLAND_BORDER)
                .bg(Palette.ISLAND_BACKGROUND)
                .a("╰" + "─".repeat(ISLAND_WIDTH - 2) + "╯")
        );

        cursor.moveRelative(-ISLAND_WIDTH + 1, 0);

        //left border
        for(int i = 0; i < ISLAND_HEIGHT - 2; i++) {
            cursor.moveRelative(-1, -1);
            cursor.print(ansi()
                    .fg(Palette.ISLAND_BORDER)
                    .bg(Palette.ISLAND_BACKGROUND)
                    .a("│")
            );
        }

        cursor.moveRelative(-1, -1);

        cursor.restoreCursorPosition();
    }

}
