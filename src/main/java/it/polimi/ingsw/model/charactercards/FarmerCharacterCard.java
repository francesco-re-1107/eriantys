package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.InfluenceCalculator;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.influencecalculators.ProfessorsControlInfluenceCalculator;

/**
 * This class models the farmer card
 * EFFECT: take control of the professor even if players have the same number of students for that color
 */
public class FarmerCharacterCard extends InfluenceCharacterCard {

    /**
     * Holds a reference to the player who played this card, in order to pass it to the calculator
     */
    private final Player player;

    public FarmerCharacterCard(Player player) {
        super(2);
        this.player = player;
    }

    @Override
    public InfluenceCalculator getInfluenceCalculator() {
        return new ProfessorsControlInfluenceCalculator(player);
    }
}
