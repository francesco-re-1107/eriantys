package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.InfluenceCalculator;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.influencecalculators.NoTowersInfluenceCalculator;

import java.io.Serial;

/**
 * This class models the centaur card
 * EFFECT: Towers are not considered in the influence calculation process
 */
public class CentaurCharacterCard extends InfluenceCharacterCard {

    @Serial
    private static final long serialVersionUID = 7018923759566651943L;

    @Override
    public InfluenceCalculator getInfluenceCalculator(Player cardPlayer) {
        return new NoTowersInfluenceCalculator();
    }

    @Override
    public Character getCharacter() {
        return Character.CENTAUR;
    }
}
