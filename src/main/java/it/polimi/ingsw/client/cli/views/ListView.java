package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.cli.Palette;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.fusesource.jansi.Ansi.ansi;

public class ListView<T> extends BaseView{

    private List<T> listItems;
    private BiConsumer<T, Integer> listener;
    private IntegerInputView inputView;
    private String title;
    private final String prompt;
    private String listEmpty;
    private final Function<T, Ansi> customRenderer;

    public ListView(Function<T, Ansi> customRenderer) {
        this(new ArrayList<>(), customRenderer, "", "Scegli opzione", "N/A");
    }

    public ListView(List<T> listItems, Function<T, Ansi> customRenderer, String title, String prompt, String listEmpty){
        super();
        this.title = title;
        this.prompt = prompt;
        this.listEmpty = listEmpty;
        this.listItems = listItems;
        this.customRenderer = customRenderer;
        this.inputView = new IntegerInputView(prompt, 1, listItems.size());
    }

    @Override
    public void draw() {
        cursor.clearScreen();
        cursor.printCentered(title, 1);

        if(listItems.isEmpty()){
            cursor.printCentered(listEmpty, 10);
            cursor.moveToXY(1, 23);
        } else {
            this.inputView.setListener(input -> {
                if(listener == null) return;

                listener.accept(listItems.get(input - 1), input - 1);
            });

            //print elements
            for (int i = 0; i < Math.min(listItems.size(), 10); i++) {
                cursor.printCentered(ansi()
                                .a("[")
                                .fgBrightYellow()
                                .a(i + 1)
                                .fg(Palette.WHITE)
                                .a("] ")
                                .a(customRenderer.apply(listItems.get(i)))
                                .reset(),
                        3 + i * 2
                );
                cursor.moveRelative(0, 1);
            }

            inputView.draw();
        }
    }

    public void setListItems(List<T> listItems) {
        this.listItems = listItems;

        this.inputView = new IntegerInputView(prompt, 1, listItems.size());
        draw();
    }

    public void showError(String error) {
        inputView.showError(error);
    }

    public void setListener(BiConsumer<T, Integer> listener) {
        this.listener = listener;
    }
}
