package it.polimi.ingsw.common.requests;

import java.util.UUID;

/**
 * This class represents the request to join the game with the specified uuid
 */
public class JoinGameRequest extends Request{

    private final UUID gameId;

    public JoinGameRequest(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getUUID() {
        return gameId;
    }
}
