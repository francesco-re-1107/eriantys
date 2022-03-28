package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.InfluenceCalculator;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.influencecalculators.ProfessorsControlInfluenceCalculator;

public class FarmerCharacterCard extends InfluenceCharacterCard {

    private Player player;

    public FarmerCharacterCard(Player player) {
        super(2);
        this.player = player;
    }

    @Override
    public InfluenceCalculator getInfluenceCalculator() {
        return new ProfessorsControlInfluenceCalculator(player);
    }
}
