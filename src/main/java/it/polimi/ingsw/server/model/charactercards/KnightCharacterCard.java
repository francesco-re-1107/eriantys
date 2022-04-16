package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.InfluenceCalculator;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.influencecalculators.AdditionalPointsInfluenceCalculator;

/**
 * This class models the knight card
 * EFFECT: provides 2 additional points in influence calculation for the player that played the card
 */
public class KnightCharacterCard extends InfluenceCharacterCard {

    public KnightCharacterCard() {
        super(2);
    }

    @Override
    public InfluenceCalculator getInfluenceCalculator(Player cardPlayer) {
        return new AdditionalPointsInfluenceCalculator(cardPlayer);
    }
}
