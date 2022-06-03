package it.polimi.ingsw.common.exceptions;

import java.io.Serial;

/**
 * A generic error used when an inconsistent state is reached in the project
 */
public class InvalidOperationError extends Error{

    @Serial
    private static final long serialVersionUID = 8031552863889479174L;

    public InvalidOperationError() {
    }

    public InvalidOperationError(String message) {
        super(message);
    }
}
