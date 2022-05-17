package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.common.exceptions.GameJoiningError;
import it.polimi.ingsw.common.exceptions.NicknameNotRegisteredError;
import it.polimi.ingsw.common.exceptions.NicknameNotValidException;
import it.polimi.ingsw.common.reducedmodel.GameListItem;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the main controller for the server.
 * There's only 1 instance throughout the server.
 * This controller handles all the general requests like registering nicknames, listing games, joining games...
 * All the other requests regarding games are handled instead by the GameController.
 */
public class Controller implements Game.GameUpdateListener {

    /**
     * Stores all the currently active games
     */
    private final List<Game> games;

    /**
     * Stores nicknames of the registered players and their virtual view
     */
    private final Map<String, VirtualView> nicknameVirtualViewMap;

    /**
     * Create a controller
     */
    public Controller() {
        this.games = new ArrayList<>();
        this.nicknameVirtualViewMap = new ConcurrentHashMap<>();
    }

    /**
     * Register nickname of a player.
     * This method must be called as soon as a player connects to this server.
     *
     * @param nickname
     * @param virtualView
     * @return the gameController if this player was in a game before disconnecting, null otherwise
     * @throws DuplicatedNicknameException if the nickname is already in use by another player
     * @throws NicknameNotValidException   if the nickname does not meet all the criteria
     */
    public synchronized GameController registerNickname(String nickname, VirtualView virtualView)
            throws DuplicatedNicknameException, NicknameNotValidException {
        if (nicknameVirtualViewMap.containsKey(nickname)) {
            if (nicknameVirtualViewMap.get(nickname).isConnected())
                throw new DuplicatedNicknameException();

            //replace disconnected view with the new one
            nicknameVirtualViewMap.put(nickname, virtualView);

            //find the previous game
            var foundGame = findGameByNickname(nickname);

            if (foundGame != null) {
                var foundPlayer = findPlayerInGame(nickname, foundGame);

                var gc = new GameController(foundGame, foundPlayer);
                foundGame.setPlayerReconnected(foundPlayer);
                return gc;
            }
        } else {
            if (!Utils.isValidNickname(nickname))
                throw new NicknameNotValidException();

            //insert the virtual view in the map
            nicknameVirtualViewMap.put(nickname, virtualView);
        }
        return null;
    }


    /**
     * Find the player in the game with the given nickname.
     *
     * @param nickname  The nickname of the player to find.
     * @param foundGame The game that the player is playing.
     * @return the player with the given nickname, or null if not found.
     */
    private Player findPlayerInGame(String nickname, Game foundGame) {
        return foundGame.getPlayers()
                .stream()
                .filter(p -> p.getNickname().equals(nickname))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find the game played by the specified player nickname
     *
     * @param nickname the nickname of the player
     * @return the game played by the player, null if the player is not in a game
     */
    private Game findGameByNickname(String nickname) {
        for (var g : games) {
            for (var p : g.getPlayers())
                if (Objects.equals(p.getNickname(), nickname))
                    return g;
        }
        return null;
    }

    /**
     * Obtain a list of all the games available in this server.
     * Only the games not started yet and with enough space are returned
     *
     * @return the list of games
     */
    public List<GameListItem> listGames() {
        return games.stream()
                .filter(g -> g.getGameState() == Game.State.CREATED &&
                        g.getCurrentNumberOfPlayers() < g.getNumberOfPlayers())
                .map(GameListItem::fromGame)
                .toList();
    }

    /**
     * Join the specified game
     *
     * @param nickname of the player playing this action
     * @param uuid     of the game to join
     * @return the GameController needed to control this game
     */
    public synchronized GameController joinGame(String nickname, UUID uuid) {
        checkIfNicknameRegistered(nickname);

        var selectedGame = games.stream()
                .parallel()
                .filter(g -> g.getUUID().equals(uuid))
                .findFirst();

        if (selectedGame.isEmpty())
            throw new GameJoiningError("Game not found");

        var g = selectedGame.get();

        //game not started yet and with space for another player
        if (g.getGameState() == Game.State.CREATED &&
                g.getCurrentNumberOfPlayers() < g.getNumberOfPlayers()) {

            var p = g.addPlayer(nickname);

            if (g.getCurrentNumberOfPlayers() == g.getNumberOfPlayers())
                g.startGame();

            return new GameController(g, p);
        } else {
            throw new GameJoiningError("This game is already full");
        }
    }

    /**
     * Create a new game
     *
     * @param nickname        of the player playing this action
     * @param numberOfPlayers number of player desired for this game
     * @param expertMode      whether the game will use expert rules
     * @return the GameController needed to control this game
     */
    public synchronized GameController createGame(String nickname, int numberOfPlayers, boolean expertMode) {
        checkIfNicknameRegistered(nickname);

        var g = new Game(numberOfPlayers, expertMode);
        g.addGameUpdateListener(this);
        var p = g.addPlayer(nickname);
        //g.addGameUpdateListener(this); remove game when it is finished
        games.add(g);

        return new GameController(g, p);
    }

    /**
     * Check if the nickname is correctly registered on this server.
     * If not a NicknameNotRegisteredError is thrown.
     *
     * @param nickname
     */
    private synchronized void checkIfNicknameRegistered(String nickname) {
        if (!nicknameVirtualViewMap.containsKey(nickname))
            throw new NicknameNotRegisteredError();
    }

    /**
     * This callback is called for every game on this server.
     * If a game finishes this will be removed from the list
     *
     * @param game
     */
    @Override
    public void onGameUpdate(Game game) {
        var state = game.getGameState();

        //if no one is connected to the game, remove it
        if(game.getCurrentNumberOfPlayers() == 0) {
            games.remove(game);
        }

        //finished games are removed from the list
        if (state == Game.State.TERMINATED || state == Game.State.FINISHED) {
            games.remove(game);

            //when a game is finished or terminated remove all the disconnected players
            //connected players instead are kept, so they can start a new game if they want
            for (Player p : game.getPlayers()) {
                if (!nicknameVirtualViewMap.get(p.getNickname()).isConnected())
                    nicknameVirtualViewMap.remove(p.getNickname());
            }
        }
    }

    /**
     * Find nickname associated with a given virtual view
     * @param vw the virtual view
     * @return the nickname associated with the given virtual view
     */
    public String findNicknameByVirtualView(VirtualView vw) {
        return nicknameVirtualViewMap.entrySet()
                .stream()
                .filter(e -> e.getValue() == vw)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if a virtual view is registered, in other words if it is associated with a nickname
     * @param vw the virtual view
     * @return true if the virtual view is registered, false otherwise
     */
    public boolean isRegistered(VirtualView vw) {
        return findNicknameByVirtualView(vw) != null;
    }
}
