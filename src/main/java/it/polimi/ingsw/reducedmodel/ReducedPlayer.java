package it.polimi.ingsw.reducedmodel;

import it.polimi.ingsw.model.*;

import java.util.Map;

public record ReducedPlayer(
        String nickname,
        StudentsContainer entrance,
        StudentsContainer school,
        int towersCount,
        Tower towerColor,
        Map<AssistantCard, Boolean> deck,
        int coins
) {

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
