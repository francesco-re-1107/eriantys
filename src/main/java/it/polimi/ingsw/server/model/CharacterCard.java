package it.polimi.ingsw.server.model;

import java.io.Serializable;

/**
 * This class is used to represent a generic character card
 */
public interface CharacterCard extends Serializable {

    /**
     * @return the name (simple class name) of the card
     */
    Character getCharacter();

    /**
     * Apply the card's effect to the given game
     * @param game the game to apply the effect to
     */
    void play(Game game);
}
