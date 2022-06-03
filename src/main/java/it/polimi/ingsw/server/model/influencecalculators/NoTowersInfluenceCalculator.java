package it.polimi.ingsw.server.model.influencecalculators;

import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Student;

import java.io.Serial;
import java.util.Map;

/**
 * This calculator ignores the towers in the influence calculator process
 */
public class NoTowersInfluenceCalculator extends DefaultInfluenceCalculator {

    @Serial
    private static final long serialVersionUID = -3193043778709996517L;

    @Override
    protected int calculateTowersInfluence(Player player, Island island, Map<Student, Player> professors) {
        return 0;
    }

}
