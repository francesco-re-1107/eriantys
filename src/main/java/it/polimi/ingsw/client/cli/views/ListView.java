package it.polimi.ingsw.client.cli.views;

import org.fusesource.jansi.Ansi;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Generic list view for the CLI.
 * @param <T> the type of the list items
 */
public class ListView<T> extends BaseView{

    /**
     * Items of the list to be displayed.
     */
    private List<T> listItems;

    /**
     * Listener called when the user selects an item.
     * <T> the type of the list items
     * Integer the index of the selected item
     */
    private BiConsumer<T, Integer> onSelectionListener;

    /**
     * Input view used internally to get the user input.
     */
    private IntegerInputView inputView;

    /**
     * Title of the list.
     * Displayed above the list, centered in the screen.
     */
    private final String title;

    /**
     * Message displayed to the user when asking for input.
     */
    private final String prompt;

    /**
     * Message displayed to the user when the list is empty.
     */
    private final String listEmpty;

    /**
     * Whether the list items are displayed centered in the screen or not.
     */
    private final boolean centered;

    /**
     * Renderer for the list items.
     * Called when displaying an item, given the item this function returns the formatted string to be displayed.
     */
    private final Function<T, Ansi> customRenderer;

    /**
     * Number of rows allocated for every list item.
     */
    private final int rowsPerItem;

    /**
     * Create a ListView with the given parameters.
     *
     * @param listItems the items of the list
     * @param customRenderer a function that returns the formatted string (ansi) to be displayed for a given item
     * @param title the title of the list
     * @param prompt the message displayed to the user when asking for input
     * @param listEmpty the message displayed to the user when the list is empty
     * @param centered whether the list items are displayed centered in the screen or not
     * @param rowsPerItem number of rows allocated for every list item
     */
    public ListView(List<T> listItems, Function<T, Ansi> customRenderer, String title, String prompt, String listEmpty, boolean centered, int rowsPerItem){
        super();
        this.title = title;
        this.prompt = prompt;
        this.listEmpty = listEmpty;
        this.listItems = listItems;
        this.customRenderer = customRenderer;
        this.inputView = new IntegerInputView(prompt, 1, listItems.size());
        this.centered = centered;
        this.rowsPerItem = rowsPerItem;
    }

    @Override
    public void draw() {
        cursor.clearInput();
        cursor.clearScreen();
        cursor.printCentered(title, 1);

        if(listItems.isEmpty()){
            cursor.printCentered(listEmpty, 10);
            cursor.moveToXY(1, 23);
        } else {
            this.inputView.setListener(input -> {
                if(onSelectionListener == null) return;

                onSelectionListener.accept(listItems.get(input - 1), input - 1);
            });

            //print elements
            for (int i = 0; i < Math.min(listItems.size(), 10); i++) {
                var itemView = ansi().a("[")
                        .fgBrightYellow()
                        .a(i + 1)
                        .fgDefault()
                        .a("] ")
                        .a(customRenderer.apply(listItems.get(i)))
                        .reset();

                if (centered)
                    cursor.printCentered(itemView, 3 + i * 2);
                else
                    cursor.print(itemView, 5, 3 + i * rowsPerItem);

                cursor.moveRelative(0, 1);
            }

            inputView.draw();
        }
    }

    /**
     * Set new list items and redraw the view.
     * @param listItems the new list items
     */
    public void setListItems(List<T> listItems) {
        this.listItems = listItems;

        this.inputView = new IntegerInputView(prompt, 1, listItems.size());
        draw();
    }

    /**
     * Show an error message to the user.
     * @param error the error message
     */
    public void showError(String error) {
        inputView.showError(error);
    }

    /**
     * Set a listener that will be called when the user selects an item.
     * @param onSelectionListener the listener
     */
    public void setOnSelectionListener(BiConsumer<T, Integer> onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }
}
