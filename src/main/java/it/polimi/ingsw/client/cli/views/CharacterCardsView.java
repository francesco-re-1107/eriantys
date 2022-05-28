package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.server.model.Character;
import org.fusesource.jansi.Ansi;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This view shows a list of character cards that the player can buy.
 */
public class CharacterCardsView extends BaseView {

    /**
     * ListView used internally to show the list of character cards.
     */
    private final ListView<Map.Entry<Character, Integer>> listView;

    /**
     * Character card renderer
     */
    Function<Map.Entry<Character, Integer>, Ansi> renderer = entry -> {
        var title = Constants.CHARACTER_NAMES.get(entry.getKey());
        var description = Constants.CHARACTER_DESCRIPTIONS.get(entry.getKey());

        return ansi()
                .bold()
                .fgBrightMagenta()
                .a(title)
                .fgDefault()
                .bold()
                .a(" (" + entry.getKey().getCost(entry.getValue()) + "$)")
                .reset()
                .fgDefault()
                .newline()
                .a(Utils.wrapWithSpaces(description, Cursor.WIDTH, 8))
                .reset();
    };

    /**
     * Create a new CharacterCardsView with the given list of playable character cards.
     * @param playableCards the list of playable character cards.
     */
    public CharacterCardsView(List<Map.Entry<Character, Integer>> playableCards) {
        this.listView = new ListView<>(
                playableCards,
                renderer,
                "Carte personaggio acquistabili",
                "Seleziona carta",
                "Nessuna carta giocabile",
                false,
                4
        );
    }

    @Override
    public void draw() {
        listView.draw();
    }

    /**
     * Set the listener called when the user selects a character card.
     * @param listener the listener called when the user selects a character card.
     */
    public void setListener(Consumer<Character> listener) {
        listView.setOnSelectionListener((e, index) -> listener.accept(e.getKey()));
    }
}
