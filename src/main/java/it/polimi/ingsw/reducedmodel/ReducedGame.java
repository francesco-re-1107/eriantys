package it.polimi.ingsw.reducedmodel;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record ReducedGame(
        int numberOfPlayers,
        boolean expertMode,
        List<ReducedPlayer> players,
        List<ReducedIsland> islands,
        int motherNaturePosition,
        ReducedRound currentRound,
        Map<String, Integer> characterCards,
        Map<Student, ReducedPlayer> currentProfessors,
        Game.State currentState,
        ReducedPlayer winner
) {

    public static ReducedGame fromGame(Game g){
        ReducedPlayer winner = null;
        if(g.getWinner().isPresent())
            winner = ReducedPlayer.fromPlayer(g.getWinner().get());

        return new ReducedGame(
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
                ReducedRound.fromRound(g.getCurrentRound()),
                g.getCharacterCards(),
                g.getProfessors()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> ReducedPlayer.fromPlayer(e.getValue())
                        )),
                g.getGameState(),
                winner
        );
    }
}
