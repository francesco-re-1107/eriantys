package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.SortedMap;

public class Round {
    private SortedMap<Player, AssistantCard> playedAssistantCards;

    private SortedMap<Player, CharacterCard> playedCharacterCards;

    private ArrayList<StudentsContainer> clouds;

    public Iterable<Player> iterator() {
        return null;
    }
}
