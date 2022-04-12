package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.common.exceptions.GameJoiningError;
import it.polimi.ingsw.common.exceptions.NicknameNotRegisteredError;
import it.polimi.ingsw.common.exceptions.NicknameNotValidException;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Controller implements Game.GameUpdateListener {

    private final List<Game> games;

    private final Map<String, VirtualView> nicknameVirtualViewMap;

    public Controller() {
        this.games = new ArrayList<>();
        this.nicknameVirtualViewMap = new ConcurrentHashMap<>();
    }

    public void registerNickname(String nickname, VirtualView virtualView)
            throws DuplicatedNicknameException, NicknameNotValidException {
        if(nicknameVirtualViewMap.containsKey(nickname)) {
            if (nicknameVirtualViewMap.get(nickname).isConnected())
                throw new DuplicatedNicknameException();

            //replace disconnected view with the new one
        }else{
            if(Utils.isValidNickname(nickname))
                throw new NicknameNotValidException();

            //insert the virtual view in the map
        }
        nicknameVirtualViewMap.put(nickname, virtualView);
    }

    public List<ReducedGame> listGames() {
        return games.stream()
                .filter(g -> g.getGameState() == Game.State.CREATED &&
                        g.getCurrentNumberOfPlayers() < g.getNumberOfPlayers())
                .map(ReducedGame::fromGame)
                .toList();
    }

    public synchronized GameController joinGame(String nickname, UUID uuid) {
        checkIfNicknameRegistered(nickname);

        Optional<Game> selectedGame = games.stream()
                .parallel()
                .filter(g -> g.getUUID() == uuid)
                .findFirst();

        if(selectedGame.isEmpty())
            throw new GameJoiningError("Game not found");

        Game g = selectedGame.get();

        //game not started yet and with space for another player
        if(g.getGameState() == Game.State.CREATED &&
                g.getCurrentNumberOfPlayers() < g.getNumberOfPlayers() ) {

            Player p = g.addPlayer(nickname);

            return new GameController(g, p);
        }else{
            throw new GameJoiningError("This game is already full");
        }
    }

    public GameController createGame(String nickname, int numberOfPlayers, boolean expertMode) {
        checkIfNicknameRegistered(nickname);

        Game g = new Game(numberOfPlayers, expertMode);
        g.addGameUpdateListener(this);
        Player p = g.addPlayer(nickname);
        //g.addGameUpdateListener(this); remove game when it is finished
        games.add(g);

        return new GameController(g, p);
    }

    private void checkIfNicknameRegistered(String nickname) {
        if(!nicknameVirtualViewMap.containsKey(nickname))
            throw new NicknameNotRegisteredError();
    }

    @Override
    public void onGameUpdate(Game game) {
        Game.State state = game.getGameState();

        //finished games are removed from the list
        if(state == Game.State.TERMINATED || state == Game.State.FINISHED)
            games.remove(game);
    }
}
