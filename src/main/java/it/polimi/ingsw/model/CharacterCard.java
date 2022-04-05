package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.charactercards.*;

import java.util.*;

/**
 * This class is used to represent a generic character card
 */
public abstract class CharacterCard {

    /**
     * This list holds all the available character cards that can be randomly extracted
     */
    private static final List<String> availableCards = Arrays.asList(
            "CentaurCharacterCard",
            "FarmerCharacterCard",
            "GrandmaCharacterCard",
            "HeraldCharacterCard",
            "KnightCharacterCard",
            "MinstrelCharacterCard",
            "MushroomManCharacterCard",
            "PostmanCharacterCard"
    );

    /**
     * This is the initial cost of the card, it will change when the card is used
     */
    private final int cost;

    /**
     * Create a generic character card
     * @param cost initial cost of the card
     */
    public CharacterCard(int cost) {
        this.cost = cost;
    }

    /**
     * Generate a random deck of character cards without duplicates
     * @param howManyCards number of cards to pick randomly
     * @return the generated deck
     */
    public static List<String> generateRandomDeck(int howManyCards) {
        if(howManyCards > availableCards.size())
            throw new InvalidOperationException("Too many cards");

        Collections.shuffle(availableCards);

        return new LinkedList<>(availableCards.subList(0, howManyCards));
    }

    /**
     * @return the initial cost of the card
     */
    public int getCost() {
        return cost;
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
     * @return the name (simple class name) of the card
     */
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
