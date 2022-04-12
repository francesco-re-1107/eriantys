package it.polimi.ingsw.common.exceptions;

public class NicknameNotRegisteredError extends Error {

    public NicknameNotRegisteredError() {
        super();
    }

    public NicknameNotRegisteredError(String message) {
        super(message);
    }

}
