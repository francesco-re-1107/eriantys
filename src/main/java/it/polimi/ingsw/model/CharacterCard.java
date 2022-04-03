package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.charactercards.*;

import java.util.*;

/**
 * This class is used to represent a generic character card
 */
public abstract class CharacterCard {

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
     * Generate a random deck of character cards without duplicates (TODO: fix duplicates)
     * @param howManyCards
     * @return the generated deck
     */
    public static List<String> generateRandomDeck(int howManyCards) {
        if(howManyCards > availableCards.size())
            throw new InvalidOperationException("Too many cards");

        Collections.shuffle(availableCards);

        return availableCards.subList(0, howManyCards);
    }

    /**
     * Calculate current cost based on how many times the card was used
     * @return the initial cost if card was not used, initial cost +1 otherwise
     */
    public int getCost() {
        return cost;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }
}
