package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.charactercards.MushroomManCharacterCard;

import java.io.Serial;

public class ReducedMushroomManCharacterCard extends ReducedCharacterCard {

    @Serial
    private static final long serialVersionUID = 4139512378041996038L;

    private final Student studentColorNotInfluencing;

    public ReducedMushroomManCharacterCard(Student studentColorNotInfluencing) {
        this.studentColorNotInfluencing = studentColorNotInfluencing;
    }

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new MushroomManCharacterCard(studentColorNotInfluencing);
    }
}
