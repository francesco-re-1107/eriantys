package it.polimi.ingsw.common.requests;

/**
 * This class represents the request to create a game on the server with the specified parameters and automatically
 * join it.
 */
public class CreateGameRequest extends Request{

    private final int numberOfPlayers;

    private final boolean expertMode;

    public CreateGameRequest(int numberOfPlayers, boolean expertMode) {
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }
}
