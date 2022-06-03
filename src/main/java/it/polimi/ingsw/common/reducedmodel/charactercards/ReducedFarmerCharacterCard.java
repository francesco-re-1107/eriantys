package it.polimi.ingsw.common.reducedmodel.charactercards;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.charactercards.FarmerCharacterCard;

import java.io.Serial;

public class ReducedFarmerCharacterCard extends ReducedCharacterCard {

    @Serial
    private static final long serialVersionUID = 7885817200791047017L;

    @Override
    public CharacterCard toCharacterCard(Game game) {
        return new FarmerCharacterCard();
    }
}
