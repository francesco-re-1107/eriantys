package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.charactercards.HeraldCharacterCard;

import java.io.Serial;

public class ReducedHeraldCharacterCard extends ReducedCharacterCard {

    @Serial
    private static final long serialVersionUID = -5351718358500432879L;

    private final int islandIndex;

    public ReducedHeraldCharacterCard(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new HeraldCharacterCard(game.getIslands().get(islandIndex));
    }

}
