package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;

/**
 * This class models the postman card
 *
 * EFFECT: provides additional mother nature moves to the player that played the card
 */
public class PostmanCharacterCard extends CharacterCard {

    private static final int ADDITIONAL_MOTHER_NATURE_MOVES = 2;

    public PostmanCharacterCard() {
        super(1);
    }

    /**
     * @return the number of additional moves that mother nature can do
     */
    public int getAdditionalMotherNatureMoves(){
        return ADDITIONAL_MOTHER_NATURE_MOVES;
    }
}
