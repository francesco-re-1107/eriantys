package it.polimi.ingsw.model.influencecalculators;

import it.polimi.ingsw.model.InfluenceCalculator;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class implements the default algorithm for influence calculation
 */
public class DefaultInfluenceCalculator implements InfluenceCalculator {

    /**
     * Calculate default influence which is the sum of towers influence and students influence
     * @param player for which calculate influence
     * @param island on which calculate influence
     * @param professors the current professors
     * @return the calculated influence
     */
    @Override
    public int calculateInfluence(Player player, Island island, Map<Student, Player> professors) {
        return calculateTowersInfluence(player, island, professors) +
                calculateStudentsInfluence(player, island, professors);
    }

    /**
     * Calculate influence of towers on this island for a specific player
     * @param player for which calculate influence
     * @param island on which calculate influence
     * @param professors the current professors
     * @return the calculated influence for towers
     */
    protected int calculateTowersInfluence(Player player, Island island, Map<Student, Player> professors){
        //atomic because it is used in lambda
        AtomicInteger influence = new AtomicInteger();

        if(island.isConquered() && island.getTowerColor() == player.getTowerColor())
            influence.addAndGet(island.getTowersCount());

        return influence.get();
    }

    /**
     * Calculate influence of students on this island for a specific player
     * @param player for which calculate influence
     * @param island on which calculate influence
     * @param professors the current professors
     * @return the calculated influence for students
     */
    protected int calculateStudentsInfluence(Player player, Island island, Map<Student, Player> professors){
        //atomic because it is used in lambda
        AtomicInteger influence = new AtomicInteger();

        professors.forEach((s, p) -> {
            if (p.equals(player)) //if the player has the professor associated
                influence.addAndGet(island.getStudents().getCountForStudent(s));
        });

        return influence.get();
    }
}
