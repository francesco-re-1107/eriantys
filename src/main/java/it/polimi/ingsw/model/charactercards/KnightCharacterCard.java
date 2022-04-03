package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.InfluenceCalculator;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.influencecalculators.AdditionalPointsInfluenceCalculator;

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
