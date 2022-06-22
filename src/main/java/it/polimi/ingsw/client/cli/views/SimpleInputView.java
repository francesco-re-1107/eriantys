package it.polimi.ingsw.client.cli.views;

import java.util.function.Consumer;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This view is used to ask the user for a generic input.
 */
public class SimpleInputView extends BaseView {

    /**
     * The message to display to the user.
     */
    protected String message;

    /**
     * The callback to call when the user has entered the input.
     */
    protected Consumer<String> listener;

    /**
     * Retry message display on error
     */
    private static final String ERROR_RETRY_MESSAGE = "[Premi invio per riprovare]";

    /**
     * Create a new SimpleInputView with the given message.
     * @param message the message to display to the user.
     */
    public SimpleInputView(String message) {
        this.message = message;
    }

    @Override
    public void draw() {
        //clear rows that are used
        cursor.clearRow(22);
        cursor.clearRow(23);
        cursor.clearRow(24);

        //print message
        cursor.print(message, 1, 22);
        cursor.moveToXY(1, 23);

        new Thread(() -> {
            try {
                listener.accept(cursor.input());
            } catch (Exception e) {}
        }).start();
    }

    /**
     * Show an error message to the user and ask again the user for input
     * @param error the error message to show to the user
     */
    public void showError(String error) {
        error += " " + ERROR_RETRY_MESSAGE;
        cursor.print(ansi().fgRed().a(error).reset(), 1, 23);
        new Thread(() -> {
            try {
                cursor.input();
                draw();
            } catch (Exception e) {}
        }).start();
    }

    /**
     * Set the callback to call when the user has entered the input.
     * @param listener the callback to call when the user has entered the input.
     */
    public void setListener(Consumer<String> listener) {
        this.listener = listener;
    }

    /**
     * Set the message to display to the user.
     * This method does not redraw the view.
     * @param message the message to display to the user.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
