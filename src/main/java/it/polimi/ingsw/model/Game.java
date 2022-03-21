package it.polimi.ingsw.model;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.exceptions.DuplicatedNicknameException;

import java.sql.Array;
import java.util.*;

public class Game {

    private final ArrayList<Player> players;

    private final int numberOfPlayers;

    private List<Island> islands;

    private final RandomizedStudentsContainer studentsBag;

    private int motherNaturePosition = 0;

    private ArrayList<Round> rounds;

    private final ArrayList<CharacterCard> characterCards;

    private Map<Student, Player> currentProfessors;

    private boolean isGameStarted = false;

    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.studentsBag = new RandomizedStudentsContainer(Constants.STUDENTS_BAG_NUMBER_PER_COLOR);
        this.characterCards = CharacterCard.generateRandomDeck(3);
        this.players = new ArrayList<>();

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
        if (players.size() != numberOfPlayers || isGameStarted)
            return;


        this.isGameStarted = true;
    }

    public void addPlayer(String nickname) throws DuplicatedNicknameException {
        if (players.size() >= numberOfPlayers || isGameStarted)
            return;

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
            int infl = p.calculateInfluence(curr);

            if(infl > max){
                max = infl;
                maxP = Optional.of(p);
            }else if(infl == max){ // if duplicate max
                maxP = Optional.empty();
            }
        }

        maxP.ifPresent(
                player -> {
                    curr.setIslandConquered(player.getTowerColor());
                    player.setTowersCount(player.getTowersCount() - curr.getTowersCount());
                }
        );
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

    public void getProfessorsForPlayer(Player player) {

    }

    public void updateProfessors() {

    }

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

    public ArrayList<Round> getRounds() {
        //return a copy
        return new ArrayList<>(rounds);
    }

    public ArrayList<CharacterCard> getCharacterCards() {
        //return a copy
        return new ArrayList<>(characterCards);
    }

    public Map<Student, Player> getCurrentProfessors() {
        //return a copy
        return new HashMap<>(currentProfessors);
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }
}
