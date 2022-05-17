package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.GameController;

/**
 * This class represents the request to play a character card in the currently played game
 */
public class PlayCharacterCardRequest extends GameRequest{

    private final ReducedCharacterCard characterCard;

    public PlayCharacterCardRequest(ReducedCharacterCard characterCard) {
        this.characterCard = characterCard;
    }

    @Override
    public Reply handleGameRequest(VirtualView vw, GameController gc) {
        gc.playCharacterCard(characterCard);
        return new AckReply(getRequestId());
    }
}
