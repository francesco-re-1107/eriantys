package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Player {

    private String nickname;

    private StudentsContainer entrance;

    private StudentsContainer school;

    private int towersCount;

    private Tower towerColor;

    private Map<AssistantCard, Boolean> deck;

    private int coins = 1;

    public Player(String nickname, Tower towerColor, int numberOfPlayers) {
        this.nickname = nickname;
        this.towerColor = towerColor;
        this.towersCount = numberOfPlayers == 2 ? 8 : 6; //2 -> 8 towers, 3 -> 6 towers

        this.entrance = new StudentsContainer(numberOfPlayers == 2 ? 7 : 9);
        this.school = new StudentsContainer();

        this.deck = new HashMap<>();
        AssistantCard.getDefaultDeck().forEach(
                (c) -> this.deck.put(c, false)
        );
    }

    public int calculateInfluence(Island island) {
        return 0;
    }
}
