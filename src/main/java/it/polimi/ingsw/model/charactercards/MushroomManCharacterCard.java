package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.InfluenceCalculator;
import it.polimi.ingsw.model.influencecalculators.NoColorInfluenceCalculator;
import it.polimi.ingsw.model.Student;

public class MushroomManCharacterCard extends InfluenceCharacterCard {

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
