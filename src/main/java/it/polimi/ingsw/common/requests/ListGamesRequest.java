package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.GamesListReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.GameController;

import java.io.Serial;

/**
 * This class represents the request to list the currently available games
 */
public class ListGamesRequest extends Request{
    @Serial
    private static final long serialVersionUID = 3841726434208634389L;

    @Override
    public Reply handleRequest(VirtualView vw, Controller c, GameController gc) {
        return new GamesListReply(getRequestId(), c.listGames());
    }
}
