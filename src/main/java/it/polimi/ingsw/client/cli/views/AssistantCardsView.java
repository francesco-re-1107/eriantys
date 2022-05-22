package it.polimi.ingsw.client.cli.views;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.fusesource.jansi.Ansi;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.requests.GameRequest;
import it.polimi.ingsw.common.requests.PlayAssistantCardRequest;
import it.polimi.ingsw.server.model.AssistantCard;

public class AssistantCardsView extends CardsView {
    private Map<AssistantCard, Boolean> playerDeck;
    private List<Map<AssistantCard, Boolean>> otherDecks;
    private ListView<AssistantCard> listView;

    public AssistantCardsView(Map<AssistantCard, Boolean> playerDeck, List<Map<AssistantCard, Boolean>> otherDecks) {
        this.playerDeck = playerDeck;
        this.otherDecks = otherDecks;
        this.listView = new ListView<>(
                playerDeck.entrySet().stream()
                        .filter(e -> !e.getValue())
                        .sorted()
                        .map(e -> e.getKey())
                        .toList(),
                (card) -> new Ansi()
                        .a(String.format("< %d, %d >", card.turnPriority(),
                                card.motherNatureMaxMoves()))
                        .reset(),
                "Scegli la carta assistente",
                "Inserisci il numero corrispondente",
                "");

    }

    @Override
    public void draw() {
        listView.setListener(
                (card, input) -> {
                    Client.getInstance()
                            .forwardGameRequest(new PlayAssistantCardRequest(card),
                            (e) ->listView.showError("Carta non giocabile"));
                });
        listView.draw();
    }
}
