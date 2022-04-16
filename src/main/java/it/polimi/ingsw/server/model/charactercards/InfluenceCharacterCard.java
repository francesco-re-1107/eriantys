package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.InfluenceCalculator;
import it.polimi.ingsw.server.model.Player;

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
     * @param cardPlayer the player who played the card
     * @return the specific influence calculator for every card depending on the card effect
     */
    public abstract InfluenceCalculator getInfluenceCalculator(Player cardPlayer);

}
