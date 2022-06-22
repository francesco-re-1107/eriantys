package it.polimi.ingsw.client.cli.views;

import java.util.function.Consumer;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This class represents the view for the boolean input. [yes/no]
 */
public class BooleanInputView extends BaseView{

    /**
     * This callback is called when the user inputs a valid answer.
     */
    private Consumer<Boolean> listener;

    /**
     * Message displayed to the user.
     */
    private String message;

    /**
     * Retry message display on error
     */
    private static final String ERROR_RETRY_MESSAGE = "[Premi invio per riprovare]";

    /**
     * Creates a new BooleanInputView with the specified message.
     * @param message the message to display to the user.
     */
    public BooleanInputView(String message) {
        this.message = message;
    }

    @Override
    public void draw() {
        cursor.clearRow(22);
        cursor.clearRow(23);
        cursor.clearRow(24);
        cursor.print(message, 1, 22);
        cursor.moveToXY(1, 23);

        new Thread(() -> {
            try {
                var input = cursor.input().trim().toLowerCase();

                if (input.equals("s")) {
                    listener.accept(true);
                } else if (input.equals("n")) {
                    listener.accept(false);
                } else {
                    showError("Formato non valido");
                }
            } catch (Exception e) {}
        }).start();
    }

    /**
     * Shows an error message to the user.
     * @param error the error message to show.
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
     * Sets the listener for the input.
     * @param listener the listener to set.
     */
    public void setListener(Consumer<Boolean> listener) {
        this.listener = listener;
    }
}
