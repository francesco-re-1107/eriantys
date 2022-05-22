package it.polimi.ingsw.client.cli;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class Cursor {
    public static Cursor instance = null;

    public static final int WIDTH = 80;
    public static final int HEIGHT = 24;
    private int relativeX = 0;
    private int relativeY = 0;

    public static Cursor getInstance() {
        if (instance == null) {
            instance = new Cursor();
        }
        return instance;
    }

    private Cursor() {
        AnsiConsole.systemInstall();
        System.setProperty("jansi.passthrough", "true");
    }

    public void eraseScreen() {
        print(ansi().eraseScreen().reset());
    }

    public void clearScreen() {
        for(int i = 1; i <= HEIGHT; i++)
            clearRow(i);
    }

    // unused
    /*public void drawEdges() {
        moveToXY(0, 1);
        System.out.print('╔');
        System.out.print("═".repeat(WIDTH - 2));
        System.out.print('╗');
        for (int i = 2; i < HEIGHT - 1; i++) {
            moveToXY(i, 1);
            System.out.print("║");
            moveToXY(i, WIDTH);
            System.out.print("║");
        }
        moveToXY(1, HEIGHT - 1);
        System.out.print('╚');
    }


    // draws a list of islands
    public void drawIslands(Collection<ReducedIsland> islandList) {
        int incremental_y = 2;
        int count = islandList.size(); // TODO: rearrange island based on count
        var iterator = islandList.iterator();

        ReducedIsland island = iterator.next();
        for (int i = 0; i < 5; i++) {
            drawIsland(BOARD_DELIMITER + ((ISLAND_WIDTH + 1) * i), incremental_y, island, String.valueOf(i));
            island = iterator.next();
        }
        incremental_y += ISLAND_HEIGHT + 1;

        drawIsland(BOARD_DELIMITER + 4 * (ISLAND_WIDTH + 1), incremental_y, island, String.valueOf(5));
        island = iterator.next();

        // TODO: fix
        incremental_y += ISLAND_HEIGHT + 1;
        for (int i = 6; i < count - 1; i++) {
            drawIsland(BOARD_DELIMITER + ((ISLAND_WIDTH + 1) * (count - 2 - i)), incremental_y, island, String.valueOf(i));
            island = iterator.next();
        }
        incremental_y -= ISLAND_HEIGHT + 1;
        drawIsland(BOARD_DELIMITER, incremental_y, island, String.valueOf(11));
    }

    private void drawWithBg(String content) {
        System.out.print(ansi().bg(Palette.ISLAND_BACKGROUND).a(content).reset());
    }

    private void drawWithBgAndContrast(String content) {
        System.out.print(ansi().fgBlack().bg(Palette.ISLAND_BACKGROUND).a(content).reset());
    }

    public void printWithFgColor(String s, int color) {
        print(ansi().fg(color).a(s).reset());
    }

    public void drawBoard(List<ReducedPlayer> players, ReducedRound round, ReducedGame game){
        //ReducedRound round = game.currentRound();
        int incremental_y = 2;
        for (var p : players) {  
            //name
            moveToXY(LEFT_MARGIN, incremental_y);
            printBold(p.nickname());
            if (round.currentPlayer().equals(p.nickname())) {
                moveToXY(LEFT_MARGIN - 2, incremental_y);
                printBold("➤");
            }
            //board
            incremental_y++;
            for (Student color : Student.values()) {
                if (game.currentProfessors().containsKey(color)){
                    if (game.currentProfessors().get(color).equals(p.nickname())) {
                        moveToXY(LEFT_MARGIN - 1, incremental_y);
                        System.out.print(ansi().fg(STUDENT_COLOR_MAP.get(color)).a("⬣").reset());  
                    }
                }
                moveToXY(LEFT_MARGIN, incremental_y);
                System.out.print(ansi().fg(STUDENT_COLOR_MAP.get(color)).a("●").reset());
                System.out.print("%d(%d)".formatted(
                    p.school().getCountForStudent(color),
                    p.entrance().getCountForStudent(color)));
                incremental_y++;
            }
            //current card
            moveToXY(LEFT_MARGIN + 8, incremental_y - 5);
            System.out.print("<%d|%d>".formatted(5,3)); //TODO
            //towers
            moveToXY(LEFT_MARGIN + 8, incremental_y - 3);
            System.out.print(ansi()
                .bg(getTowerAnsiColor(p.towerColor()))
                .fg(Palette.TOWER_CONTRAST_BACKGROUND)
                .a("♜").reset().a("%d/%d".formatted(5,3))); //TODO
            //coins
            if (game.expertMode()) {  
                moveToXY(LEFT_MARGIN + 8, incremental_y - 1);
                System.out.print("$%d".formatted(2)); //TODO
            }

            incremental_y++;
        }

    }

    private int getTowerAnsiColor(Tower color) {
        return switch (color) {
            case BLACK -> Palette.TOWER_BLACK;
            case WHITE -> Palette.TOWER_WHITE;
            case GREY -> Palette.TOWER_GREY;
        };
    }

    public void printPrompt(String prompt) {
        printPrompt(prompt, "");
    }

    public void printPrompt(String prompt, String default_resp) {
        moveToXY(1, HEIGHT - 1);
        Ansi s = ansi().bold().a("%s".formatted(prompt)).boldOff();
        if (!default_resp.isEmpty()) {
            s = s.a(" (default %s)".formatted(default_resp));
        }
        s = s.a(": ").reset();
        System.out.println(s);
        System.out.print("> ");
    }

    public void printBold(String s) {
        System.out.println(ansi().bold().a(s).reset());
    }

    public int intInput() {
        while (true){
            var s = new Scanner(System.in).nextLine();
            try {
                return Integer.parseInt(s);
            }catch (Exception e){
                showError("Expected a number");
            }
        }
    }

    public void showError(String err) {
        printWithFgColor(err, Palette.RED_TEXT);
    }
    */

    public void moveToXY(int x, int y) {
        print(ansi().cursor(y, x).reset());
    }

    public void end() {
        AnsiConsole.systemUninstall();
    }

    public String input() {
        return new Scanner(System.in).nextLine();
    }

    public String input(int x, int y) {
        moveToXY(x, y);
        return input();
    }

    public void print(String s) {
        this.relativeX += realLength(s);
        System.out.print(s);
    }

    public void print(String s, int x, int y) {
        moveToXY(x, y);
        print(s);
    }

    public void print(Ansi s) {
        print(s.toString());
    }

    public void print(Ansi s, int x, int y) {
        moveToXY(x, y);
        print(s);
    }

    public void println(String s){
        print(ansi().a(s).cursorDownLine().reset());
    }

    public void printCentered(String string, int row) {
        int x = (WIDTH - realLength(string)) / 2;
        print(string, x, row);
    }

    public void printCentered(Ansi ansi, int row) {
        printCentered(ansi.toString(), row);
    }

    public int realLength(Ansi s) {
        return realLength(s.toString());
    }

    public int realLength(String s) {
        return s.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").length();
    }

    public void clearRow(int row) {
        print(ansi().fgDefault().bgDefault().a(" ".repeat(WIDTH)), 1, row);
    }

    public void moveRelative(int xOffset, int yOffset) {
        this.relativeX += xOffset;
        this.relativeY += yOffset;
        print(ansi().cursorRight(xOffset).cursorDown(yOffset).reset());
    }

    public void saveCursorPosition() {
        this.relativeX = 0;
        this.relativeY = 0;
    }

    public void restoreCursorPosition() {
        int x = this.relativeX;
        int y = this.relativeY;
        moveRelative(-x, -y);
    }

    public void paintBackground(int color, int width, int height) {
        for (int i = 0; i < height; i++) {
            print(ansi()
                    .bg(color)
                    .a(DrawingCharacters.EMPTY.repeat(width))
                    .reset()
            );
            moveRelative(-width, 1);
        }
    }

    public void paintBackground(int color, int xMin, int yMin, int xMax, int yMax) {
        moveToXY(xMin, yMin);
        paintBackground(color, xMax-xMin+1, yMax-yMin+1);
    }
}
