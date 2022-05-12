package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.Tower;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.Collection;
import java.util.Map;

import static org.fusesource.jansi.Ansi.*;


public class Cursor {
    public static Cursor instance = null;

    public static final int ISLAND_WIDTH = 11;
    public static final int ISLAND_HEIGHT = 6;
    private static final Map<Student,Integer> STUDENT_COLOR_MAP =  Map.ofEntries(
            Map.entry(Student.YELLOW, Palette.STUDENT_YELLOW),
            Map.entry(Student.PINK, Palette.STUDENT_PINK),
            Map.entry(Student.RED, Palette.STUDENT_RED),
            Map.entry(Student.BLUE, Palette.STUDENT_BLUE),
            Map.entry(Student.GREEN, Palette.STUDENT_GREEN)
    );
    final int WIDTH = 80;
    final int HEIGHT = 24;

    public static Cursor getInstance(){
       if (instance == null) {
           instance = new Cursor();
       }
       return instance;
    }

    private Cursor() {
        AnsiConsole.systemInstall();
        System.setProperty("jansi.passthrough", "true");
        System.out.print( ansi().eraseScreen());
        for (int i = 0; i < HEIGHT; i++) {
            System.out.println(ansi().bg(Palette.ISLAND_BACKGROUND).a(" ".repeat(WIDTH)));
        }
    }
    public void clearScreen(){
        System.out.print(ansi().eraseScreen().reset());
    }
    // unused
    public void drawEdges(){
        moveToXY(1,1);
        System.out.print('╔');
        System.out.print("═".repeat(WIDTH-2));
        System.out.print('╗');
        for (int i = 2; i < HEIGHT-1; i++) {
            moveToXY(i, 1);
            System.out.print("║");
            moveToXY(i, WIDTH);
            System.out.print("║");
        }
        moveToXY(1,HEIGHT-1);
        System.out.print('╚');
    }

    // draws a list of islands
    public void drawIslands(Collection<ReducedIsland> islandList){
        int x_delimiter = 20;
        int incremental_y = 1;
        int count = islandList.size(); // TODO: rearrange island based on count
        var iterator = islandList.iterator();

        ReducedIsland island = iterator.next();
        for (int i = 0; i < 5; i++) {
            drawIsland(x_delimiter + ((ISLAND_WIDTH+1)*i), incremental_y, island, String.valueOf(i));
            island = iterator.next();
        }
        incremental_y += ISLAND_HEIGHT + 1;

        drawIsland(x_delimiter + 4 * (ISLAND_WIDTH + 1), incremental_y, island, String.valueOf(5));
        island = iterator.next();

        //TODO: fix
        incremental_y += ISLAND_HEIGHT + 1;
        for (int i = 6; i < count - 1; i++) {
            drawIsland(x_delimiter - ((ISLAND_WIDTH + 1) * i), incremental_y, island, String.valueOf(i));
            island = iterator.next();
        }
        incremental_y -= ISLAND_HEIGHT + 1;
        drawIsland(x_delimiter, incremental_y, island, String.valueOf(9));
    }

    private void drawWithBg(String content){
        System.out.print(ansi().bg(Palette.ISLAND_BACKGROUND).a(content).reset());
    }

    // draws and island starting at
    private void drawIsland(int x0, int y0, ReducedIsland island, String id){
        moveToXY(x0,y0);

        String border = "╭" + "─".repeat(ISLAND_WIDTH -2) + "╮";
        drawWithBg(border);

        moveToXY(x0,y0+1);
        drawWithBg("| " + id);
        moveToXY(x0+ ISLAND_WIDTH -1,y0+1);
        drawWithBg("|");

        moveToXY(x0,y0+2);
        drawWithBg("|");
        String s = "";
        for (var x: island.students().getStudents().entrySet()) {
            s += ansi().bg(Palette.ISLAND_BACKGROUND).fg(STUDENT_COLOR_MAP.get(x.getKey())).a("●".repeat(x.getValue())).reset();
        }
        System.out.println(s);
        moveToXY(x0+ ISLAND_WIDTH -1,y0+2);
        drawWithBg("|");

        for (int i = 0; i < ISLAND_HEIGHT - 3; i++) {
            moveToXY(x0,y0+3+i);
            drawWithBg("|" + " ".repeat(ISLAND_WIDTH -2) + "|");
        }

        moveToXY(x0,y0+ ISLAND_HEIGHT -1);
        drawWithBg("| ");


        System.out.println( ansi().fg(getTowerAnsiColor(island.towerColor())).bg(117).a("♜".repeat(island.towersCount())).reset());
        moveToXY(x0+ ISLAND_WIDTH -1,y0+ ISLAND_HEIGHT -1);
        drawWithBg("|");

        moveToXY(x0,y0+ ISLAND_HEIGHT);

        border = "╰" + "─".repeat(ISLAND_WIDTH -2) + "╯";
        drawWithBg(border);
    }

    private int getTowerAnsiColor(Tower color){
        return switch (color){
            case BLACK -> Palette.TOWER_BLACK;
            case WHITE -> Palette.TOWER_WHITE;
            case GREY -> Palette.TOWER_GREY;
        };
    }

    private void moveToXY(int x, int y){
        System.out.print(ansi().cursor(y, x).reset());
    }

    public void end(){
        AnsiConsole.systemUninstall();
    }

    public void printPrompt(String prompt){
        printPrompt(prompt, "");
    }

    public void printPrompt(String prompt, String default_resp){
        Ansi s = ansi().bold().a("%s".formatted(prompt)).boldOff();
        if (!default_resp.isEmpty()){
            s = s.a(" (default %s)".formatted(default_resp));
        }
        s = s.a(": ").reset();
        System.out.println(s);
    }

    public void printBold(String s){
        System.out.println(ansi().bold().a(s).reset());
    }


}
