package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.InfluenceCalculator;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.influencecalculators.AdditionalPointsInfluenceCalculator;

/**
 * This class models the knight card
 * EFFECT: provides 2 additional points in influence calculation for the player that played the card
 */
public class KnightCharacterCard extends InfluenceCharacterCard {

    private final Player player;

    public KnightCharacterCard(Player player) {
        super(2);
        this.player = player;
    }

    @Override
    public InfluenceCalculator getInfluenceCalculator() {
        return new AdditionalPointsInfluenceCalculator(this.player);
    }
}
