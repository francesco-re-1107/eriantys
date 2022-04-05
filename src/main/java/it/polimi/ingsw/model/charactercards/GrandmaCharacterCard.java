package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Island;

/**
 * This class models the grandma card
 * EFFECT: put a no entry tile on the selected island
 */
public class GrandmaCharacterCard extends CharacterCard {

    private final Island island;

    public GrandmaCharacterCard(Island island) {
        super(2);
        this.island = island;
    }

    public Island getIsland() {
        return island;
    }
}
