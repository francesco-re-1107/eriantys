package it.polimi.ingsw.common.exceptions;

/**
 * A generic error used when an inconsistent state is reached in the project
 */
public class InvalidOperationException extends Error{

    public InvalidOperationException() {
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}
