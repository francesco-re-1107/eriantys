package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Island;

/**
 * This class models the herald card
 * EFFECT: Calculate influence on the selected island regardless of mother nature
 */
public class HeraldCharacterCard extends CharacterCard {

    private final Island island;

    public HeraldCharacterCard(Island island) {
        super();
        this.island = island;
    }

    @Override
    public Character getCharacter() {
        return Character.HERALD;
    }

    @Override
    public void play(Game game) {
        if (!game.getIslands().contains(island))
            throw new InvalidOperationError("Island not present in this game");

        //calculate index on th egiven island
        game.calculateInfluenceOnIsland(island);

        //After influence calculation a player may have conquered a new island.
        //It's necessary to check if islands could be merged
        game.checkMergeableIslands();
    }
}
