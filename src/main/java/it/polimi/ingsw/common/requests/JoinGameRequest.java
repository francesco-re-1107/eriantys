package it.polimi.ingsw.common.requests;

import java.util.UUID;

public class JoinGameRequest extends Request{

    private final UUID uuid;

    public JoinGameRequest(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }
}
