package it.polimi.ingsw.server.model.charactercards;

import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.InfluenceCalculator;
import it.polimi.ingsw.server.model.Player;

import java.io.Serial;

/**
 * This class is the supertype for all the character cards that affect influence calculation
 */
public abstract class InfluenceCharacterCard implements CharacterCard {

    @Serial
    private static final long serialVersionUID = 7863976591165545747L;

    /**
     * @param cardPlayer the player who played the card
     * @return the specific influence calculator for every card depending on the card effect
     */
    public abstract InfluenceCalculator getInfluenceCalculator(Player cardPlayer);

    /**
     * Every influence card is played in the same way
     * @param game
     */
    @Override
    public void play(Game game) {
        game.getCurrentRound()
                .setTemporaryInfluenceCalculator(
                        getInfluenceCalculator(game.getCurrentRound().getCurrentPlayer())
                );
    }
}
