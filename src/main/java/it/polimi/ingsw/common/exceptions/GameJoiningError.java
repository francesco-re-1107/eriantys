package it.polimi.ingsw.common.exceptions;

public class GameJoiningError extends Error {

    public GameJoiningError() {
        super();
    }

    public GameJoiningError(String message) {
        super(message);
    }
}
