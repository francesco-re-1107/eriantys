package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.InfluenceCalculator;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.influencecalculators.ProfessorsControlInfluenceCalculator;

import java.io.Serial;

/**
 * This class models the farmer card
 * EFFECT: take control of the professor even if players have the same number of students for that color
 */
public class FarmerCharacterCard extends InfluenceCharacterCard {

    @Serial
    private static final long serialVersionUID = 5920195844193266919L;

    @Override
    public InfluenceCalculator getInfluenceCalculator(Player cardPlayer) {
        return new ProfessorsControlInfluenceCalculator(cardPlayer);
    }

    @Override
    public Character getCharacter() {
        return Character.FARMER;
    }
}
