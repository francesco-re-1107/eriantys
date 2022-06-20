package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.charactercards.GrandmaCharacterCard;

import java.io.Serial;

public class ReducedGrandmaCharacterCard extends ReducedCharacterCard {

    @Serial
    private static final long serialVersionUID = -3881273740977799869L;

    private final int islandIndex;

    public ReducedGrandmaCharacterCard(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new GrandmaCharacterCard(game.getIslands().get(islandIndex));
    }

}
