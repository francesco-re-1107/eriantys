package it.polimi.ingsw.common.responses.updates;

import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.responses.Update;

public class GameUpdate extends Update {

    private final ReducedGame game;

    public GameUpdate(ReducedGame game) {
        super();
        this.game = game;
    }

    public ReducedGame getGame() {
        return game;
    }
}
