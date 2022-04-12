package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.util.Map;
import java.util.stream.Collectors;

public class GameController implements Game.GameUpdateListener {

    private final Game game;

    private GameUpdateListener listener;

    private final Player player;

    public GameController(Game game, Player player) {
        this.game = game;
        this.player = player;
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

    public void playAssistantCard(AssistantCard card) {
        game.playAssistantCard(player, card);
    }

    public void selectCloud(StudentsContainer cloud) {
        game.selectCloud(player, cloud);
    }

    public void moveMotherNature(int steps) {
        game.moveMotherNature(player, steps);
    }

    public void placeStudents(StudentsContainer inSchool, Map<Integer, StudentsContainer> inIslands) {
        game.putStudents(
                player,
                inSchool,
                inIslands.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                e -> game.getIslands().get(e.getKey()),
                                Map.Entry::getValue
                        ))
        );
    }

    public interface GameUpdateListener {
        void onGameUpdate(ReducedGame game);
    }
}
