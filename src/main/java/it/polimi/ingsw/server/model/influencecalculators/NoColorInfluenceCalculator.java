package it.polimi.ingsw.server.model.influencecalculators;

import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Student;

import java.io.Serial;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This calculator ignores the selected student color in the influence calculation process
 */
public class NoColorInfluenceCalculator extends DefaultInfluenceCalculator {

    @Serial
    private static final long serialVersionUID = 3584931472310979093L;

    private final Student studentColorNotInfluencing;

    public NoColorInfluenceCalculator(Student studentColorNotInfluencing) {
        this.studentColorNotInfluencing = studentColorNotInfluencing;
    }

    @Override
    protected int calculateStudentsInfluence(Player player, Island island, Map<Student, Player> professors) {
        //atomic because it is used in lambda
        AtomicInteger influence = new AtomicInteger();

        professors.forEach((s, p) -> {
            if (p.equals(player) && s != studentColorNotInfluencing) //if the player has the professor associated
                influence.addAndGet(island.getStudents().getCountForStudent(s));
        });

        return influence.get();
    }

}
