package it.polimi.ingsw.common.requests;

public class RegisterNicknameRequest extends Request{

    private final String nickname;

    public RegisterNicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
