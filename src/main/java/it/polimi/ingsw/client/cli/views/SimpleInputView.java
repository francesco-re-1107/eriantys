package it.polimi.ingsw.client.cli.views;

import java.util.function.Consumer;

import static org.fusesource.jansi.Ansi.ansi;

public class SimpleInputView extends BaseView {

    protected String message;

    protected Consumer<String> listener;

    public SimpleInputView(String message) {
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
                listener.accept(cursor.input());
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

    public void setListener(Consumer<String> listener) {
        this.listener = listener;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
