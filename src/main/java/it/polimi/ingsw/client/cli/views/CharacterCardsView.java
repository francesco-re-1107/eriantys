package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.server.model.Character;
import org.fusesource.jansi.Ansi;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.fusesource.jansi.Ansi.ansi;

public class CharacterCardsView extends BaseView {

    private final ListView<Map.Entry<Character, Integer>> listView;

    public CharacterCardsView(List<Map.Entry<Character, Integer>> playableCards) {
        this.listView = new ListView<>(
                playableCards,
                getRenderer(),
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

    public void setListener(Consumer<Character> listener) {
        listView.setListener((e, index) -> listener.accept(e.getKey()));
    }

    private Function<Map.Entry<Character, Integer>, Ansi> getRenderer() {
        return entry -> {
            var title = Constants.CHARACTER_NAMES.get(entry.getKey());
            var description = Constants.CHARACTER_DESCRIPTIONS.get(entry.getKey());
            return ansi()
                    .bold()
                    .fgBrightMagenta()
                    .a(title)
                    .fg(Palette.WHITE)
                    .bold()
                    .a(" (" + entry.getKey().getCost(entry.getValue()) + "$)")
                    .reset()
                    .fg(Palette.WHITE)
                    .a("\n        ") //inset description
                    .a(description.replaceAll("(.{65})", "$1\n        ")) //put a new line every 65 chars
                    .reset();
        };
    }
}
