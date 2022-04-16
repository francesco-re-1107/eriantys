package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.InfluenceCalculator;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.influencecalculators.NoTowersInfluenceCalculator;

/**
 * This class models the centaur card
 * EFFECT: Towers are not considered in the influence calculation process
 */
public class CentaurCharacterCard extends InfluenceCharacterCard {

    public CentaurCharacterCard() {
        super(3);
    }

    @Override
    public InfluenceCalculator getInfluenceCalculator(Player cardPlayer) {
        return new NoTowersInfluenceCalculator();
    }
}
