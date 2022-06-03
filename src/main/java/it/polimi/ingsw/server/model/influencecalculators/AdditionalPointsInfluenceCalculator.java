package it.polimi.ingsw.server.model.influencecalculators;

import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Student;

import java.io.Serial;
import java.util.Map;

/**
 * This calculator provides additional points to the given player in the influence calculation
 */
public class AdditionalPointsInfluenceCalculator extends DefaultInfluenceCalculator {

    @Serial
    private static final long serialVersionUID = 8207587773206558696L;

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
