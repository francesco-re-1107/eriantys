package it.polimi.ingsw.server.model.influencecalculators;

import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Student;

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
