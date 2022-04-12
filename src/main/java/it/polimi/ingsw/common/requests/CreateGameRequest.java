package it.polimi.ingsw.common.requests;

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
