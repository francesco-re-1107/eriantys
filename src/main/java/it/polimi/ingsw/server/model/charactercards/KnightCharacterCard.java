package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.InfluenceCalculator;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.influencecalculators.AdditionalPointsInfluenceCalculator;

import java.io.Serial;

/**
 * This class models the knight card
 * EFFECT: provides 2 additional points in influence calculation for the player that played the card
 */
public class KnightCharacterCard extends InfluenceCharacterCard {

    @Serial
    private static final long serialVersionUID = 9019098394539981453L;

    @Override
    public InfluenceCalculator getInfluenceCalculator(Player cardPlayer) {
        return new AdditionalPointsInfluenceCalculator(cardPlayer);
    }

    @Override
    public Character getCharacter() {
        return Character.KNIGHT;
    }
}
