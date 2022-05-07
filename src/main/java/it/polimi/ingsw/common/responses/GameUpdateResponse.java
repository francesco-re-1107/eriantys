package it.polimi.ingsw.common.responses;

import it.polimi.ingsw.common.reducedmodel.ReducedGame;

public class GameUpdateResponse extends UpdateResponse {

    private final ReducedGame game;

    public GameUpdateResponse(ReducedGame game) {
        super();
        this.game = game;
    }

    public ReducedGame getGame() {
        return game;
    }
}
