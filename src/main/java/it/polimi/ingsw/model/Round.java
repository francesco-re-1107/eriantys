package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidOperationException;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class Round {

    private Stage stage = Stage.PLAN;

    private SortedMap<Player, AssistantCard> playedAssistantCards;

    private SortedMap<Player, CharacterCard> playedCharacterCards;

    private final List<StudentsContainer> clouds;

    public Round(List<StudentsContainer> clouds) {
        this.clouds = clouds;
    }

    public void nextStage(){
        // only if .size == numPlayers
        if (stage == Stage.ATTACK)
            throw new InvalidOperationException("Already in ATTACK stage");
        stage = Stage.ATTACK;
    }

    /*public Iterable<Player> iterator() {
        return null;
    }*/

    public Stage getStage() {
        return stage;
    }

    public SortedMap<Player, AssistantCard> getPlayedAssistantCards() {
        return playedAssistantCards;
    }

    public SortedMap<Player, CharacterCard> getPlayedCharacterCards() {
        return playedCharacterCards;
    }

    public List<StudentsContainer> getClouds() {
        return new ArrayList<>(clouds);
    }

    public void removeCloud(StudentsContainer cloud) {
        clouds.remove(cloud);
    }

    enum Stage {
        PLAN,
        ATTACK
    }
}

