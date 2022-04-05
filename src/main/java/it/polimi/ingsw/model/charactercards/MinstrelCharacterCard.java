package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.StudentsContainer;

/**
 * This class models the minstrel card
 * EFFECT: swap 1 or 2 students between entrance and school for the player that played the card
 */
public class MinstrelCharacterCard extends CharacterCard {

    /**
     * Students to remove from school and add to entrance
     */
    private final StudentsContainer studentsToRemove;

    /**
     * Students to add to school and remove from entrance
     */
    private final StudentsContainer studentsToAdd;

    public MinstrelCharacterCard(StudentsContainer studentsToRemove, StudentsContainer studentsToAdd) {
        super(1);
        this.studentsToRemove = studentsToRemove;
        this.studentsToAdd = studentsToAdd;
    }

    /**
     * @return the students to remove from school and add to entrance
     */
    public StudentsContainer getStudentsToRemove() {
        return studentsToRemove;
    }

    /**
     * @return the students to add to school and remove from entrance
     */
    public StudentsContainer getStudentsToAdd() {
        return studentsToAdd;
    }
}
