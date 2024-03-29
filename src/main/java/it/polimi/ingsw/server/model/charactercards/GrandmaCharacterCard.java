package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Island;

import java.io.Serial;

/**
 * This class models the grandma card
 * EFFECT: put a no entry tile on the selected island
 */
public class GrandmaCharacterCard implements CharacterCard {

    @Serial
    private static final long serialVersionUID = 5987933858737561284L;

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
            throw new InvalidOperationError("Island not present in this game");

        island.setNoEntry(true);
    }
}
