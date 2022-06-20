package it.polimi.ingsw.common.reducedmodel;

import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;

import java.io.Serial;
import java.io.Serializable;

public abstract class ReducedCharacterCard implements Serializable {

    @Serial
    private static final long serialVersionUID = -9082736014851181102L;

    public abstract CharacterCard toCharacterCard(Game game);

}
