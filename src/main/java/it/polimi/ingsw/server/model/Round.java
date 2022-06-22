package it.polimi.ingsw.server.model;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.exceptions.InvalidOperationError;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Round implements Serializable {

    @Serial
    private static final long serialVersionUID = 4748110032539192683L;

    /**
     * Current stage of the round
     */
    private Stage stage = Stage.Plan.PLAN;

    /**
     * List of players, always ordered by turn priority
     */
    private List<Player> players;

    /**
     * List of played cards
     */
    private final HashMap<Player, AssistantCard> playedAssistantCards;

    /**
     * This list contains all players that didn't select a cloud
     */
    private List<Player> playersWithoutCloud;

    /**
     * List of clouds for this game
     */
    private final List<StudentsContainer> clouds;

    /**
     * Current player
     */
    private Player currentPlayer;

    /**
     * Additional mother nature moves for the current turn
     */
    private int additionalMotherNatureMoves = 0;

    /**
     * @param players list of players ordered with respect to the assistant cards played in the previous round
     * @param clouds randomly generated clouds
     */
    public Round(List<Player> players, List<StudentsContainer> clouds) {
        this.clouds = clouds;
        this.players = players;
        this.playedAssistantCards = new HashMap<>();
        this.playersWithoutCloud = new ArrayList<>(players);

        //if the first player is offline go to the second one
        currentPlayer = players.get(0).isConnected() ? players.get(0) : players.get(1);
    }

    /**
     * Go to the next stage (from PLAN to ATTACK), called internally
     */
    private void nextStage(){
        //ordered players by turn priority, if a player hasn't played any card it has the lowest priority
        players = players.stream()
                .sorted(Comparator.comparingInt(p -> getCardPlayedBy(p) == null ? 100 : getCardPlayedBy(p).turnPriority()))
                .toList();

        playersWithoutCloud = new ArrayList<>(players);

        stage = Stage.Attack.STARTED;

        //if the first player is offline go to the second one
        currentPlayer = players.get(0).isConnected() ? players.get(0) : players.get(1);
    }

    /**
     * Set the new attack substage (e.g. MOTHER_NATURE_MOVED -> SELECTED_CLOUD)
     * @param newStage the new stage
     */
    public void setAttackSubstage(Stage.Attack newStage){
        if(Stage.isEqualOrPost(stage, newStage))
            throw new InvalidOperationError("newStage must be post to current stage");
        stage = newStage;
    }

    /**
     * Play the given card for the given player
     * @param player the player who plays the card
     * @param card the card to play
     */
    public void playAssistantCard(Player player, AssistantCard card){
        if(stage instanceof Stage.Attack)
            throw new InvalidOperationError("In attack mode cannot play assistantCard");

        if(playedAssistantCards.get(player) != null)
            throw new InvalidOperationError("This player already played his card");

        if(playedAssistantCards.containsValue(card)){

            //check if the player has other cards to play
            var canPlayOtherCards = player.getDeck().entrySet()
                    .stream()
                    .anyMatch(e -> !e.getValue() && !playedAssistantCards.containsValue(e.getKey()));

            if (canPlayOtherCards)
                throw new InvalidOperationError("Card already played by another player");
        }

        player.playAssistantCard(card);
        playedAssistantCards.put(player, card);
        nextPlayer();
    }

    /**
     * Go to next player in ATTACK mode
     * @return true if the round is finished, false otherwise
     */
    public boolean nextPlayer(){
        Utils.LOGGER.info("Next player called, " + stage.getClass().getSimpleName());
        if(stage instanceof Stage.Plan){ //PLAN mode
            var nextActivePlayer = getNextActivePlayer();

            if (nextActivePlayer == null) //go to attack stage
                nextStage();
            else
                currentPlayer = nextActivePlayer;

        } else { //ATTACK mode

            var nextActivePlayer = getNextAttackActivePlayer();

            Utils.LOGGER.info("Next attack active player is " + nextActivePlayer);

            // finished round check
            if (nextActivePlayer == null){

                //check if any cloud is left, this means that one or more offline players didn't choose it
                if(!clouds.isEmpty())
                    playersWithoutCloud.forEach(p -> p.addCloudToEntrance(clouds.remove(0)));

                return true;
            }

            currentPlayer = nextActivePlayer;

            // more players to come
            stage = Stage.Attack.STARTED;
        }
        return false;
    }

    /**
     * Get the next active player which has played its card in plan stage
     * @return the player or null
     */
    private Player getNextAttackActivePlayer() {
        return players.stream()
                .filter(p -> players.indexOf(p) > players.indexOf(currentPlayer))
                .filter(Player::isConnected)
                .filter(p -> getCardPlayedBy(p) != null)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get next active player or null if it does not exist
     * @return the next player
     */
    private Player getNextActivePlayer() {
        return players.stream()
                .filter(p -> players.indexOf(p) > players.indexOf(currentPlayer))
                .filter(Player::isConnected)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * @return the curent stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @return the players list
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Get the assistant card played by the given player
     * @param player
     * @return the assistant card of the given player, returns empty if the player hasn't played yet
     */
    public AssistantCard getCardPlayedBy(Player player){
        return playedAssistantCards.get(player);
    }

    /**
     * @return the current player, both in PLAN and ATTACK mode
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return the list of clouds (remaining clouds) of this round
     */
    public List<StudentsContainer> getClouds() {
        return new ArrayList<>(clouds);
    }

    /**
     * Remove a cloud from the cloud list
     * @param cloud the cloud to remove
     */
    public void selectCloud(Player player, StudentsContainer cloud) {
        clouds.remove(cloud);
        playersWithoutCloud.remove(player);
    }

    /**
     * Set the additional mother nature moves for this turn
     * @param additionalMoves
     */
    public void setAdditionalMotherNatureMoves(int additionalMoves) {
        this.additionalMotherNatureMoves = additionalMoves;
    }

    /**
     * @return the additional mother nature moves for this turn
     */
    public int getAdditionalMotherNatureMoves() {
        return additionalMotherNatureMoves;
    }

    /**
     * @return a copy of the players list of this game
     */
    public List<Player> getActivePlayers() {
        // return a copy
        return players.stream().filter(Player::isConnected).toList();
    }

    /**
     * If a player disconnects and the game continues check that the round of that player finishes
     * @param player
     * @return true if a new round needs to be created, false otherwise
     */
    public boolean setPlayerDisconnected(Player player) {
        if(!currentPlayer.equals(player)) return false;

        return nextPlayer();
    }

    public Map<Player, AssistantCard> getPlayedAssistantCards() {
        return playedAssistantCards;
    }
}

