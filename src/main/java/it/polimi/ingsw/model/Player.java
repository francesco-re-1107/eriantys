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
     * @param numberOfPlayers used to determine the number of towers and the max size of the entrance
     */
    public Player(String nickname, Tower towerColor, int numberOfPlayers) {
        this.nickname = nickname;
        this.towerColor = towerColor;
        this.towersCount = numberOfPlayers == 2 ?
                Constants.TWO_PLAYERS.TOWERS_COUNT :
                Constants.THREE_PLAYERS.TOWERS_COUNT;

        this.entrance =
                new StudentsContainer(numberOfPlayers == 2 ?
                        Constants.TWO_PLAYERS.ENTRANCE_SIZE :
                        Constants.THREE_PLAYERS.ENTRANCE_SIZE);
        this.school = new StudentsContainer();

        this.deck = new HashMap<>();
        AssistantCard.getDefaultDeck().forEach(
                (c) -> this.deck.put(c, false)
        );
    }

    /**
     * Check if the user can afford a character card
     * @param card the card to check
     * @return true if this player can afford the card
     */
    public boolean canAffordCharacterCard(CharacterCard card){
        return this.coins >= card.getCost();
    }

    /**
     * Buy a character card
     * @param card the card to buy
     * @return true if the card was bought (so the player could afford it), false otherwise
     */
    public boolean buyCharacterCard(CharacterCard card){
        if(!canAffordCharacterCard(card))
            return false;

        this.coins -= card.getCost();

        return true;
    }

    public boolean canPlayAssistantCard(AssistantCard card){
        return !deck.get(card);
    }

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

    public StudentsContainer getEntrance() {
        return new StudentsContainer(entrance);
    }

    public StudentsContainer getSchool() {
        return new StudentsContainer(school);
    }

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

    public void addStudentsToSchool(StudentsContainer toSchool) {
        entrance.removeAll(toSchool);
        school.addAll(toSchool);
    }

    public int getAssistantCardsLeftCount() {
        return (int) deck.entrySet()
                .stream()
                .filter((e) -> !e.getValue()) //only cards not used
                .count();
    }
}
