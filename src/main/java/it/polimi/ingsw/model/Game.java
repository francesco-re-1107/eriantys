package it.polimi.ingsw.model;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.exceptions.InvalidOperationException;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

    private final ArrayList<Player> players;

    private final int numberOfPlayers;

    private List<Island> islands;

    private final RandomizedStudentsContainer studentsBag;

    private int motherNaturePosition = 0;

    private Round currentRound;

    private final ArrayList<CharacterCard> characterCards;

    private final Map<Student, Player> professors;

    private Status gameStatus = Status.CREATED;

    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.studentsBag = new RandomizedStudentsContainer(Constants.STUDENTS_BAG_NUMBER_PER_COLOR);
        this.characterCards = CharacterCard.generateRandomDeck(3);
        this.players = new ArrayList<>();
        this.professors = new HashMap<>();

        initializeIslands();
    }

    private void initializeIslands() {
        RandomizedStudentsContainer bag =
                new RandomizedStudentsContainer(Constants.ISLANDS_STUDENTS_BAG_NUMBER_PER_COLOR);

        this.islands = Arrays.asList(
                new Island(),
                new Island(bag.pickOneRandom()),
                new Island(bag.pickOneRandom()),
                new Island(bag.pickOneRandom()),
                new Island(bag.pickOneRandom()),
                new Island(bag.pickOneRandom()),
                new Island(),
                new Island(bag.pickOneRandom()),
                new Island(bag.pickOneRandom()),
                new Island(bag.pickOneRandom()),
                new Island(bag.pickOneRandom()),
                new Island(bag.pickOneRandom())
        );
    }

    public void startGame() {
        if (players.size() != numberOfPlayers)
            throw new InvalidOperationException("Number of players not reached yet");

        if(gameStatus != Status.CREATED)
            throw new InvalidOperationException(
                    MessageFormat.format("Game already started (gameStatus is {0})", gameStatus)
            );

        newRound();

        this.gameStatus = Status.STARTED;
    }

    private void newRound() {
        //generate clouds
        List<StudentsContainer> clouds = new ArrayList<>();
        for(int i = 0; i < numberOfPlayers; i++)
            clouds.add(studentsBag.pickManyRandom(
                    numberOfPlayers == 2 ?
                            Constants.TWO_PLAYERS.STUDENTS_PER_CLOUD :
                            Constants.THREE_PLAYERS.STUDENTS_PER_CLOUD)
            );

        currentRound = new Round(clouds); //TODO pass card order maybe
    }

    public void selectCloud(StudentsContainer cloud, Player player) {
        if(!currentRound.getClouds().contains(cloud))
            throw new InvalidOperationException("Cannot find selected cloud");

        if(!players.contains(player))
            throw new InvalidOperationException("Player not found");

        player.addCloudToEntrance(cloud);
        currentRound.removeCloud(cloud);
    }

    public void addPlayer(String nickname) throws DuplicatedNicknameException {
        if (players.size() >= numberOfPlayers)
            throw new InvalidOperationException("Players lobby is already full");

        if (gameStatus != Status.CREATED)
            throw new InvalidOperationException(
                    MessageFormat.format("Game already started (gameStatus is {0})", gameStatus)
            );

        if (players
                .stream()
                .anyMatch(p -> p.getNickname().equals(nickname)))
            throw new DuplicatedNicknameException();


        players.add(
                new Player(
                        nickname,
                        Tower.values()[players.size()], //0->BLACK, 1->WHITE, 2->GREY
                        numberOfPlayers
                )
        );
    }

    public void moveMotherNature(int steps){
        this.motherNaturePosition = calculateMotherNatureIndex(steps);

        this.calculateInfluenceOnCurrentIsland();

        this.checkAndMergeIslands();
    }

    private void calculateInfluenceOnCurrentIsland(){
        Island curr = getCurrentIsland();

        int max = -1;
        Optional<Player> maxP = Optional.empty();

        for(Player p: players){
            int infl = calculateInfluence(curr, p);

            if(infl > max){
                max = infl;
                maxP = Optional.of(p);
            }else if(infl == max){ // if there are two or more maximums
                maxP = Optional.empty();
            }
        }

        maxP.ifPresent(
                player -> {
                    if(player.getTowerColor() != curr.getTowerColor()) { //only if island is not yet conquered
                        curr.setIslandConquered(player.getTowerColor());
                        player.setTowersCount(player.getTowersCount() - curr.getTowersCount());
                    }
                }
        );
    }

    public void useCharacterCard(Player player, CharacterCard card){
        if(!players.contains(player))
            throw  new InvalidOperationException();

        if(!characterCards.contains(card))
            throw  new InvalidOperationException();

        //TODO check if it's the player turn

        if(!player.buyCharacterCard(card))
            throw new InvalidOperationException("Player cannot buy the card");

        //currentRound.useCharacterCard();
    }


    private int calculateInfluence(Island island, Player player) {
        //atomic because it is used in lambda
        AtomicInteger influence = new AtomicInteger();

        //towers
        if(island.isConquered())
            if(island.getTowerColor() == player.getTowerColor())
                influence.addAndGet(island.getTowersCount());

        //students
        this.professors.forEach((s, p) -> {
            if (p.equals(player)) //if the player has the professor associated
                influence.addAndGet(island.getStudents().getCountForStudent(s));
        });

        return influence.get();
    }

    private void checkAndMergeIslands() {
        Island curr, prev, next;

        curr = getCurrentIsland();
        prev = islands.get(calculateMotherNatureIndex(-1));
        next = islands.get(calculateMotherNatureIndex(1));

        if(curr.checkMergeCompatibility(prev)){
            curr.merge(prev);
            islands.remove(prev);
        }

        if(curr.checkMergeCompatibility(next)){
            curr.merge(next);
            islands.remove(next);
        }

        //adjust motherNatureIndex
        this.motherNaturePosition = islands.indexOf(curr);
    }

    public Island getCurrentIsland(){
        return islands.get(this.motherNaturePosition);
    }

    private int calculateMotherNatureIndex(int steps){
        return (this.motherNaturePosition + steps) % islands.size();
    }


    public void addOnReceivedMessageListener() {

    }

    /*public ArrayList<Student> getProfessorsForPlayer(Player player) {
        return
    }

    public void updateProfessors() {

    }*/

    public ArrayList<Player> getPlayers() {
        //return a copy
        return new ArrayList<>(players);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public List<Island> getIslands() {
        //return a copy
        return new ArrayList<>(islands);
    }

    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public ArrayList<CharacterCard> getCharacterCards() {
        //return a copy
        return new ArrayList<>(characterCards);
    }

    public Map<Student, Player> getProfessors() {
        //return a copy
        return new HashMap<>(professors);
    }

    public Status getGameStatus() {
        return gameStatus;
    }

    enum Status {
        CREATED,
        STARTED,
        PAUSED,
        FINISHED
    }
}
