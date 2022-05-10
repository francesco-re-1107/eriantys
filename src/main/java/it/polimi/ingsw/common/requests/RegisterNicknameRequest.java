package it.polimi.ingsw.common.requests;

/**
 * This class represents the request to register a nickname on the connected server, this should be the first request sent to the server
 */
public class RegisterNicknameRequest extends Request{

    private final String nickname;

    public RegisterNicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
