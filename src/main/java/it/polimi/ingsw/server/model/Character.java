package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;

import java.util.*;

/**
 * This enum holds all the available character cards that can be randomly extracted
 */
public enum Character {
    CENTAUR(3),
    FARMER(2),
    GRANDMA(2),
    HERALD(3),
    KNIGHT(2),
    MINSTREL(1),
    MUSHROOM_MAN(3),
    POSTMAN(1);

    /**
     * This is the initial cost of the card, it will change when the card is used
     */
    private final int cost;

    Character(int cost) {
        this.cost = cost;
    }

    /**
     * Calculate current cost based on how many times the card was used
     * @param usedTimes number of times the card was used
     * @return the initial cost if card was not used, initial cost +1 otherwise
     */
    public int getCost(int usedTimes) {
        return usedTimes <= 0 ? cost : cost + 1;
    }

    /**
     * Get initial cost of the card
     * @return the initial cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Generate a random deck of characters without duplicates
     * @param howMany number of characters to pick randomly
     * @return the generated deck
     */
    public static List<Character> generateRandomDeck(int howMany) {
        var list = new ArrayList<>(Arrays.asList(Character.values()));

        if(howMany > list.size())
            throw new InvalidOperationError("Too many cards");

        Collections.shuffle(list);

        return new LinkedList<>(list.subList(0, howMany));
    }
}
