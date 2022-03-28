package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Island;

public class GrandmaCharacterCard extends CharacterCard {

    private Island island;

    public GrandmaCharacterCard(Island island) {
        super(2);
        this.island = island;
    }

    public Island getIsland() {
        return island;
    }
}
