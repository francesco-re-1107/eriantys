package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;

import java.util.*;

public class Round {

    /**
     * Current stage of the round
     */
    private Stage stage = Stage.PLAN;

    /**
     * List of players, always ordered by turn priority
     */
    private final List<Player> players;

    /**
     * List of played cards, it's bound 1 to 1 to the players list
     */
    private final List<AssistantCard> playedAssistantCards;

    /**
     * List of clouds for this game
     */
    private final List<StudentsContainer> clouds;

    /**
     * Index of the current player with respect to the players list
     */
    private int currentPlayer = 0;

    /**
     * Additional mother nature moves for the current turn TODO: maybe improve
     */
    private int additionalMotherNatureMoves = 0;

    /**
     * @param players list of players ordered with respect to the assistant cards played in the previous round
     * @param clouds randomly generated clouds
     */
    public Round(List<Player> players, List<StudentsContainer> clouds) {
        this.clouds = clouds;
        this.players = players;
        this.playedAssistantCards = new ArrayList<>();
    }

    /**
     * Go to the next stage (from PLAN to ATTACK), called internally
     */
    private void nextStage(){
        //TODO: improve this shitty code

        //sort players and cards together by turn priority
        List<CardPair> cardPairs = new ArrayList<>();

        for(int i = 0; i < players.size(); i++)
            cardPairs.add(new CardPair(players.get(i), playedAssistantCards.get(i)));

        cardPairs.sort(Comparator.comparingInt(cardPair -> cardPair.second.getTurnPriority()));

        players.clear();
        players.addAll(cardPairs.stream()
                .map(cardPair -> cardPair.first)
                .toList()
        );

        playedAssistantCards.clear();
        playedAssistantCards.addAll(cardPairs.stream()
                .map(cardPair -> cardPair.second)
                .toList()
        );

        stage = Stage.ATTACK;
        currentPlayer = 0;
    }

    /**
     * Play the given card for the given player
     * @param player
     * @param card
     */
    public void playAssistantCard(Player player, AssistantCard card){
        if(stage == Stage.ATTACK)
            throw new InvalidOperationException("In attack mode cannot play assistantCard");

        boolean alreadyPlayed =
                playedAssistantCards.stream().anyMatch(c -> c.equals(card));

        if(alreadyPlayed){
            //check if the player has other cards to play
            boolean canPlayOtherCards = player.getDeck()
                    .entrySet()
                    .stream()
                    .filter(e -> !e.getValue())
                    .map(Map.Entry::getKey)
                    .anyMatch(c -> !playedAssistantCards.contains(c));

            if (canPlayOtherCards)
                throw new InvalidOperationException("Card already played by another player");
        }

        playedAssistantCards.add(card);
        player.playAssistantCard(card);
        if (playedAssistantCards.size() == players.size()) //go to attack stage
            nextStage();
        else
            currentPlayer++;
    }

    /**
     * Go to next player in ATTACK mode
     * @return true if the round is finished, false otherwise
     */
    public boolean nextPlayer(){
        if(stage == Stage.PLAN)
            throw new InvalidOperationException();

        currentPlayer++;

        //finished round check
        return currentPlayer == players.size();
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
    public Optional<AssistantCard> getCardPlayedBy(Player player){
        int index = players.indexOf(player);

        if(index != -1 && index < playedAssistantCards.size())
            return Optional.of(playedAssistantCards.get(index));
        else
            return Optional.empty();
    }

    /**
     * @return the current player, both in PLAN and ATTACK mode
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
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
    public void removeCloud(StudentsContainer cloud) {
        clouds.remove(cloud);
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
     * This enum represents all the possible stage for this round
     */
    public enum Stage {
        PLAN,
        ATTACK
    }

    private record CardPair(Player first, AssistantCard second){ }
}

