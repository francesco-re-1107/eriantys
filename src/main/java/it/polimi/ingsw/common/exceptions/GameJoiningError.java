package it.polimi.ingsw.common.exceptions;

import java.io.Serial;

public class GameJoiningError extends Error {

    @Serial
    private static final long serialVersionUID = -5734255106657068608L;

    public GameJoiningError() {
        super();
    }

    public GameJoiningError(String message) {
        super(message);
    }
}
