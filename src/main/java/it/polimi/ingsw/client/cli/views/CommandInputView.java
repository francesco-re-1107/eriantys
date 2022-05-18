package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.cli.Command;

import java.util.function.Consumer;

public class CommandInputView extends BaseView {

    private Consumer<Command> listener;

    @Override
    public void draw() {
        cursor.print("Enter command: ", 1, 23);
        cursor.moveToXY(1, 24);

        new Thread(() -> {
            var cmd = cursor.input();
            listener.accept(Command.parse(cmd));
        }).start();
    }

    public void setCommandListener(Consumer<Command> listener) {
        this.listener = listener;
    }


}
