package it.polimi.ingsw.model.influencecalculators;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.Map;

public class AdditionalPointsInfluenceCalculator extends DefaultInfluenceCalculator {

    private final Player player;

    private static final int ADDITIONAL_INFLUENCE = 2;

    public AdditionalPointsInfluenceCalculator(Player player) {
        this.player = player;
    }

    @Override
    public int calculateInfluence(Player player, Island island, Map<Student, Player> professors) {
        if(this.player.equals(player))
            return super.calculateInfluence(player, island, professors) + ADDITIONAL_INFLUENCE;
        else
            return super.calculateInfluence(player, island, professors);

    }
}
