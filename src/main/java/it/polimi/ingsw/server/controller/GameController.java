package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.server.model.Game;

public class GameController implements Game.GameUpdateListener {

    private final Game game;

    private GameUpdateListener listener;

    public GameController(Game game) {
        this.game = game;
        game.addGameUpdateListener(this);
    }

    public void setOnGameUpdateListener(GameUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onGameUpdate(Game game) {
        listener.onGameUpdate(ReducedGame.fromGame(game));
    }

    public void disconnect() {
        //game.disconnectPlayer();
    }

    public void leaveGame() {
        //game.
    }

    public interface GameUpdateListener {
        void onGameUpdate(ReducedGame game);
    }
}
