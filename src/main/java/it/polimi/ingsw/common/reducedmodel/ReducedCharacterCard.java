package it.polimi.ingsw.common.reducedmodel;

import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;

import java.io.Serializable;

public abstract class ReducedCharacterCard implements Serializable {

    public abstract CharacterCard toCharacterCard(Game game);

}
