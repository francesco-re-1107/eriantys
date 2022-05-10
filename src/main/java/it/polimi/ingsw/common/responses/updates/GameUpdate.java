package it.polimi.ingsw.common.responses.updates;

import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.responses.Update;

/**
 * This class represents an update of the game. It is sent by the server to the clients.
 */
public class GameUpdate extends Update {

    /**
     * The updated game.
     */
    private final ReducedGame game;

    /**
     * Create a GameUpdate with the given game.
     * @param game
     */
    public GameUpdate(ReducedGame game) {
        super();
        this.game = game;
    }

    /**
     * Get the updated game.
     */
    public ReducedGame getGame() {
        return game;
    }
}
