package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.InfluenceCalculator;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.influencecalculators.ProfessorsControlInfluenceCalculator;

/**
 * This class models the farmer card
 * EFFECT: take control of the professor even if players have the same number of students for that color
 */
public class FarmerCharacterCard extends InfluenceCharacterCard {

    public FarmerCharacterCard() {
        super(2);
    }

    @Override
    public InfluenceCalculator getInfluenceCalculator(Player cardPlayer) {
        return new ProfessorsControlInfluenceCalculator(cardPlayer);
    }
}
