package it.polimi.ingsw.model;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.exceptions.InvalidOperationException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This class represents a game. When a new game is created it is in the State.CREATED state.
 * While in this state, players can be added through the method addPlayer(...).
 * When the desired number of players (@see numberOfPlayers in the constructor) is reached the game can be started
 * with the method startGame(). At this point the game state is State.STARTED.
 */
public class Game {

    /**
     * Stores the players of this game
     */
    private final ArrayList<Player> players;

    /**
     * Desired number of players of this game, decided when game is created
     */
    private final int numberOfPlayers;

    /**
     * Stores all the islands of this game.
     * The last one is "connected" with the first one in a circular path.
     */
    private List<Island> islands;

    /**
     * The students bag used to pick random students throughout the game
     */
    private final RandomizedStudentsContainer studentsBag;

    /**
     * Stores the current mother nature position (index relative to the islands list)
     */
    private int motherNaturePosition = 0;

    /**
     * Stores the current round that is being played
     */
    private Round currentRound;

    /**
     * Stores the 3 character cards selected for this game
     */
    private final ArrayList<CharacterCard> characterCards;

    /**
     * Stores for each student color which player has the professor, it is empty when the game is started
     */
    private final Map<Student, Player> professors;

    /**
     * Stores the current game state (@see Game.State)
     */
    private State gameState = State.CREATED;

    private Optional<Player> winner = Optional.empty();

    /**
     * Create a new game
     * @param numberOfPlayers number of players chose at the creation of the game
     */
    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.studentsBag = new RandomizedStudentsContainer(Constants.STUDENTS_BAG_NUMBER_PER_COLOR);
        this.characterCards = CharacterCard.generateRandomDeck(3);
        this.players = new ArrayList<>();
        this.professors = new HashMap<>();

        initializeIslands();
    }

    /**
     * Create the islands and add them in the islands list (used internally)
     */
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

    /**
     * Start this game
     * Only if the numberOfPlayers is reached and if the Game is in state CREATED.
     */
    public void startGame() {
        if (players.size() != numberOfPlayers)
            throw new InvalidOperationException("Number of players not reached yet");

        if(gameState != State.CREATED)
            throw new InvalidOperationException(
                    MessageFormat.format("Game already started (gameStatus is {0})", gameState)
            );

        newRound();

        this.gameState = State.STARTED;
    }

    /**
     * Create a new round
     * Generate automatically the clouds.
     */
    private void newRound() {
        //if the bag is empty the game is finished
        if(studentsBag.getSize() == 0)
            setGameFinished(calculateCurrentWinner());

        //check if players have any card left (checking only player0 because they're all the same)
        if(players.get(0).getAssistantCardsLeftCount() == 0)
            setGameFinished(calculateCurrentWinner());

        //generate clouds
        List<StudentsContainer> clouds = new ArrayList<>();
        for(int i = 0; i < numberOfPlayers; i++)
            clouds.add(studentsBag.pickManyRandom(
                    numberOfPlayers == 2 ?
                            Constants.TWO_PLAYERS.STUDENTS_PER_CLOUD :
                            Constants.THREE_PLAYERS.STUDENTS_PER_CLOUD)
            );

        List<Player> tmpPlayers;

        //if it's not the first round, use the previous players order for the new round
        if(currentRound != null)
            tmpPlayers = currentRound.getPlayers();
        else
            tmpPlayers = new ArrayList<>(players);

        currentRound = new Round(tmpPlayers, clouds);
    }

    /**
     * Select a cloud for a given player
     * @param cloud the cloud chose by the player
     * @param player the player
     */
    public void selectCloud(StudentsContainer cloud, Player player) {
        if(!currentRound.getClouds().contains(cloud))
            throw new InvalidOperationException("Cannot find selected cloud");

        if(!players.contains(player))
            throw new InvalidOperationException("Player not found");

        player.addCloudToEntrance(cloud);
        currentRound.removeCloud(cloud);
    }

    /**
     * Add a new player to this game lobby, only if the game is in CREATED state and there's space for another user
     * @param nickname the nickname chose by the player
     * @return the newly created player
     * @throws DuplicatedNicknameException if the nickname is already used by another player
     */
    public Player addPlayer(String nickname) throws DuplicatedNicknameException {
        if (players.size() >= numberOfPlayers)
            throw new InvalidOperationException("Players lobby is already full");

        if (gameState != State.CREATED)
            throw new InvalidOperationException(
                    MessageFormat.format("Game already started (gameStatus is {0})", gameState)
            );

        //check duplicate nickname
        if (players
                .stream()
                .anyMatch(p -> p.getNickname().equals(nickname)))
            throw new DuplicatedNicknameException();

        Player p = new Player(
                nickname,
                Tower.values()[players.size()], //0->BLACK, 1->WHITE, 2->GREY
                numberOfPlayers
        );

        players.add(p);
        return p;
    }

    /**
     * Move mother nature on the islands
     * @param steps number of steps that mother nature needs to be moved
     */
    public void moveMotherNature(Player player, int steps){
        if(!currentRound.getCurrentPlayer().equals(player))
            throw new InvalidOperationException("");

        if(currentRound.getStage() != Round.Stage.ATTACK)
            throw new InvalidOperationException("");

        //use get directly cause in attack stage every player has played its card
        AssistantCard card = currentRound.getCardPlayedBy(player).get();

        if(steps > card.getMotherNatureMaxMoves())
            throw new InvalidOperationException("Cannot move mother nature that far");

        this.motherNaturePosition = calculateMotherNatureIndex(steps);

        // if there's a no entry on the island then remove it and don't calculate influence
        if(getCurrentIsland().isNoEntry()) {
            getCurrentIsland().setNoEntry(false);
        } else {
            //after moving mother nature calculate influence on the island she just reached
            this.calculateInfluenceOnCurrentIsland();

            //After influence calculation a player may have conquered a new island.
            //It's necessary to check if islands could be merged
            this.checkAndMergeIslands();
        }
    }

    /**
     * Calculate which player has the most influence on the givcurrenten island
     * and change the towers on that island respectively
     */
    private void calculateInfluenceOnCurrentIsland(){
        this.calculateInfluenceOnIsland(getCurrentIsland());
    }

    /**
     * Calculate which player has the most influence on the given island
     * and change the towers on that island respectively
     */
    public void calculateInfluenceOnIsland(Island island){
        int max = -1;
        Optional<Player> maxP = Optional.empty();

        for(Player p: players){
            int infl = calculateInfluenceOfPlayer(island, p);

            if(infl > max){
                max = infl;
                maxP = Optional.of(p);
            }else if(infl == max){ // if there are two or more maximums
                maxP = Optional.empty();
            }
        }

        //only if there's a max without duplicate
        maxP.ifPresent(
                player -> {
                    //only if island is not yet conquered by this player
                    if(player.getTowerColor() != island.getTowerColor()) {
                        island.setConquered(player.getTowerColor());
                        player.setTowersCount(player.getTowersCount() - island.getTowersCount());

                        //TODO maybe use an observer
                        if(player.getTowersCount() <= 0)
                            setGameFinished(player);
                    }
                }
        );
    }

    /**
     *
     * @param player
     * @param card
     */
    public void playAssistantCard(Player player, AssistantCard card){
        if(!players.contains(player))
            throw new InvalidOperationException();

        if(player.canPlayAssistantCard(card))
            throw new InvalidOperationException();

        player.playAssistantCard(card);
        currentRound.playAssistantCard(player, card);
    }

    /**
     *
     * @param player
     * @param card
     */
    public void playCharacterCard(Player player, CharacterCard card){
        if(!players.contains(player))
            throw  new InvalidOperationException();

        if(!characterCards.contains(card))
            throw  new InvalidOperationException();

        if(!player.buyCharacterCard(card))
            throw new InvalidOperationException("Player cannot buy the card");

        //currentRound.useCharacterCard();
    }

    public void putStudents(Player player, StudentsContainer inSchool, Map<Island,StudentsContainer> inIsland){
        if(!currentRound.getCurrentPlayer().equals(player))
            throw new InvalidOperationException();

        if(currentRound.getStage() != Round.Stage.ATTACK)
            throw new InvalidOperationException();

        int studentsMoved = inSchool.getSize() + inIsland.values().stream().mapToInt(StudentsContainer::getSize).sum();

        int studentsToMove = numberOfPlayers == 3 ?
                Constants.THREE_PLAYERS.STUDENTS_TO_MOVE :
                Constants.TWO_PLAYERS.STUDENTS_TO_MOVE;

        if(studentsMoved != studentsToMove)
            throw new InvalidOperationException();

        player.addStudentsToSchool(inSchool);
        inIsland.forEach(Island::addStudents);
    }

    /**
     * Calculate the influence of a player on a specific island
     * @param island
     * @param player
     * @return calculate influence as int >= 0
     */
    private int calculateInfluenceOfPlayer(Island island, Player player) {
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

    /**
     * Check if island could be merged.
     * It is called every time mother nature is moved so only the current island is checked
     */
    private void checkAndMergeIslands() {
        Island curr, prev, next;

        curr = getCurrentIsland();
        prev = islands.get(calculateMotherNatureIndex(-1));
        next = islands.get(calculateMotherNatureIndex(1));

        if(curr.isMergeCompatible(prev)){
            curr.merge(prev);
            islands.remove(prev);
        }

        if(curr.isMergeCompatible(next)){
            curr.merge(next);
            islands.remove(next);
        }

        if(islands.size() <= 3)
            setGameFinished(calculateCurrentWinner());

        //adjust motherNatureIndex
        this.motherNaturePosition = islands.indexOf(curr);
    }

    private Player calculateCurrentWinner() {
        //order players by placed towers
        List<Player> orderedPlayers = players.stream()
                .sorted(Comparator.comparingInt(Player::getTowersCount).reversed())
                .collect(Collectors.toList());

        Player firstPlayer =
                orderedPlayers.get(0);

        Player secondPlayer =
                orderedPlayers.get(1);

        if(firstPlayer.getTowersCount() < secondPlayer.getTowersCount()) //there's a winner
            return firstPlayer;

        //otherwise, look at the professors
        if(getProfessorsForPlayer(firstPlayer).size() >
                getProfessorsForPlayer(secondPlayer).size())
            return firstPlayer;
        else
            return secondPlayer;
    }

    /**
     * @return the island on which mother nature is located
     */
    public Island getCurrentIsland(){
        return islands.get(this.motherNaturePosition);
    }

    /**
     * Calculate the index of mother nature on the islands in a circular path
     * @param steps number of steps to move mother nature (positive or negative)
     * @return the calculated index
     */
    private int calculateMotherNatureIndex(int steps){
        return (this.motherNaturePosition + steps) % islands.size();
    }


    public void addGameUpdateListener() {
        //TODO implement
    }

    private void notifyUpdate(){
        //TODO implement
    }

    public void updateProfessors() {
        Arrays.stream(Student.values()).forEach(s -> {
            List<Player> sortedPlayers = players.stream()
                    .sorted(
                            Comparator.comparingInt(p -> p.getSchool().getCountForStudent(s))
                    )
                    .collect(Collectors.toList());

            Player firstPlayer = sortedPlayers.get(0);
            Player secondPlayer = sortedPlayers.get(0);

            if(firstPlayer.getSchool().getCountForStudent(s) >
                    secondPlayer.getSchool().getCountForStudent(s))
                professors.put(s, firstPlayer);
        });
    }

    public List<Student> getProfessorsForPlayer(Player player) {
        return professors.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(player))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void setGameFinished(Player winner) {
        gameState = State.FINISHED;

        this.winner = Optional.of(winner);
    }

    /**
     * @return a copy of the players list of this game
     */
    public List<Player> getPlayers() {
        //return a copy
        return new ArrayList<>(players);
    }

    /**
     * If game is started, this will return the same as getCurrentNumberOfPlayers()
     * @return the desired number of players
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * If game is started, this will return the same as getNumberOfPlayers()
     * @return the current number of players
     */
    public int getCurrentNumberOfPlayers() {
        return players.size();
    }

    /**
     * @return a copy of the islands list
     */
    public List<Island> getIslands() {
        //return a copy
        return new ArrayList<>(islands);
    }

    /**
     * @return current mother nature position (index relative to the islands list)
     */
    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }

    /**
     * @return the current round
     */
    public Round getCurrentRound() {
        return currentRound;
    }

    /**
     * @return a copy of the character cards selected for this game
     */
    public List<CharacterCard> getCharacterCards() {
        //return a copy
        return new ArrayList<>(characterCards);
    }

    /**
     * @return a copy of the current professors
     */
    public Map<Student, Player> getProfessors() {
        //return a copy
        return new HashMap<>(professors);
    }

    /**
     * @return current game state (@see Game.State)
     */
    public State getGameState() {
        return gameState;
    }

    public void pauseGame(){
        this.gameState = State.PAUSED;
    }

    public void restartGame(){
        this.gameState = State.STARTED;
    }

    public Optional<Player> getWinner() {
        return winner;
    }

    /**
     * this enum represents all the possible states of a game
     */
    enum State {
        CREATED, //game was created but never started
        STARTED, //game is currently played
        PAUSED, //when a client disconnects, the game is paused
        FINISHED, //game finished, there's a winner
        TERMINATED //game terminated before finish (e.g. a player left the game)
    }
}
