package it.polimi.ingsw.common.reducedmodel;

import it.polimi.ingsw.server.model.*;

import java.io.Serializable;
import java.util.Map;

/**
 * This record represents a Player for the client.
 * This is a reduced version of the Player class, so it used in the communication with the client
 */
public record ReducedPlayer(
        String nickname,
        StudentsContainer entrance,
        StudentsContainer school,
        int towersCount,
        Tower towerColor,
        Map<AssistantCard, Boolean> deck,
        int coins
) implements Serializable {

    /**
     * Create a ReducedPlayer starting from a Player
     * @param p the player to translate
     * @return the ReducedPlayer just created
     */
    public static ReducedPlayer fromPlayer(Player p){
        return new ReducedPlayer(
                p.getNickname(),
                p.getEntrance(),
                p.getSchool(),
                p.getTowersCount(),
                p.getTowerColor(),
                p.getDeck(),
                p.getCoins()
        );
    }
}
