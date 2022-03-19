package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {

    private String nickname;

    private StudentsContainer entrance;

    private StudentsContainer students;

    private int towersCount;

    private Tower towerColor;

    private ArrayList<AssistantCard> deck;

    private int coins = 1;

    public int calculateInfluence(Island island) {
        return 0;
    }
}
