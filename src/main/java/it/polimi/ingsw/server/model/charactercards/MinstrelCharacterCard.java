package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.io.Serial;

/**
 * This class models the minstrel card
 * EFFECT: swap 1 or 2 students between entrance and school for the player that played the card
 */
public class MinstrelCharacterCard implements CharacterCard {

    @Serial
    private static final long serialVersionUID = -3606524692311186657L;

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
        if (studentsToRemove.getSize() != 2 || studentsToAdd.getSize() != 2)
            throw new InvalidOperationError("You must swap 2 students");

        game.getCurrentRound().getCurrentPlayer().swapStudents(studentsToRemove, studentsToAdd);
        game.updateProfessors();
    }
}
