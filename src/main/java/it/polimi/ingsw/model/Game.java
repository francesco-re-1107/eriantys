package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Map;

public class Game {

    private ArrayList<Player> players;
    private final int numberOfPlayers;

    private ArrayList<Island> islands;

    private RandomizedStudentsContainer studentsBag;

    private int motherNaturePosition;

    private ArrayList<Round> rounds;

    private ArrayList<CharacterCard> characterCards;

    private Map<Student, Player> currentProfessors;

    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void initializeGame() {
        if (players.size() != numberOfPlayers)

    }

    public void addPlayer(String nickname) {
        if (players.size() >= numberOfPlayers)
            return;

        players.add(
                new Player(
                        nickname,
                        Tower.values()[players.size()], //0->BLACK, 1->WHITE, 2->GREY
                        numberOfPlayers
                )
        );
    }

    public void checkAndMergeIslands() {

    }

    public void addOnReceivedMessageListener() {

    }

    public void getProfessorsForPlayer(Player player) {

    }

    public void updateProfessors() {

    }
}
