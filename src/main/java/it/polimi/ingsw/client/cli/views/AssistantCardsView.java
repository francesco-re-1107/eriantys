package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.requests.PlayAssistantCardRequest;
import it.polimi.ingsw.server.model.AssistantCard;
import org.fusesource.jansi.Ansi;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
                        .sorted(Comparator.comparingInt(e -> e.getKey().turnPriority()))
                        .map(Map.Entry::getKey)
                        .toList(),

                card -> new Ansi()
                        .a("Priorità turno: ")
                        .a(card.turnPriority())
                        .a(" | ")
                        .a("Mosse madre natura:")
                        .fg(Palette.RAINBOW.get(card.motherNatureMaxMoves() - 1))
                        .a(card.motherNatureMaxMoves())
                        .reset(),
                "Scegli la carta assistente",
                "Inserisci il numero corrispondente",
                "Nessuna carta giocabile",
                true,
                2);

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
