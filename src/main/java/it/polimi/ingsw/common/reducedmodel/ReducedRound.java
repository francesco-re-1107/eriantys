package it.polimi.ingsw.common.reducedmodel;

import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Round;
import it.polimi.ingsw.server.model.Stage;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This record represents a Round for the client.
 * This is a reduced version of the Round class, so it used in the communication with the client
 */
public record ReducedRound(
        Stage stage,
        String currentPlayer, //nickname
        List<StudentsContainer> clouds,
        int additionalMotherNatureMoves,
        Map<String, AssistantCard> playedAssistantCards //nickname -> card
) implements Serializable {

    /**
     * Create a ReducedRound starting from a Round
     * @param r the round to translate
     * @return the ReducedRound just created
     */
    public static ReducedRound fromRound(Round r) {
        return new ReducedRound(
                r.getStage(),
                r.getCurrentPlayer().getNickname(),
                r.getClouds(),
                r.getAdditionalMotherNatureMoves(),
                r.getPlayedAssistantCards().entrySet().stream().collect(
                        Collectors.toMap(e -> e.getKey().getNickname(), Map.Entry::getValue)
                )
        );
    }
}
