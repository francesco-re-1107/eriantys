package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.charactercards.MinstrelCharacterCard;

public class ReducedMinstrelCharacterCard extends ReducedCharacterCard {

    private final StudentsContainer studentsToAdd, studentsToRemove;

    public ReducedMinstrelCharacterCard(StudentsContainer studentsToAdd, StudentsContainer studentsToRemove) {
        this.studentsToAdd = studentsToAdd;
        this.studentsToRemove = studentsToRemove;
    }

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new MinstrelCharacterCard(studentsToRemove, studentsToAdd);
    }

}
