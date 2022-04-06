package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This card represents an assistant card used by players in the planning stage of the round
 */
public class AssistantCard {

    /**
     * Stores the maximum number of moves that mother nature can do when this card is played
     */
    private final int motherNatureMaxMoves;

    /**
     * Stores the priority of card in the round
     */
    private final int turnPriority;

    /**
     * Create an assistant card
     * @param motherNatureMaxMoves number of moves that mother nature can do
     * @param turnPriority priority number of this card
     */
    private AssistantCard(int turnPriority, int motherNatureMaxMoves) {
        this.motherNatureMaxMoves = motherNatureMaxMoves;
        this.turnPriority = turnPriority;
    }

    /**
     * @return number of moves that mother nature can do when this card is played
     */
    public int getMotherNatureMaxMoves() {
        return motherNatureMaxMoves;
    }

    /**
     * @return the priority of card in the round
     */
    public int getTurnPriority() {
        return turnPriority;
    }

    /**
     * Generate a new default deck of assistant cards
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssistantCard that = (AssistantCard) o;
        return motherNatureMaxMoves == that.motherNatureMaxMoves && turnPriority == that.turnPriority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(motherNatureMaxMoves, turnPriority);
    }
}