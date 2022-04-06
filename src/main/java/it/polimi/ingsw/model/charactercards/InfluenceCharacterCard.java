package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.InfluenceCalculator;

/**
 * This class is the supertype for all the character cards that affect influence calculation
 */
public abstract class InfluenceCharacterCard extends CharacterCard {

    /**
     * Create a generic character card
     *
     * @param cost initial cost of the card
     */
    protected InfluenceCharacterCard(int cost) {
        super(cost);
    }

    /**
     * @return the specific influence calculator for every card depending on the card effect
     */
    public abstract InfluenceCalculator getInfluenceCalculator();

}
