package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.charactercards.KnightCharacterCard;

import java.io.Serial;

public class ReducedKnightCharacterCard extends ReducedCharacterCard {

    @Serial
    private static final long serialVersionUID = 554359924028727835L;

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new KnightCharacterCard();
    }

}
