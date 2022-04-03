package it.polimi.ingsw.model.influencecalculators;

import it.polimi.ingsw.model.InfluenceCalculator;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultInfluenceCalculator implements InfluenceCalculator {

    @Override
    public int calculateInfluence(Player player, Island island, Map<Student, Player> professors) {
        return calculateTowersInfluence(player, island, professors) +
                calculateStudentsInfluence(player, island, professors);
    }

    protected int calculateTowersInfluence(Player player, Island island, Map<Student, Player> professors){
        //atomic because it is used in lambda
        AtomicInteger influence = new AtomicInteger();

        if(island.isConquered())
            if(island.getTowerColor() == player.getTowerColor())
                influence.addAndGet(island.getTowersCount());

        return influence.get();
    }

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
