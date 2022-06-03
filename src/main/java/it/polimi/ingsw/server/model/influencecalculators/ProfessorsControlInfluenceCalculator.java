package it.polimi.ingsw.server.model.influencecalculators;

import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Student;

import java.io.Serial;
import java.util.EnumMap;
import java.util.Map;

/**
 * This calculator lets the selected player take professor for a student color if the number
 * of students is >= of other players (by default it is only >)
 */
public class ProfessorsControlInfluenceCalculator extends DefaultInfluenceCalculator {

    @Serial
    private static final long serialVersionUID = 7748194063358719583L;

    private final Player privilegedPlayer;

    public ProfessorsControlInfluenceCalculator(Player privilegedPlayer) {
        this.privilegedPlayer = privilegedPlayer;
    }

    @Override
    protected int calculateStudentsInfluence(Player player, Island island, Map<Student, Player> professors) {
        Map<Student, Player> newProfessors = new EnumMap<>(professors);

        for (Student s : Student.values()) {
            //if no professor then all the players have 0 students of this color
            if(!professors.containsKey(s)) {
                newProfessors.put(s, privilegedPlayer);
            }else{ //professors map contains this key
                Player otherPlayer = professors.get(s);
                if(!privilegedPlayer.equals(otherPlayer)){ //professor held by someone else

                    //check if the privileged player has the same number of students
                    if(privilegedPlayer.getSchool().getCountForStudent(s)
                            >= otherPlayer.getSchool().getCountForStudent(s))
                        newProfessors.put(s, privilegedPlayer);
                }
            }
        }

        //let the superclass calculate influence with the changed professors
        return super.calculateStudentsInfluence(player, island, newProfessors);
    }
}
