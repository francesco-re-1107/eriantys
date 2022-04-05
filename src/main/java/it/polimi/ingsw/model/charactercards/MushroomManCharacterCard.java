package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.InfluenceCalculator;
import it.polimi.ingsw.model.influencecalculators.NoColorInfluenceCalculator;
import it.polimi.ingsw.model.Student;

/**
 * This class models the mushroom man card
 * EFFECT: ignore the selected student color during influence calculation
 */
public class MushroomManCharacterCard extends InfluenceCharacterCard {

    /**
     * Student color selected by the user that will be ignored during influence calculation
     */
    private final Student studentColorNotInfluencing;

    public MushroomManCharacterCard(Student studentColorNotInfluencing) {
        super(3);
        this.studentColorNotInfluencing = studentColorNotInfluencing;
    }

    @Override
    public InfluenceCalculator getInfluenceCalculator() {
        return new NoColorInfluenceCalculator(studentColorNotInfluencing);
    }
}
