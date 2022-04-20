package it.polimi.ingsw.common.reducedmodel;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Student;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * This record represents a game for the client.
 * This is a reduced version of the Game class, so it used in the communication with the client
 */
public record ReducedGame(
        UUID uuid,
        int numberOfPlayers,
        boolean expertMode,
        List<ReducedPlayer> players,
        List<ReducedIsland> islands,
        int motherNaturePosition,
        int studentsBagSize,
        ReducedRound currentRound,
        Map<String, Integer> characterCards,
        Map<Student, ReducedPlayer> currentProfessors,
        Game.State currentState,
        ReducedPlayer winner
) implements Serializable {

    /**
     * Create a ReducedGame starting from a Game
     * @param g the game to translate
     * @return the ReducedGame just created
     */
    public static ReducedGame fromGame(Game g){
        AtomicReference<ReducedPlayer> winner = new AtomicReference<>(null);
        g.getWinner().ifPresent(w -> winner.set(ReducedPlayer.fromPlayer(w)));

        ReducedRound reducedRound = null;
        if(g.getCurrentRound() != null)
            reducedRound = ReducedRound.fromRound(g.getCurrentRound());

        return new ReducedGame(
                g.getUUID(),
                g.getNumberOfPlayers(),
                g.isExpertMode(),
                g.getPlayers()
                        .stream()
                        .map(ReducedPlayer::fromPlayer)
                        .toList(),
                g.getIslands()
                        .stream()
                        .map(ReducedIsland::fromIsland)
                        .toList(),
                g.getMotherNaturePosition(),
                g.getStudentsBag().getSize(),
                reducedRound,
                g.getCharacterCards(),
                g.getProfessors()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> ReducedPlayer.fromPlayer(e.getValue())
                        )),
                g.getGameState(),
                winner.get()
        );
    }
}