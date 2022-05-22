package it.polimi.ingsw.client.cli.views;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.fusesource.jansi.Ansi;

import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.server.model.AssistantCard;

public class AssistantCardsView extends CardsView {

    private Map<AssistantCard, Boolean> playerDeck;
    private Consumer<AssistantCard> listener;
    private SimpleInputView inputView;

    @Override
    public void draw() {
        cursor.clearScreen();
        cursor.printCentered("Scegli carta assistente da giocare", 1);

        this.inputView.setListener(input -> {
            if (listener == null)
                return;

            try {
                int index = Integer.parseInt(input) - 1;

                if (!(index >= 0 && index <= playerDeck.size()))
                    inputView.showError("Selezione non valida");

                listener.accept((AssistantCard) playerDeck.keySet().toArray()[index]);
            } catch (NumberFormatException e) {
                inputView.showError("Selezione non valida");
            }
        });

        for (var pair : playerDeck.entrySet()) {
            cursor.print(
                    new Ansi()
                            .bg(pair.getValue() ? Palette.TOWER_CONTRAST_BACKGROUND : Palette.TOWER_BLACK)
                            .a(String.format("< %d, %d >", pair.getKey().turnPriority(),
                                    pair.getKey().motherNatureMaxMoves()))
                            .reset());
            cursor.moveRelative(0, 1);
        }

        inputView.draw();

    }
}
