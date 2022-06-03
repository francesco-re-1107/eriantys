package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.charactercards.PostmanCharacterCard;

import java.io.Serial;

public class ReducedPostmanCharacterCard extends ReducedCharacterCard {

    @Serial
    private static final long serialVersionUID = -2984622107585363451L;

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new PostmanCharacterCard();
    }

}
