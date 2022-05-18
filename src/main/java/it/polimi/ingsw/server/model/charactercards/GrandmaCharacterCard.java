package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Island;

/**
 * This class models the grandma card
 * EFFECT: put a no entry tile on the selected island
 */
public class GrandmaCharacterCard extends CharacterCard {

    private final Island island;

    public GrandmaCharacterCard(Island island) {
        super();
        this.island = island;
    }

    @Override
    public Character getCharacter() {
        return Character.GRANDMA;
    }

    @Override
    public void play(Game game) {
        if (!game.getIslands().contains(island))
            throw new InvalidOperationException("Island not present in this game");

        island.setNoEntry(true);
    }
}
