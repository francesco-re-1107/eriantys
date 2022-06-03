package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.charactercards.CentaurCharacterCard;

import java.io.Serial;

public class ReducedCentaurCharacterCard extends ReducedCharacterCard {

    @Serial
    private static final long serialVersionUID = -1883899868974952176L;

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new CentaurCharacterCard();
    }
}
