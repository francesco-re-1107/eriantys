package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.InfluenceCalculator;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.influencecalculators.NoColorInfluenceCalculator;

import java.io.Serial;

/**
 * This class models the mushroom man card
 * EFFECT: ignore the selected student color during influence calculation
 */
public class MushroomManCharacterCard extends InfluenceCharacterCard {

    @Serial
    private static final long serialVersionUID = -68389996830129993L;

    /**
     * Student color selected by the user that will be ignored during influence calculation
     */
    private final Student studentColorNotInfluencing;

    public MushroomManCharacterCard(Student studentColorNotInfluencing) {
        super();
        this.studentColorNotInfluencing = studentColorNotInfluencing;
    }

    @Override
    public InfluenceCalculator getInfluenceCalculator(Player cardPlayer) {
        return new NoColorInfluenceCalculator(studentColorNotInfluencing);
    }

    @Override
    public Character getCharacter() {
        return Character.MUSHROOM_MAN;
    }
}
