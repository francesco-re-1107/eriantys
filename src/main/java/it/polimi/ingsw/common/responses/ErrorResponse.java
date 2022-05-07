package it.polimi.ingsw.common.responses;

public class ErrorResponse extends Response {

    public ErrorResponse(Throwable e) {
        super(e);
    }

}
