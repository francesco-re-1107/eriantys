package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.StudentsContainer;

public class MinstrelCharacterCard extends CharacterCard {

    //with respect to the school
    private final StudentsContainer studentsToRemove;

    private final StudentsContainer studentsToAdd;

    public MinstrelCharacterCard(StudentsContainer studentsToRemove, StudentsContainer studentsToAdd) {
        super(1);
        this.studentsToRemove = studentsToRemove;
        this.studentsToAdd = studentsToAdd;
    }

    public StudentsContainer getStudentsToRemove() {
        return studentsToRemove;
    }

    public StudentsContainer getStudentsToAdd() {
        return studentsToAdd;
    }
}
