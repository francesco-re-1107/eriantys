package it.polimi.ingsw.common.exceptions;

/**
 * A generic error used when an inconsistent state is reached in the project
 */
public class InvalidOperationError extends Error{

    public InvalidOperationError() {
    }

    public InvalidOperationError(String message) {
        super(message);
    }
}
