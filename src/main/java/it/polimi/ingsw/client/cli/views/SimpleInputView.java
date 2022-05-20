package it.polimi.ingsw.client.cli.views;

import java.util.function.Consumer;

import static org.fusesource.jansi.Ansi.ansi;

public class SimpleInputView extends BaseView {

    private String message;
    private Thread thread;

    private Consumer<String> listener;

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

        thread = new Thread(() -> listener.accept(cursor.input()));
        thread.start();
    }

    public void showError(String error) {
        error += " [Premi invio per riprovare]";
        cursor.print(ansi().fgRed().a(error).reset(), 1, 23);
        cursor.input();
        draw();
    }

    public void stop() {
        try {
            thread.interrupt();
        } catch (Exception e) { }
    }

    public void setListener(Consumer<String> listener) {
        this.listener = listener;
    }
}
