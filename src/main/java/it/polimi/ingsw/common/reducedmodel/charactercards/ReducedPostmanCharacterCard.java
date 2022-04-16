package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.charactercards.PostmanCharacterCard;

public class ReducedPostmanCharacterCard extends ReducedCharacterCard {

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new PostmanCharacterCard();
    }

}
