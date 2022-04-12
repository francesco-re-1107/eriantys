package it.polimi.ingsw.common.responses;

public class ErrorResponse extends Response {

    private final Throwable throwable;

    public ErrorResponse(Throwable e) {
        super();
        this.throwable = e;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
