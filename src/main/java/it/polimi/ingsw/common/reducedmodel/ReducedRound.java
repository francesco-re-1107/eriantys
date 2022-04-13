package it.polimi.ingsw.common.reducedmodel;

import it.polimi.ingsw.server.model.Round;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.util.List;

/**
 * This record represents a Round for the client.
 * This is a reduced version of the Round class, so it used in the communication with the client
 */
public record ReducedRound(
        Round.Stage stage,
        ReducedPlayer currentPlayer,
        List<StudentsContainer> clouds
        //Map<ReducedPlayer, AssistantCard> playedAssistantCards
) {

    /**
     * Create a ReducedRound starting from a Round
     * @param r the round to translate
     * @return the ReducedRound just created
     */
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
