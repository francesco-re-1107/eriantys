package it.polimi.ingsw.model;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.charactercards.*;
import it.polimi.ingsw.model.influencecalculators.DefaultInfluenceCalculator;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.logging.Logger;

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
     * Stores the 3 character cards selected for this game and the number of times they've been used
     */
    private final Map<String, Integer> characterCards = new HashMap<>();;

    /**
     * Stores for each student color which player has the professor, it is empty when the game is started
     */
    private final Map<Student, Player> professors;

    /**
     * Stores the current game state (@see Game.State)
     */
    private State gameState = State.CREATED;

    /**
     * InfluenceCalculator used by default when calculating influence of a player on an island
     */
    private final InfluenceCalculator defaultInfluenceCalculator = new DefaultInfluenceCalculator();

    /**
     * One shot InfluenceCalculator used when calculating influence of a player on an island
     * under a character card effect. It has higher priority than defaultInfluenceCalculator
     */
    private Optional<InfluenceCalculator> temporaryInfluenceCalculator = Optional.empty();

    /**
     * Used to retrieve the winner of this game, it is null until the game comes in the FINISHED state
     */
    private Player winner;

    /**
     * Whether the game is played with experto mode on
     */
    private final boolean expertMode;

    Logger logger = Logger.getLogger(Game.class.getName());

    /**
     * Create a new game
     * @param numberOfPlayers number of players chose at the creation of the game
     */
    public Game(int numberOfPlayers, boolean expertMode) {
        logger.info(MessageFormat.format("creating game with {0} players", numberOfPlayers));
        this.numberOfPlayers = numberOfPlayers;
        this.studentsBag = new RandomizedStudentsContainer(Constants.STUDENTS_BAG_NUMBER_PER_COLOR);
        this.players = new ArrayList<>();
        this.professors = new HashMap<>();
        this.expertMode = expertMode;

        CharacterCard.generateRandomDeck(Constants.NUMBER_OF_CHARACTER_CARD)
                .forEach(s -> this.characterCards.put(s, 0));

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

        logger.fine("initialized islands");
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
                    MessageFormat.format("Game already started (gameState is {0})", gameState)
            );

        logger.info("game starting...");
        newRound();

        this.gameState = State.STARTED;
    }

    /**
     * Create a new round
     * Generate automatically the clouds.
     */
    private void newRound() {
        logger.info("round started");
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
    public void selectCloud(Player player, StudentsContainer cloud) {
        if(!currentRound.getClouds().contains(cloud))
            throw new InvalidOperationException("Cannot find selected cloud");

        if(!players.contains(player))
            throw new InvalidOperationException("Player not found");

        player.addCloudToEntrance(cloud);
        currentRound.removeCloud(cloud);

        //go to next player or new round if necessary
        if(currentRound.nextPlayer())
            newRound();
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

        //check duplicate nickname
        if (players
                .stream()
                .anyMatch(p -> p.getNickname().equals(nickname)))
            throw new DuplicatedNicknameException();

        int entranceSize = numberOfPlayers == 2 ?
                Constants.TWO_PLAYERS.ENTRANCE_SIZE :
                Constants.THREE_PLAYERS.ENTRANCE_SIZE;

        int towersCount = numberOfPlayers == 2 ?
                Constants.TWO_PLAYERS.TOWERS_COUNT :
                Constants.THREE_PLAYERS.TOWERS_COUNT;

        Player p = new Player(
                nickname,
                Tower.values()[players.size()], //0->BLACK, 1->WHITE, 2->GREY
                towersCount,
                studentsBag.pickManyRandom(entranceSize)
        );

        logger.info(MessageFormat.format("added player {0}", p.getNickname()));
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

        if(steps > card.getMotherNatureMaxMoves() + currentRound.getAdditionalMotherNatureMoves())
            throw new InvalidOperationException("Cannot move mother nature that far");
        //reset additional moves after use
        currentRound.setAdditionalMotherNatureMoves(0);

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
     * Calculate which player has the most influence on the current island
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
        InfluenceCalculator calc = temporaryInfluenceCalculator.orElse(defaultInfluenceCalculator);

        for(Player p: players){
            int infl = calc.calculateInfluence(p, island, getProfessors());

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

        //remove temporary after use
        temporaryInfluenceCalculator = Optional.empty();
    }

    /**
     *
     * @param player
     * @param card
     */
    public void playAssistantCard(Player player, AssistantCard card){
        if(!players.contains(player))
            throw new InvalidOperationException();

        if(!player.canPlayAssistantCard(card))
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
        if(!expertMode)
            return;

        if(!players.contains(player))
            throw new InvalidOperationException();

        if(!characterCards.containsKey(card.getName()))
            throw new InvalidOperationException();

        int cost = card.getCost(characterCards.get(card.getName()));

        if(player.getCoins() < cost)
            throw new InvalidOperationException("Player cannot buy the card");

        player.useCoins(cost);

        if(card instanceof InfluenceCharacterCard){
            temporaryInfluenceCalculator = Optional.of(((InfluenceCharacterCard) card).getInfluenceCalculator());
        } else if (card instanceof HeraldCharacterCard){
            Island island = ((HeraldCharacterCard)card).getIsland();
            calculateInfluenceOnIsland(island);
        } else if (card instanceof PostmanCharacterCard) {
            currentRound.setAdditionalMotherNatureMoves(((PostmanCharacterCard)card).getAdditionalMotherNatureMoves());
        } else if (card instanceof GrandmaCharacterCard) {
            ((GrandmaCharacterCard) card).getIsland().setNoEntry(true);
        } else if (card instanceof MinstrelCharacterCard) {
            MinstrelCharacterCard c = ((MinstrelCharacterCard) card);

            if(c.getStudentsToRemove().getSize() > 2 || c.getStudentsToAdd().getSize() > 2)
                throw new InvalidOperationException("Too much students to swap");

            player.swapStudents(c.getStudentsToRemove(), c.getStudentsToAdd());
        }
    }

    /**
     * TODO: find a better name for this method
     * Move students of a player from entrance to school or islands
     *
     * @param player
     * @param inSchool students to add to school
     * @param inIsland students to add to the relative island
     */
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
        inIsland.forEach(player::addStudentsToIsland);

        updateProfessors();
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
            logger.info("island merged with previous one");
            curr.merge(prev);
            islands.remove(prev);
        }

        if(curr.isMergeCompatible(next)){
            logger.info("island merged with next one");
            curr.merge(next);
            islands.remove(next);
        }

        if(islands.size() <= 3)
            setGameFinished(calculateCurrentWinner());

        //adjust motherNatureIndex
        this.motherNaturePosition = islands.indexOf(curr);
    }

    /**
     * TODO: fix if 3 players have the same towers and the last one has the most professors
     * Calculate player that is winning right now, based on the number of towers left.
     * If there's a draw, the player with most professors wins
     * If there's a draw, TODO: the instructions don't handle this case
     * @return the winning player
     */
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

    /**
     * Calculate current professors
     * If two players have the same number of students nothing changes for that student
     */
    private void updateProfessors() {
        Arrays.stream(Student.values()).forEach(s -> {
            List<Player> sortedPlayers = players.stream()
                    .sorted(
                            Comparator.comparingInt(p -> ((Player) p).getSchool().getCountForStudent(s))
                                    .reversed()
                    )
                    .collect(Collectors.toList());

            Player firstPlayer = sortedPlayers.get(0);
            Player secondPlayer = sortedPlayers.get(1);

            if(firstPlayer.getSchool().getCountForStudent(s) >
                    secondPlayer.getSchool().getCountForStudent(s))
                professors.put(s, firstPlayer);
        });
    }

    /**
     * Utility method to retrieve the professors of a given player
     * @param player
     * @return a list of student for which the player has the professor
     */
    public List<Student> getProfessorsForPlayer(Player player) {
        return professors.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(player))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * End this game and set a winner
     * @param winner the winner of the game
     */
    private void setGameFinished(Player winner) {
        logger.info(MessageFormat.format("game finished! {1} won", winner.getNickname()));
        gameState = State.FINISHED;

        this.winner = winner;
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
    public Map<String, Integer> getCharacterCards() {
        //return a copy
        return new HashMap<>(characterCards);
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

    //TODO maybe these will be substituted with setClientOffline(), setClientOnline(), setClientQuit()...
    public void pauseGame(){
        this.gameState = State.PAUSED;
    }

    public void resumeGame(){
        this.gameState = State.STARTED;
    }

    /**
     * Return
     * @return the winner of the game, if the game was ended
     */
    public Optional<Player> getWinner() {
        return Optional.of(winner);
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
