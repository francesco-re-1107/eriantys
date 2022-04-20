package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * This class models a player of the game, storing all the information of the board
 */
public class Player implements StudentsContainer.StudentNumberReachedListener, Serializable {

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
     * Whether the player is connected or not
     */
    private boolean isConnected;

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
        setUpListenersForCoins();

        this.deck = new HashMap<>();
        AssistantCard.getDefaultDeck().forEach(
                c -> this.deck.put(c, false)
        );
    }

    /**
     * This method will add listeners to the school container for 3, 6 and 9 students of each type
     * in order to receive coins
     */
    private void setUpListenersForCoins() {
        for (Student s : Student.values()) {
            this.school.addOnStudentNumberReachedListener(s, 3, this);
            this.school.addOnStudentNumberReachedListener(s, 6, this);
            this.school.addOnStudentNumberReachedListener(s, 9, this);
        }
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
        if(Boolean.TRUE.equals(deck.get(card)))
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
     * Decrement number of towers left on the board of this player
     * @param count number of towers to add to towersCount
     */
    public void incrementTowersCount(int count){
        this.towersCount += count;
    }

    /**
     * Increment number of towers left on the board of this player
     * @param count number of towers to subtract from towersCount
     */
    public void decrementTowersCount(int count){
        //not going below zero
        this.towersCount = Math.max(this.towersCount - count, 0);
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
     * Set the player as connected or not
     * @param connected
     */
    public void setConnected(boolean connected) {
        this.isConnected = connected;
    }

    /**
     * @return true if the player is currently connected, false otherwise
     */
    public boolean isConnected() {
        return isConnected;
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
                .filter(e -> !e.getValue()) //only cards not used
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

    /**
     * Move students from entrance to island
     * @param island
     * @param students
     */
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

    /**
     * Listener used for receiving coins
     * @param student
     * @param count
     */
    @Override
    public void onStudentNumberReachedListener(Student student, int count) {
        coins++;
        //coin obtained -> remove listener
        this.school.removeOnStudentNumberReachedListener(student, count);
    }

    public String prettyBoard(){
        String b = MessageFormat.format("Towers: {0}\nCoins: {1}\n\n", towersCount, coins);
        int maxEntrance = entrance.getStudents().values().stream().mapToInt(n -> n).max().orElse(0); // used to align
        for (var s: Student.values()) {
            var ch = s.toString().substring(0,1); // color first letter
            var count = entrance.getCountForStudent(s);
            b = b + ch.repeat(count) + " ".repeat(maxEntrance - count) + " | "; // entrance
            b = b + ch.repeat(school.getCountForStudent(s)) + '\n'; // school
        }
        return b;
    }
}
