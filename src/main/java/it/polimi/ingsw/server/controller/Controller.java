package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.common.exceptions.GameJoiningError;
import it.polimi.ingsw.common.exceptions.NicknameNotRegisteredError;
import it.polimi.ingsw.common.exceptions.NicknameNotValidException;
import it.polimi.ingsw.common.reducedmodel.GameListItem;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.io.*;
import java.util.*;

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
    private List<Game> games;

    private final Set<String> registeredNicknames;

    /**
     * Create a controller
     */
    public Controller() {
        this.games = new ArrayList<>();
        this.registeredNicknames = new HashSet<>();

        loadBackupIfPresent();
        startAutoSaveTimer();
    }

    /**
     * Register nickname of a player.
     * This method must be called as soon as a player connects to this server.
     *
     * @param nickname
     * @param nickname
     * @return the gameController if this player was in a game before disconnecting, null otherwise
     * @throws DuplicatedNicknameException if the nickname is already in use by another player
     * @throws NicknameNotValidException   if the nickname does not meet all the criteria
     */
    public synchronized GameController registerNickname(String nickname)
            throws DuplicatedNicknameException, NicknameNotValidException {

        if (isRegistered(nickname))
            throw new DuplicatedNicknameException();

        if (!Utils.isValidNickname(nickname))
            throw new NicknameNotValidException();

        registeredNicknames.add(nickname);

        //check if the player was previously in a game
        //return the game controller if so
        var foundGame = findGameByNickname(nickname);
        if (foundGame != null) {
            var foundPlayer = findPlayerInGame(nickname, foundGame);

            var gc = new GameController(foundGame, foundPlayer);
            foundGame.setPlayerReconnected(foundPlayer);
            return gc;
        }

        return null;
    }

    /**
     * Find the player in the game with the given nickname.
     *
     * @param nickname  The nickname of the player to find.
     * @param game The game that the player is playing.
     * @return the player with the given nickname, or null if not found.
     */
    private synchronized Player findPlayerInGame(String nickname, Game game) {
        return game.getPlayers()
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
    private synchronized Game findGameByNickname(String nickname) {
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
    public synchronized List<GameListItem> listGames() {
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
        if (!isRegistered(nickname))
            throw new NicknameNotRegisteredError();

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
     * @param nickname client nickname
     * @param numberOfPlayers number of player desired for this game
     * @param expertMode      whether the game will use expert rules
     * @return the GameController needed to control this game
     */
    public synchronized GameController createGame(String nickname, int numberOfPlayers, boolean expertMode) {
        if (!isRegistered(nickname))
            throw new NicknameNotRegisteredError();

        var g = new Game(numberOfPlayers, expertMode);
        g.addGameUpdateListener(this);
        var p = g.addPlayer(nickname);
        //g.addGameUpdateListener(this); remove game when it is finished
        games.add(g);

        return new GameController(g, p);
    }

    /**
     * This callback is called for every game on this server.
     * If a game finishes this will be removed from the list
     *
     * @param game
     */
    @Override
    public synchronized void onGameUpdate(Game game) {
        var state = game.getGameState();

        //if no one is connected to the game when it is not started yet, remove it
        if(game.getCurrentNumberOfPlayers() == 0)
            games.remove(game);

        //finished games are removed from the list
        if (state == Game.State.TERMINATED || state == Game.State.FINISHED)
            games.remove(game);

        //TODO: fix when games are loaded from backup
        //if all players get disconnected (not because of a server crash), remove the game
        /*if(state == Game.State.PAUSED &&
                game.getPlayers().stream().noneMatch(Player::isConnected))
            games.remove(game);*/
    }

    /**
     * Check if a nickname is registered, in other words if it is associated with a nickname
     * @param nickname the client nickname
     * @return true if the virtual view is registered, false otherwise
     */
    public synchronized boolean isRegistered(String nickname) {
        return registeredNicknames.contains(nickname);
    }

    /**
     * Start the scheduling of games auto-save
     */
    private synchronized void startAutoSaveTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                saveGames();
            }
        }, Constants.AUTO_SAVE_INTERVAL, Constants.AUTO_SAVE_INTERVAL);
    }

    /**
     * Load backup from file, if it exists
     */
    @SuppressWarnings("unchecked")
    private synchronized void loadBackupIfPresent() {
        var backupFile = Utils.getServerConfig()
                .backupFolder()
                .resolve("games.bak")
                .toAbsolutePath()
                .toFile();

        if(!backupFile.isFile()) return;

        try (
                var f = new FileInputStream(backupFile);
                var o = new ObjectInputStream(f);
        ) {
            //load games
            games = (ArrayList<Game>) o.readObject();

            //remove ended games or created games (i.e. games that were never started)
            games.removeIf(g -> g.getGameState() == Game.State.TERMINATED ||
                            g.getGameState() == Game.State.FINISHED ||
                            g.getGameState() == Game.State.CREATED);

            for (var g : games) {
                g.initializeFromBackup();
                g.addGameUpdateListener(this);
            }

            Utils.LOGGER.info(games.size() + " games loaded from backup");
        } catch (Exception e) {
            e.printStackTrace();
            Utils.LOGGER.warning("Error loading games backup, probably corrupted file");
        }
    }

    /**
     * Save games list to file
     */
    private synchronized void saveGames() {
        var backupFile = Utils.getServerConfig()
                .backupFolder()
                .resolve("games.bak")
                .toAbsolutePath()
                .toFile();

        try (
                var f = new FileOutputStream(backupFile);
                var o = new ObjectOutputStream(f)
        ) {
            o.writeObject(games);
            Utils.LOGGER.info("Games saved at " + backupFile.getAbsolutePath());
        } catch (IOException e) {
            Utils.LOGGER.warning("Error saving games: " + e.getMessage());
        }
    }

    /**
     * Called when a registered client disconnects
     * @param nickname
     */
    public synchronized void disconnect(String nickname) {
        registeredNicknames.remove(nickname);
    }
}
