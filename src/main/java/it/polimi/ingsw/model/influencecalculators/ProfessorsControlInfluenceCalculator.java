package it.polimi.ingsw.model.influencecalculators;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.Arrays;
import java.util.Map;

/**
 * This calculator lets the selected player take professor for a student color if the number
 * of students is >= of other players (by default it is only >)
 */
public class ProfessorsControlInfluenceCalculator extends DefaultInfluenceCalculator {

    private final Player player;

    public ProfessorsControlInfluenceCalculator(Player player) {
        this.player = player;
    }

    @Override
    protected int calculateStudentsInfluence(Player player, Island island, Map<Student, Player> professors) {
        if (!this.player.equals(player)) //only for the player that played the card
            return super.calculateStudentsInfluence(player, island, professors);

        int influence = 0;

        /* see later TODO: test and then delete this
        //professors owned by this player
        influence += professors.entrySet()
                .stream()
                .filter(e -> player.equals(e.getValue()))
                .mapToInt(e -> island.getStudents().getCountForStudent(e.getKey()))
                .sum();*/

        //professors with null players (means that everyone has 0 students of that type TODO: maybe improve)
        influence += professors.entrySet()
                .stream()
                .filter(e -> e.getValue() == null)
                .mapToInt(e -> island.getStudents().getCountForStudent(e.getKey()))
                .sum();

        //professors owned by this player OR by other players but with same number of students
        influence += professors.entrySet()
                .stream()
                .filter(e ->
                        e.getValue() != null &&
                                player.getSchool().getCountForStudent(e.getKey()) >=
                                        e.getValue().getSchool().getCountForStudent(e.getKey())
                )
                .mapToInt(e -> island.getStudents().getCountForStudent(e.getKey()))
                .sum();

        return influence;
    }
}
