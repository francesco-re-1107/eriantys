package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.AssistantCard;

/**
 * This class represents the request to play an assistant card in the currently played game
 */
public class PlayAssistantCardRequest extends GameRequest{

    private final AssistantCard card;

    public PlayAssistantCardRequest(AssistantCard card) {
        this.card = card;
    }

    @Override
    public Reply handleGameRequest(VirtualView vw, GameController gc) {
        gc.playAssistantCard(card);
        return new AckReply(getRequestId());
    }
}
