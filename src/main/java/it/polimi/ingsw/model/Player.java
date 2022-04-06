package it.polimi.ingsw.model;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * This class models a player of the game, storing all the information of the board
 */
public class Player {

    /**
     * Stores the nickname chose by the player during setup
     */
    private final String nickname;

    /**
     * Stores all the students collected in the entrance of the player board
     */
    private final StudentsContainer entrance;

    /**
     * Stores all the students collected in the school
     */
    private final StudentsContainer school;

    /**
     * Stores the number of towers the player currently has on his board
     * TODO: check if it's too much redundant because it could be calculated by counting the towers on the islands
     */
    private int towersCount;

    /**
     * Stores the towers color of this player
     */
    private final Tower towerColor;

    /**
     * Stores the assistant cards of this player and whether they're already used
     */
    private final Map<AssistantCard, Boolean> deck;

    /**
     * Stores the number of coins this player currently has, 1 coin is given to everyone by default
     */
    private int coins = 1;

    /**
     * Create a player
     * @param nickname nickname choose by the client
     * @param towerColor color of the towers of this player
     * @param towersCount number of towers at the beginning of the game
     * @param entrance randomly picked entrance
     */
    public Player(String nickname, Tower towerColor, int towersCount, StudentsContainer entrance) {
        this.nickname = nickname;
        this.towerColor = towerColor;
        this.towersCount = towersCount;
        this.entrance = entrance;
        this.school = new StudentsContainer();

        this.deck = new HashMap<>();
        AssistantCard.getDefaultDeck().forEach(
                (c) -> this.deck.put(c, false)
        );
    }

    /**
     * Check if this player can play the given card.
     * In other words return true if the player hasn't played the card yet.
     * @param card the card to test
     * @return whether this user can play the given card
     */
    public boolean canPlayAssistantCard(AssistantCard card){
        return !deck.get(card);
    }

    /**
     * Play the given assistant card and set it as used
     * @param card the card to play
     */
    public void playAssistantCard(AssistantCard card){
        //TODO: find a better way to handle this
        if(deck.get(card))
            throw new InvalidOperationException("Card already used by this user");

       deck.put(card, true);
    }

    /**
     * @return the nickname of this player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the number of towers left on the board of this player
     */
    public int getTowersCount() {
        return towersCount;
    }

    /**
     * Set the number of towers left on the board of this player
     * TODO: improve this part of code
     * @param towersCount new number of towers to set
     */
    public void setTowersCount(int towersCount){
        this.towersCount = towersCount;
    }

    /**
     * @return the tower color chose for this player
     */
    public Tower getTowerColor() {
        return towerColor;
    }

    /**
     * @return the number of coins collected by this player
     */
    public int getCoins() {
        return coins;
    }

    /**
     * @return a copy of the entrance students container
     */
    public StudentsContainer getEntrance() {
        return new StudentsContainer(entrance);
    }

    /**
     * @return a copy of the school students container
     */
    public StudentsContainer getSchool() {
        return new StudentsContainer(school);
    }

    /**
     * @return a copy of the assistant cards deck
     */
    public Map<AssistantCard, Boolean> getDeck() {
        return new HashMap<>(deck);
    }

    /**
     * Used when the player select a cloud, all the students in the cloud will be added to the
     * entrance of this player.
     * @param cloud selected cloud
     */
    public void addCloudToEntrance(StudentsContainer cloud) {
        this.entrance.addAll(cloud);
    }

    /**
     * Move students from entrance to school
     * @param toSchool the students to move
     */
    public void addStudentsToSchool(StudentsContainer toSchool) {
        entrance.removeAll(toSchool);
        school.addAll(toSchool);
    }

    /**
     * @return the number of assistant cards left to use
     */
    public int getAssistantCardsLeftCount() {
        return (int) deck.entrySet()
                .stream()
                .filter((e) -> !e.getValue()) //only cards not used
                .count();
    }

    /**
     * Decrement coins count by howMany
     * @param howMany the number of coins to use
     */
    public void useCoins(int howMany) {
        if(howMany > coins)
            throw new InvalidOperationException();

        coins -= howMany;
    }

    public void addStudentsToIsland(Island island, StudentsContainer students) {
        entrance.removeAll(students);
        island.addStudents(students);
    }

    /**
     * Swap students between entrance and school
     * @param studentsToRemove from school and add to entrance
     * @param studentsToAdd to school and remove from entrance
     */
    public void swapStudents(StudentsContainer studentsToRemove, StudentsContainer studentsToAdd) {
        if(studentsToAdd.getSize() != studentsToRemove.getSize())
            throw new InvalidOperationException("Cannot swap students containers of different size");

        school.removeAll(studentsToRemove);
        entrance.addAll(studentsToRemove);
        entrance.removeAll(studentsToAdd);
        school.addAll(studentsToAdd);
    }
}
