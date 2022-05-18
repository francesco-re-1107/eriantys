package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;

import java.io.Serializable;
import java.util.*;

/**
 * This class is used to represent a generic character card
 */
public abstract class CharacterCard implements Serializable {

    /**
     * Generate a random deck of character cards without duplicates
     * @param howManyCards number of cards to pick randomly
     * @return the generated deck
     */
    public static List<Character> generateRandomDeck(int howManyCards) {
        var list = new ArrayList<>(Arrays.asList(Character.values()));

        if(howManyCards > list.size())
            throw new InvalidOperationException("Too many cards");

        Collections.shuffle(list);

        return new LinkedList<>(list.subList(0, howManyCards));
    }

    /**
     * @return the name (simple class name) of the card
     */
    public abstract Character getCharacter();

    /**
     * Apply the card's effect to the given game
     * @param game
     */
    public abstract void play(Game game);
}
