package it.polimi.ingsw.model.influencecalculators;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.Map;

public class ProfessorsControlInfluenceCalculator extends DefaultInfluenceCalculator{

    private Player player;

    public ProfessorsControlInfluenceCalculator(Player player) {
        this.player = player;
    }

    @Override
    protected int calculateStudentsInfluence(Player player, Island island, Map<Student, Player> professors) {
        //TODO implement
        return super.calculateStudentsInfluence(player, island, professors);
    }
}
