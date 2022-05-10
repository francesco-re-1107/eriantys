package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;

/**
 * This class represents the request to play a character card in the currently played game
 */
public class PlayCharacterCardRequest extends GameRequest{

    private ReducedCharacterCard characterCard;

    public PlayCharacterCardRequest(ReducedCharacterCard characterCard) {
        this.characterCard = characterCard;
    }

    public ReducedCharacterCard getCharacterCard() {
        return characterCard;
    }
}
