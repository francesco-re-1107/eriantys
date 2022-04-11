package it.polimi.ingsw.reducedmodel;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.StudentsContainer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
