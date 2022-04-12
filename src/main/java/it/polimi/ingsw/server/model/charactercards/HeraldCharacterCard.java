package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Island;

/**
 * This class models the herald card
 * EFFECT: Calculate influence on the selected island regardless of mother nature
 */
public class HeraldCharacterCard extends CharacterCard {

    private final Island island;

    public HeraldCharacterCard(Island island) {
        super(3);
        this.island = island;
    }

    public Island getIsland() {
        return island;
    }
}
