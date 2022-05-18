package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;

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


    @Override
    public void play(Game game) {
        game.getCurrentRound().setAdditionalMotherNatureMoves(ADDITIONAL_MOTHER_NATURE_MOVES);
    }
}
