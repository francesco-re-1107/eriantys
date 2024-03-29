package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This controller handles only one game.
 * Every virtual view that is playing a game has one of this, nad uses it to dispatch every
 * action regarding the game.
 */
public class GameController implements Game.GameUpdateListener {

    /**
     * The game controlled
     */
    private final Game game;

    /**
     * Listener for updates on the game
     */
    private Optional<GameUpdateListener> listener = Optional.empty();

    /**
     * The player controlled
     */
    private final Player player;

    private Game lastUpdate;

    /**
     * Create a GameController
     * @param game associated with this controller
     * @param player associated with this controller
     */
    public GameController(Game game, Player player) {
        this.game = game;
        this.player = player;
        game.addGameUpdateListener(this);
    }

    /**
     * Set a listener for every game update
     * @param listener the listener to set
     */
    public void setOnGameUpdateListener(GameUpdateListener listener) {
        this.listener = Optional.of(listener);

        //send the latest update to the new listener
        if(lastUpdate != null)
            listener.onGameUpdate(ReducedGame.fromGame(lastUpdate));
    }

    /**
     * The controller is a listener for the game.
     * For every update the controller translate the Game to a ReducedGame and send it to
     * the virtual view that is listening.
     * @param game the updated game
     */
    @Override
    public void onGameUpdate(Game game) {
        lastUpdate = game;
        listener.ifPresent(l -> l.onGameUpdate(ReducedGame.fromGame(game)));

        if(game.getGameState() == Game.State.FINISHED ||
                game.getGameState() == Game.State.TERMINATED)
            game.removeGameUpdateListener(this);
    }

    /**
     * This is called when a client disconnects, in order to notify the game that a player cannot
     * play until it reconnects.
     */
    public void disconnect() {
        game.setPlayerDisconnected(player);
        game.removeGameUpdateListener(this);
    }

    /**
     * This is called when a player click the leave game button.
     * The game is terminated instantly for every player
     */
    public void leaveGame() {
        game.leaveGame(player);
    }

    /**
     * Play assistant card for this player
     * @param card the card to play
     */
    public void playAssistantCard(AssistantCard card) {
        game.playAssistantCard(player, card);
    }

    /**
     * Place students for this player
     * @param inSchool students to place in school
     * @param inIslands students to place on islands
     */
    public void placeStudents(StudentsContainer inSchool, Map<Integer, StudentsContainer> inIslands) {
        game.placeStudents(
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

    /**
     * Play the given character card
     * @param reducedCard the card to play
     */
    public void playCharacterCard(ReducedCharacterCard reducedCard) {
        game.playCharacterCard(player, reducedCard.toCharacterCard(game));
    }

    /**
     * Move mother nature for this player
     * @param steps the number of steps to move
     */
    public void moveMotherNature(int steps) {
        game.moveMotherNature(player, steps);
    }

    /**
     * Select cloud for this player
     * @param cloud the cloud to select
     */
    public void selectCloud(StudentsContainer cloud) {
        game.selectCloud(player, cloud);
    }

    /**
     * This interface models a listener for every Game update
     */
    public interface GameUpdateListener {
        void onGameUpdate(ReducedGame game);
    }

}
