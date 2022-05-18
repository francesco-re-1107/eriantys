package it.polimi.ingsw.server.model;

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
     * @return
     */
    public int getCost() {
        return cost;
    }
}
