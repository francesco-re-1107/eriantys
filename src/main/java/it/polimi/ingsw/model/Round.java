package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidOperationException;

import java.util.ArrayList;
import java.util.SortedMap;

public class Round {

    private Stage stage = Stage.PLAN;

    private SortedMap<Player, AssistantCard> playedAssistantCards;

    private SortedMap<Player, CharacterCard> playedCharacterCards;

    private ArrayList<StudentsContainer> clouds;

    public Round(ArrayList<StudentsContainer> clouds) {
        this.clouds = clouds;
    }

    public void nextStage(){
        // only if .size == numPlayers
        if (stage == Stage.ATTACK)
            throw new InvalidOperationException();
        stage = Stage.ATTACK;
    }

    public Iterable<Player> iterator() {
        return null;
    }

    enum Stage {
        PLAN,
        ATTACK
    }
}

