package it.polimi.ingsw.exceptions;

public class InvalidOperationException extends Error{
    public InvalidOperationException() {
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}
