package it.polimi.ingsw.common.exceptions;

import java.io.Serial;

public class NicknameNotRegisteredError extends Error {

    @Serial
    private static final long serialVersionUID = -8280449336571975120L;

    public NicknameNotRegisteredError() {
        super();
    }

    public NicknameNotRegisteredError(String message) {
        super(message);
    }

}
