package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.charactercards.MinstrelCharacterCard;

import java.io.Serial;

public class ReducedMinstrelCharacterCard extends ReducedCharacterCard {

    @Serial
    private static final long serialVersionUID = -2301467312197707131L;

    private final StudentsContainer studentsToAdd;
    private final StudentsContainer studentsToRemove;

    public ReducedMinstrelCharacterCard(StudentsContainer studentsToAdd, StudentsContainer studentsToRemove) {
        this.studentsToAdd = studentsToAdd;
        this.studentsToRemove = studentsToRemove;
    }

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new MinstrelCharacterCard(studentsToRemove, studentsToAdd);
    }

}
