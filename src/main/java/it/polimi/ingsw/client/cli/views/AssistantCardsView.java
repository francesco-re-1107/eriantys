package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.requests.PlayAssistantCardRequest;
import it.polimi.ingsw.server.model.AssistantCard;
import org.fusesource.jansi.Ansi;

import java.util.Comparator;
import java.util.Map;

/**
 * This view shows a list of assistant cards playable by the user
 */
public class AssistantCardsView extends BaseView {

    /**
     * List
     */
    private final ListView<AssistantCard> listView;

    /**
     * Title shown on the listview
     */
    private static final String LISTVIEW_TITLE = "Scegli la carta assistente";

    /**
     * Prompt of the listview
     */
    private static final String LISTVIEW_PROMPT = "Inserisci il numero corrispondente";

    /**
     * MEssage shown when there are no cards, this state should never be reached
     */
    private static final String EMPTY_LIST_MESSAGE = "Nessuna carta giocabile";

    /**
     * Message shown when the selected card is not playable
     */
    private static final String ERROR_CARD_NOT_PLAYABLE = "Carta non giocabile";

    /**
     * Create a new AssistantCardsView with the given deck
     * @param playerDeck the deck of the player
     */
    public AssistantCardsView(Map<AssistantCard, Boolean> playerDeck) {
        this.listView = new ListView<>(
                playerDeck.entrySet().stream()
                        .filter(e -> !e.getValue())
                        .sorted(Comparator.comparingInt(e -> e.getKey().turnPriority()))
                        .map(Map.Entry::getKey)
                        .toList(),
                card -> new Ansi()
                        .a("Priorità turno: ")
                        .a(card.turnPriority())
                        .a(" | ")
                        .a("Mosse madre natura: ")
                        .fg(Palette.RAINBOW.get(card.motherNatureMaxMoves() - 1))
                        .a(card.motherNatureMaxMoves())
                        .reset(),
                LISTVIEW_TITLE,
                LISTVIEW_PROMPT,
                EMPTY_LIST_MESSAGE,
                true,
                2
        );

        listView.setOnSelectionListener((card, input) ->
                Client.getInstance().forwardGameRequest(
                        new PlayAssistantCardRequest(card),
                        e -> listView.showError(ERROR_CARD_NOT_PLAYABLE)
                )
        );
    }

    @Override
    public void draw() {
        var inputView = new SimpleInputView("Premi invio per giocare la carta assistente");
        inputView.setListener(s -> listView.draw());
        inputView.draw();
    }
}
