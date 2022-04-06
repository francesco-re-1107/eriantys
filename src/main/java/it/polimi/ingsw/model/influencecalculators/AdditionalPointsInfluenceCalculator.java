package it.polimi.ingsw.model.influencecalculators;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.Map;

/**
 * This calculator provides additional points to the given player in the influence calculation
 */
public class AdditionalPointsInfluenceCalculator extends DefaultInfluenceCalculator {

    private final Player privilegedPlayer;

    private static final int ADDITIONAL_INFLUENCE = 2;

    public AdditionalPointsInfluenceCalculator(Player privilegedPlayer) {
        this.privilegedPlayer = privilegedPlayer;
    }

    @Override
    public int calculateInfluence(Player player, Island island, Map<Student, Player> professors) {
        if(this.privilegedPlayer.equals(player))
            return super.calculateInfluence(player, island, professors) + ADDITIONAL_INFLUENCE;
        else
            return super.calculateInfluence(player, island, professors);

    }
}
