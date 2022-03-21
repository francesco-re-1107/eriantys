package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Player {

    private String nickname;

    private final StudentsContainer entrance;

    private final StudentsContainer school;

    private int towersCount;

    private final Tower towerColor;

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



    public boolean buyCharacterCard(CharacterCard card){
        if (this.coins < card.getCost())
            return false;

        this.coins -= card.getCost();

        return true;
    }

    public String getNickname() {
        return nickname;
    }

    public int getTowersCount() {
        return towersCount;
    }

    public void setTowersCount(int towersCount){
        this.towersCount = towersCount;
    }

    public Tower getTowerColor() {
        return towerColor;
    }

    public int getCoins() {
        return coins;
    }

    public int calculateInfluence(Island island) {
        return 0;
    }
}
