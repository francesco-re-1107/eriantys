package it.polimi.ingsw.model.influencecalculators;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.influencecalculators.DefaultInfluenceCalculator;

import java.util.Map;

/**
 * This calculator ignores the towers in the influence calculator process
 */
public class NoTowersInfluenceCalculator extends DefaultInfluenceCalculator {

    @Override
    protected int calculateTowersInfluence(Player player, Island island, Map<Student, Player> professors) {
        return 0;
    }

}
