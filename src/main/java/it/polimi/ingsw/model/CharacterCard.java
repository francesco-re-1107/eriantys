package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.charactercards.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is used to represent a generic character card
 */
public abstract class CharacterCard {

    /**
     * This is the initial cost of the card, it will change when the card is used
     */
    private final int cost;

    /**
     * Random object used to generate random cards deck
     * TODO: store the seed for persistance
     */
    private final static Random random = new Random();

    /**
     * Store the number of times the card is used (used for calculating final cost)
     */
    private int used = 0;

    /**
     * Create a generic character card
     * @param cost initial cost of the card
     */
    public CharacterCard(int cost) {
        this.cost = cost;
    }

    /**
     * Increment the used counter, called whenever the card is used by some player
     */
    public void incrementUsedCounter(){
        this.used++;
    }

    /**
     * Calculate current cost based on how many times the card was used
     * @return the initial cost if card was not used, initial cost +1 otherwise
     */
    public int getCost() {
        return cost + (used == 0 ? 0 : 1);
    }
}
