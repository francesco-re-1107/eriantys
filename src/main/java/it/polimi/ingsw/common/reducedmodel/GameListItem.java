package it.polimi.ingsw.common.reducedmodel;

import it.polimi.ingsw.server.model.Game;

import java.io.Serializable;
import java.util.UUID;

public record GameListItem (
        UUID uuid,
        int numberOfPlayers,
        int currentNumberOfPlayers,
        boolean expertMode
) implements Serializable {

    public static GameListItem fromGame(Game g) {
        return new GameListItem(
                g.getUUID(),
                g.getNumberOfPlayers(),
                g.getCurrentNumberOfPlayers(),
                g.isExpertMode()
        );
    }
}
