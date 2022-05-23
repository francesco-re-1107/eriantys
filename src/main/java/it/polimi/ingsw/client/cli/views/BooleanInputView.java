package it.polimi.ingsw.client.cli.views;

import java.util.function.Consumer;

import static org.fusesource.jansi.Ansi.ansi;

public class BooleanInputView extends BaseView{

    private Consumer<Boolean> listener;

    private String message;

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

    public void setListener(Consumer<Boolean> listener) {
        this.listener = listener;
    }
}
