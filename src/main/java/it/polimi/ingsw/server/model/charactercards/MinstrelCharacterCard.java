package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.StudentsContainer;

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
        super();
        this.studentsToRemove = studentsToRemove;
        this.studentsToAdd = studentsToAdd;
    }

    @Override
    public Character getCharacter() {
        return Character.MINSTREL;
    }

    @Override
    public void play(Game game) {
        if (studentsToRemove.getSize() > 2 || studentsToAdd.getSize() > 2)
            throw new InvalidOperationException("Too much students to swap");

        game.getCurrentRound().getCurrentPlayer().swapStudents(studentsToRemove, studentsToAdd);
    }
}
