package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;

public class AssistantCard {

    private final int id;

    private final int motherNatureMaxMoves;

    private final int turnPriority;

    public AssistantCard(int id, int motherNatureMaxMoves, int turnPriority) {
        this.id = id;
        this.motherNatureMaxMoves = motherNatureMaxMoves;
        this.turnPriority = turnPriority;
    }

    public int getId() {
        return id;
    }

    public int getMotherNatureMaxMoves() {
        return motherNatureMaxMoves;
    }

    public int getTurnPriority() {
        return turnPriority;
    }

    public static List<AssistantCard> getDefaultDeck() {
        return Arrays.asList(
            new AssistantCard(0, 1, 2),
            new AssistantCard(1, 1, 2),
            new AssistantCard(2, 1, 2),
            new AssistantCard(3, 1, 2),
            new AssistantCard(4, 1, 2),
            new AssistantCard(5, 1, 2),
            new AssistantCard(6, 1, 2),
            new AssistantCard(7, 1, 2),
            new AssistantCard(8, 1, 2),
            new AssistantCard(9, 1, 2)
        );
    }
}
