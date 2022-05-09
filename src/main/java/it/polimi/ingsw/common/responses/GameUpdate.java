package it.polimi.ingsw.common.responses;

import it.polimi.ingsw.common.reducedmodel.ReducedGame;

public class GameUpdate extends UpdateResponse {

    private final ReducedGame game;

    public GameUpdate(ReducedGame game) {
        super();
        this.game = game;
    }

    public ReducedGame getGame() {
        return game;
    }
}
