package it.polimi.ingsw.client.cli.views;

import java.util.function.IntConsumer;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This view is used to ask user to input an integer value.
 */
public class IntegerInputView extends BaseView{

    /**
     * Minimum value of the integer (inclusive).
     */
    private int min;

    /**
     * Maximum value of the integer (inclusive).
     */
    private int max;

    /**
     * Whether the input needs to meet the minimum and maximum conditions.
     */
    private boolean isConstrained = false;

    /**
     * Called when the user inputs a valid integer value.
     */
    private IntConsumer listener;

    /**
     * Message to be displayed to the user.
     */
    private String message;

    /**
     * Error message displayed when the input is not valid because of constraints.
     */
    private static final String INVALID_NUMBER_ERROR_MESSAGE = "Numero non valido (min: %d, max: %d)";

    /**
     * Error message displayed when the input is not valid because it is not an integer.
     */
    private static final String INVALID_FORMAT_ERROR_MESSAGE = "Formato non valido (atteso un numero)";

    /**
     * Retry message display on error
     */
    private static final String ERROR_RETRY_MESSAGE = "[Premi invio per riprovare]";

    /**
     * Create an IntegerInputView with a minimum and a maximum value
     * @param message the message to show
     * @param min minimum value (inclusive)
     * @param max maximum value (inclusive)
     */
    public IntegerInputView(String message, int min, int max) {
        this.message = message;
        this.min = min;
        this.max = max;
        isConstrained = true;
    }

    /**
     * Create an IntegerInputView without a minimum and a maximum value
     * @param message the message to show
     */
    public IntegerInputView(String message) {
        this.message = message;
    }

    @Override
    public void draw() {
        cursor.clearRow(22);
        cursor.clearRow(23);
        cursor.clearRow(24);
        cursor.print(message, 1, 22);
        cursor.moveToXY(1, 23);

        //TODO improve
        new Thread(() -> {
            try {
                var input = cursor.input();
                try {
                    var choice = Integer.parseInt(input);

                    if (isConstrained && (choice < min || choice > max)) {
                        showError(String.format(INVALID_NUMBER_ERROR_MESSAGE, min, max));
                        return;
                    }
                    listener.accept(choice);
                } catch (NumberFormatException e) {
                    showError(INVALID_FORMAT_ERROR_MESSAGE);
                }
            } catch (Exception e) {}
        }).start();
    }

    /**
     * Show an error message to the user and retry the input.
     * @param error the error message to show
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
     * Set the listener to be called when the user inputs a valid integer value.
     * @param listener the listener to be called
     */
    public void setListener(IntConsumer listener) {
        this.listener = listener;
    }
}
