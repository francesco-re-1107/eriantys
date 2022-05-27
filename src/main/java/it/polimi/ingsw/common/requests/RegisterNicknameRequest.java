package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;
import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.GameController;

/**
 * This class represents the request to register a nickname on the connected server, this should be the first request sent to the server
 */
public class RegisterNicknameRequest extends Request{

    private final String nickname;

    public RegisterNicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public Reply handleRequest(VirtualView vw, Controller c, GameController gc) throws Exception {
        if(c.isRegistered(vw.getNickname()))
            throw new InvalidOperationError("Client already registered");

        vw.setGameController(c.registerNickname(nickname));
        vw.setNickname(nickname);

        return new AckReply(getRequestId());
    }
}
