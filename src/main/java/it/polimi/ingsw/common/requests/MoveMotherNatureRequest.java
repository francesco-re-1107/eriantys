package it.polimi.ingsw.common.requests;

public class MoveMotherNatureRequest extends GameRequest{

    private final int steps;

    public MoveMotherNatureRequest(int steps) {
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }
}
