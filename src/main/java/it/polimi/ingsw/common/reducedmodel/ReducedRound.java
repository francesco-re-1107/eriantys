package it.polimi.ingsw.common.reducedmodel;

import it.polimi.ingsw.server.model.Round;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.util.List;

public record ReducedRound(
        Round.Stage stage,
        ReducedPlayer currentPlayer,
        List<StudentsContainer> clouds
        //Map<ReducedPlayer, AssistantCard> playedAssistantCards
) {
    public static ReducedRound fromRound(Round r) {
        return new ReducedRound(
                r.getStage(),
                ReducedPlayer.fromPlayer(r.getCurrentPlayer()),
                r.getClouds()
                /*r.getPlayers()
                        .stream()
                        .collect(Collectors.toMap(
                                ReducedPlayer::fromPlayer,
                                p -> r.getCardPlayedBy(p).orElse(null))
                        )*/
        );
    }
}
