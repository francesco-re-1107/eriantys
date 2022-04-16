package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;

public class PlayCharacterCardRequest extends GameRequest{

    private ReducedCharacterCard characterCard;

    public PlayCharacterCardRequest(ReducedCharacterCard characterCard) {
        this.characterCard = characterCard;
    }

    public ReducedCharacterCard getCharacterCard() {
        return characterCard;
    }
}
