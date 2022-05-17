package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;
import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.GamesListReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.GameController;

/**
 * This class represents the request to list the currently available games
 */
public class ListGamesRequest extends Request{
    @Override
    public Reply handleRequest(VirtualView vw, Controller c, GameController gc) {
        if(!vw.isRegistered())
            throw new InvalidOperationException("Client not registered");

        return new GamesListReply(getRequestId(), c.listGames());
    }
}
