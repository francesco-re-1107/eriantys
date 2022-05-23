package it.polimi.ingsw.client.cli.views;

import java.util.function.IntConsumer;

import static org.fusesource.jansi.Ansi.ansi;

public class IntegerInputView extends BaseView{

    private int min;

    private int max;

    private boolean isConstrained = false;

    private IntConsumer listener;

    private String message;

    /**
     * Create an IntegerInputView with a minimum and a maximum value
     * @param message
     * @param min minimum value (inclusive)
     * @param max maximum value (inclusive)
     */
    public IntegerInputView(String message, int min, int max) {
        this.message = message;
        this.min = min;
        this.max = max;
        isConstrained = true;
    }

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

        new Thread(() -> {
            try {
                var input = cursor.input();
                try {
                    var choice = Integer.parseInt(input);

                    if (isConstrained && (choice < min || choice > max)) {
                        showError("Selezione non valida");
                        return;
                    }
                    listener.accept(choice);
                } catch (NumberFormatException e) {
                    showError("Formato non valido");
                }
            } catch (Exception e) {}
        }).start();
    }

    public void showError(String error) {
        error += " [Premi invio per riprovare]";
        cursor.print(ansi().fgRed().a(error).reset(), 1, 23);
        new Thread(() -> {
            try {
                cursor.input();
                draw();
            } catch (Exception e) {}
        }).start();
    }

    public void setListener(IntConsumer listener) {
        this.listener = listener;
    }
}
