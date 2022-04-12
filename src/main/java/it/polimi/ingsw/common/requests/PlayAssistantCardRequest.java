package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.server.model.AssistantCard;

public class PlayAssistantCardRequest extends GameRequest{

    private final AssistantCard card;

    public PlayAssistantCardRequest(AssistantCard card) {
        this.card = card;
    }

    public AssistantCard getCard() {
        return card;
    }
}
