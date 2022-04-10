package it.polimi.ingsw.model;

public interface Stage {
    enum Plan implements Stage {
        PLAN
    }
    enum Attack implements Stage {
        STARTING,
        PLACING,
        CARD,
        MOVING,
        SELECTING_CLOUD
    }
}