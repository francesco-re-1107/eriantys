package it.polimi.ingsw.client.cli;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This class represents the cursor of the CLI. It is designed to be used as a singleton.
 * It contains all the utility methods to draw on the terminal.
 */
public class Cursor {
    /**
     * The singleton instance of the cursor.
     */
    private static Cursor instance = null;

    /**
     * Width of the terminal.
     */
    public static final int WIDTH = 80;

    /**
     * Height of the terminal.
     */
    public static final int HEIGHT = 24;

    /**
     * Last user input string
     */
    private String inputString = "";

    /**
     * Used to keep track of the cursor movements, used by saveCursorPosition() and restoreCursorPosition().
     */
    private int relativeX = 0;

    /**
     * Used to keep track of the cursor movements, used by saveCursorPosition() and restoreCursorPosition().
     */
    private int relativeY = 0;

    /**
     * The thread currently listening to input.
     */
    private Thread threadListeningUserInput;

    /**
     * Initializes the cursor by installing the Ansi library on the terminal.
     */
    private Cursor() {
        AnsiConsole.systemInstall();
        System.setProperty("jansi.passthrough", "true");

        //start listening to user input on a new thread
        new Thread(() -> {
            while (true) {
                try {
                    inputString = new BufferedReader(new InputStreamReader(System.in)).readLine();

                    if(inputString == null)
                        inputString = "";

                } catch (IOException e) {}

                //when user input is received, notify all the waiting threads
                synchronized (this){
                    notifyAll();
                }
            }
        }).start();
    }

    /**
     * Get the singleton instance of the cursor.
     * @return the cursor
     */
    public static Cursor getInstance() {
        if (instance == null)
            instance = new Cursor();

        return instance;
    }

    /**
     * Clear screen by scrolling down the terminal for HEIGHT rows.
     */
    public void eraseScreen() {
        print(ansi().eraseScreen().reset());
    }

    /**
     * Clear screen by resetting each cell to default values.
     * Different from eraseScreen() because this method doesn't scroll the terminal.
     */
    public void clearScreen() {
        for(int i = 1; i <= HEIGHT; i++)
            clearRow(i);
    }

    /**
     * Move the cursor to absolute position.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void moveToXY(int x, int y) {
        print(ansi().cursor(y, x).reset());
    }

    /**
     * Uninstall the Ansi library from the terminal.
     */
    public void uninstall() {
        AnsiConsole.systemUninstall();
    }

    /**
     * This method listen for user input.
     * Only one thread can listen at a time.
     * If a new thread start listening to user input, the previous thread will be interrupted.
     *
     * @return the string entered by the user
     * @throws InterruptedException if the thread is interrupted because another thread is listening to user input
     */
    public String input() throws InterruptedException {
        threadListeningUserInput = Thread.currentThread();

        synchronized (this) {
            // notify all the waiting threads that a new thread is listening to user input
            // (only 1 thread at a time can listen to input)
            notifyAll();
        }

        // when a new thread is listening reset the input string
        inputString = "";

        synchronized (this) {
            // wait for the user to enter a string OR for another thread started listening to user input
            // therefore this thread will be interrupted
            wait();
        }

        // on wakeup, if the thread listening to input is this thread, then return the input string
        // otherwise, the thread was interrupted by another thread, so throw an exception
        if(threadListeningUserInput != Thread.currentThread())
            throw new InterruptedException();
        else
            return inputString;
    }

    /**
     * This method stops any thread listening to user input.
     */
    public void clearInput() {
        threadListeningUserInput = null;
    }

    /**
     * Listen for user input on a specific cell.
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return the string entered by the user
     * @throws InterruptedException if the thread is interrupted because another thread is listening to user input
     * @throws IOException error listening to user input
     */
    public String input(int x, int y) throws InterruptedException, IOException {
        moveToXY(x, y);
        return input();
    }

    /**
     * Print a string at the current position.
     * @param s the string to print
     */
    public void print(String s) {
        this.relativeX += realLength(s);
        System.out.print(s);
    }

    /**
     * Print a string starting at the given position.
     * @param s the string to print
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void print(String s, int x, int y) {
        moveToXY(x, y);
        print(s);
    }

    /**
     * Print a formatted string (Ansi) at the current position.
     * @param a the ansi string to print
     */
    public void print(Ansi a) {
        print(a.toString());
    }

    /**
     * Print a formatted string (Ansi) starting at the given position.
     * @param a the ansi string to print
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void print(Ansi a, int x, int y) {
        moveToXY(x, y);
        print(a);
    }

    /**
     * Print a string centered in the screen at the given row.
     * @param string the string to print
     * @param row the row to print the string
     */
    public void printCentered(String string, int row) {
        int x = (WIDTH - realLength(string)) / 2;
        print(string, x, row);
    }

    /**
     * Print a formatted string (Ansi) centered in the screen at the current position.
     * @param ansi the ansi string to print
     * @param row the row to print the string
     */
    public void printCentered(Ansi ansi, int row) {
        printCentered(ansi.toString(), row);
    }

    /**
     * Calculate the real length of a string.
     * In other words, the length of the string without the length of the ANSI escape sequences.
     * @param a the ansi string to calculate the length
     * @return the real length of the string
     */
    public int realLength(Ansi a) {
        return realLength(a.toString());
    }

    /**
     * Calculate the real length of a string.
     * In other words, the length of the string without the length of the ANSI escape sequences.
     * @param s the string to calculate the length
     * @return the real length of the string
     */
    public int realLength(String s) {
        return s.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]", "").length();
    }

    /**
     * Clear the given row by resetting it to default values.
     * @param row the row to clear
     */
    public void clearRow(int row) {
        print(ansi().fgDefault().bgDefault().a(" ".repeat(WIDTH)), 1, row);
    }

    /**
     * Clear the specified cell by resetting it to default values
     * @param x x coordinate of the cell to clear
     * @param y y coordinate of the cell to clear
     */
    public void clearCell(int x, int y) {
        moveToXY(x, y);
        print(ansi().fgDefault().bgDefault().a(" ").reset());
    }

    /**
     * Move the cursor relative to the current position.
     * @param xOffset the x offset, positive means right, negative means left
     * @param yOffset the y offset, positive means down, negative means up
     */
    public void moveRelative(int xOffset, int yOffset) {
        this.relativeX += xOffset;
        this.relativeY += yOffset;
        print(ansi().cursorRight(xOffset).cursorDown(yOffset).reset());
    }

    /**
     * Store the current position of the cursor.
     * To restore it later, use {@link #restoreCursorPosition()}.
     */
    public void saveCursorPosition() {
        this.relativeX = 0;
        this.relativeY = 0;
    }

    /**
     * Restore the cursor position to the last saved position.
     */
    public void restoreCursorPosition() {
        int x = this.relativeX;
        int y = this.relativeY;
        this.relativeX = 0;
        this.relativeY = 0;
        moveRelative(-x, -y);
    }

    /**
     * Paint the background of the terminal with the given color, starting at the given position
     * and ending at currentX + width and currentY + height.
     * @param color the color to paint
     * @param width the width of the area to paint
     * @param height the height of the area to paint
     */
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

    /**
     * Paint a portion of the background of the terminal with the given color.
     * @param color the color to paint
     * @param xMin the x coordinate of the top left corner of the area to paint (inclusive)
     * @param yMin the y coordinate of the top left corner of the area to paint (inclusive)
     * @param xMax the x coordinate of the bottom right corner of the area to paint (inclusive)
     * @param yMax the y coordinate of the bottom right corner of the area to paint (inclusive)
     */
    public void paintBackground(int color, int xMin, int yMin, int xMax, int yMax) {
        moveToXY(xMin, yMin);
        paintBackground(color, xMax-xMin+1, yMax-yMin+1);
    }
}
