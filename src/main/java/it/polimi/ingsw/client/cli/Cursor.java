package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.Constants;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.fusesource.jansi.Ansi.ansi;

public class Cursor {
    private static Cursor instance = null;

    public static final int WIDTH = 80;
    public static final int HEIGHT = 24;
    private int relativeX = 0;
    private int relativeY = 0;

    private Set<Thread> threadsListeningUserInput = new HashSet<>();

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

    /*
    private void drawWithBg(String content) {
        System.out.print(ansi().bg(Palette.ISLAND_BACKGROUND).a(content).reset());
    }

    private void drawWithBgAndContrast(String content) {
        System.out.print(ansi().fgBlack().bg(Palette.ISLAND_BACKGROUND).a(content).reset());
    }

    public void printWithFgColor(String s, int color) {
        print(ansi().fg(color).a(s).reset());
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

    /**
     * This method listen for user input.
     * Only one thread can listen at a time. If a thread is already listening, it will be interrupted.
     *
     * @return the string entered by the user
     * @throws InterruptedException
     * @throws IOException
     */
    public String input() throws InterruptedException, IOException {
        //stops all threads listening to input
        threadsListeningUserInput.forEach(Thread::interrupt);

        //clear system in before reading
        System.in.read(new byte[System.in.available()]);

        threadsListeningUserInput.add(Thread.currentThread());
        while(true){
            if(System.in.available() > 0)
            {
                var buff = new byte[4096];
                int n = System.in.read(buff);
                var str = new String(buff, 0, n, StandardCharsets.UTF_8)
                        .replaceAll("\n", "");

                threadsListeningUserInput.remove(Thread.currentThread());

                return str;
            } else {
                Thread.sleep(Constants.CLI_READ_POLLING_INTERVAL);
            }
        }
    }

    public String input(int x, int y) throws InterruptedException, IOException {
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
