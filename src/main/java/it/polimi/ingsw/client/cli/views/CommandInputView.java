package it.polimi.ingsw.client.cli.views;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This view shows an input view that listen for the specified commands.
 */
public class CommandInputView extends BaseView {

    /**
     * Message to show to the user
     */
    private String message;

    /**
     * Whether the supported commands should be printed to the user or not
     */
    private final boolean printCommands;

    /**
     * Commands supported with their listener
     */
    private final Map<Command, CommandListener> listeners = new HashMap<>(); // keyword -> listener

    /**
     * Error message shown to the user when input command is not valid
     */
    private static final String ERROR_COMMAND_NOT_RECOGNIZED = "Comando non riconosciuto";

    /**
     * Error message shown to the user to retry the input
     */
    private static final String ERROR_RETRY_MESSAGE = "[Premi invio per riprovare]";

    /**
     * Create a CommandInputView with the specified message and commands printing enabled
     * @param message the message to show to the user
     */
    public CommandInputView(String message) {
        this(message, true);
    }

    /**
     * Create a CommandInputView with the specified parameters
     * @param message the message to show to the user
     * @param printCommands whether the supported commands should be printed to the user or not
     */
    public CommandInputView(String message, boolean printCommands) {
        this.message = message;
        this.printCommands = printCommands;
    }

    /**
     * Set message to show to the user and redraw the view
     * @param message the message to show to the user
     */
    public void setMessage(String message){
        this.message = message;
        draw();
    }

    @Override
    public void draw() {
        cursor.clearRow(22);
        cursor.clearRow(23);

        if(printCommands){
            cursor.print(message + " [", 1, 22);
            var first = false;
            for (var c : listeners.keySet()) {
                if(first)
                    cursor.print(" | ");
                cursor.print(ansi().fgBrightYellow().a(c.triggerWord).reset());
                cursor.print(": ");
                cursor.print(c.description);
                first = true;
            }
            cursor.print("]");
        } else {
            cursor.print(message, 1 ,22);
        }

        cursor.moveToXY(1, 23);

        //TODO improve
        new Thread(() -> {
            try {
                var cmd = cursor.input();
                try {
                    var elements = Arrays.asList(cmd.split(" "));

                    var commandListeners = listeners.entrySet().stream()
                            .filter(e -> e.getKey().triggerWord.equals(elements.get(0)))
                            .map(Map.Entry::getValue)
                            .toList();

                    if(commandListeners.isEmpty())
                        showError(ERROR_COMMAND_NOT_RECOGNIZED);
                    else
                        commandListeners.forEach(l -> l.onCommand(
                                elements.get(0),
                                elements.subList(1, elements.size())
                        ));

                } catch (Exception e) {
                    showError(ERROR_COMMAND_NOT_RECOGNIZED);
                }
            }catch (Exception e) {}
        }).start();
    }

    /**
     * Show an error message to the user
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
     * Add a command listener to the view
     * @param triggerWord the trigger word of the command
     * @param description a description of the command
     * @param listener the listener to call when the command is triggered
     */
    public void addCommandListener(String triggerWord, String description, CommandListener listener) {
        this.listeners.put(new Command(triggerWord, description), listener);
    }

    /**
     * Remove a command listener from this view
     * @param triggerWord the trigger word of the command to remove
     */
    public void removeCommandListener(String triggerWord) {
        this.listeners.entrySet().removeIf(e -> e.getKey().triggerWord.equals(triggerWord));
    }

    /**
     * Listener called when a command is triggered
     */
    public interface CommandListener {
        void onCommand(String command, List<String> args);
    }

    /**
     * Representation of a command, used internally
     * @param triggerWord the trigger word of the command
     * @param description a description of the command
     */
    private record Command(String triggerWord, String description) { }
}
