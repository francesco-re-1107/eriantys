package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.Tower;

import java.util.List;
import java.util.Map;

public class Palette {
    public static final int STUDENT_RED = 9;
    public static final int STUDENT_GREEN = Utils.isWindows() ? 10 : 36;
    public static final int STUDENT_YELLOW = Utils.isWindows() ? 3 : 220;
    public static final int STUDENT_PINK = Utils.isWindows() ? 13 : 169;
    public static final int STUDENT_BLUE = Utils.isWindows() ? 12 : 39;

    public static final int ISLAND_BORDER = 22;
    public static final int ISLAND_BACKGROUND = Utils.isWindows() ? 121 : 117;
    public static final int DASHBOARD_BACKGROUND = 236;

    public static final int CLOUD_BACKGROUD = 15;

    public static final int TOWER_CONTRAST_BACKGROUND_WIN = 110;
    public static final int TOWER_BLACK = 0;
    public static final int TOWER_WHITE = 15;
    public static final int TOWER_GREY = 244;

    public static final int MOTHER_NATURE = 130;

    public static final int ERIANTYS = Utils.isWindows() ? 198 : 99;

    public static final int WHITE = 15;

    public static final int WIN = 34;

    public static final int LOSE = 124;

    public static final int TIE = 27;

    public static final int PAUSED = 220;

    public static final int TERMINATED = 25;
    public static final Map<Student, Integer> STUDENTS_COLOR_MAP = Map.ofEntries(
            Map.entry(Student.YELLOW, Palette.STUDENT_YELLOW),
            Map.entry(Student.PINK, Palette.STUDENT_PINK),
            Map.entry(Student.RED, Palette.STUDENT_RED),
            Map.entry(Student.BLUE, Palette.STUDENT_BLUE),
            Map.entry(Student.GREEN, Palette.STUDENT_GREEN)
    );
    public static final Map<Tower, Integer> TOWERS_COLOR_MAP = Map.ofEntries(
            Map.entry(Tower.BLACK, Palette.TOWER_BLACK),
            Map.entry(Tower.WHITE, Palette.TOWER_WHITE),
            Map.entry(Tower.GREY, Palette.TOWER_GREY)
    );

    public static final List<Integer> RAINBOW = List.of(196, 214, 226, 46, 39);

}
