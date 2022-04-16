package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.charactercards.GrandmaCharacterCard;

public class ReducedGrandmaCharacterCard extends ReducedCharacterCard {

    private final int islandIndex;

    public ReducedGrandmaCharacterCard(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new GrandmaCharacterCard(game.getIslands().get(islandIndex));
    }

}
