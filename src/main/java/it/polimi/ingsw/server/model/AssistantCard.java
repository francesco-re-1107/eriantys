package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * This card represents an assistant card used by players in the planning stage of the round
 */
public record AssistantCard(int turnPriority, int motherNatureMaxMoves) implements Serializable {

    /**
     * Generate a new default deck of assistant cards
     *
     * @return the generated deck
     */
    public static List<AssistantCard> getDefaultDeck() {
        return Arrays.asList(
                new AssistantCard(1, 1),
                new AssistantCard(2, 1),
                new AssistantCard(3, 2),
                new AssistantCard(4, 2),
                new AssistantCard(5, 3),
                new AssistantCard(6, 3),
                new AssistantCard(7, 4),
                new AssistantCard(8, 4),
                new AssistantCard(9, 5),
                new AssistantCard(10, 5)
        );
    }

    public String getName() {
        return turnPriority() + "_" +  motherNatureMaxMoves();
    }
}