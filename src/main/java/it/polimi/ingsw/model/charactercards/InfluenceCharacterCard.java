package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.InfluenceCalculator;

public abstract class InfluenceCharacterCard extends CharacterCard {

    /**
     * Create a generic character card
     *
     * @param cost initial cost of the card
     */
    public InfluenceCharacterCard(int cost) {
        super(cost);
    }

    public abstract InfluenceCalculator getInfluenceCalculator();

}
