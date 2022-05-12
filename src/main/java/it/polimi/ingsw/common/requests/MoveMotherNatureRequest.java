package it.polimi.ingsw.common.requests;

/**
 * This class represents the request to move mother nature in the currently played game.
 */
public class MoveMotherNatureRequest extends GameRequest{

    private final int steps;

    public MoveMotherNatureRequest(int steps) {
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }
}
