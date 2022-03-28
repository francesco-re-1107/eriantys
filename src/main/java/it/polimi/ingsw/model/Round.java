package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidOperationException;

import java.util.*;

public class Round {

    private Stage stage = Stage.PLAN;

    //ordered
    private final List<Player> players;

    private final List<AssistantCard> playedAssistantCards;

    private final List<StudentsContainer> clouds;

    private int currentPlayer = 0;

    private int additionalMotherNatureMoves = 0;

    /**
     *
     * @param players list of players ordered with respect to the assistant cards played in the previous round
     * @param clouds randomly generated clouds
     */
    public Round(List<Player> players, List<StudentsContainer> clouds) {
        this.clouds = clouds;
        this.players =players;
        this.playedAssistantCards = new ArrayList<>();
    }

    /**
     *
     */
    public void nextStage(){
        // only if all players have played cards
        if(playedAssistantCards.size() != players.size())
            throw new InvalidOperationException("Cannot go in ATTACK stage, not all players have played cards");

        if (stage == Stage.ATTACK)
            throw new InvalidOperationException("Already in ATTACK stage");

        //TODO: sort players and cards accordingly by assistant card played

        stage = Stage.ATTACK;
        currentPlayer = 0;
    }

    public void playAssistantCard(Player player, AssistantCard card){
        if(stage == Stage.ATTACK)
            throw new InvalidOperationException("In attack mode cannot play assistantCard");

        if (!player.equals(getCurrentPlayer()))
            throw new InvalidOperationException("This player cannot play card at this time");

        boolean alreadyPlayed =
                playedAssistantCards.stream().anyMatch((c) -> c.equals(card));

        if (alreadyPlayed)
            throw new InvalidOperationException("Card already played by another player");

        playedAssistantCards.add(card);
        if (playedAssistantCards.size() == players.size()) //go to attack stage
            nextStage();
        else
            currentPlayer++;
    }

    /**
     * Go to next player in attack mode
     * @return true if the round is finished, false otherwise
     */
    public boolean nextPlayer(){
        if(stage == Stage.PLAN)
            throw new InvalidOperationException();

        currentPlayer++;

        //finished round check
        return currentPlayer == players.size();
    }

    public Stage getStage() {
        return stage;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Optional<AssistantCard> getCardPlayedBy(Player player){
        int index = players.indexOf(player);

        if(index != -1 && index < playedAssistantCards.size())
            return Optional.of(playedAssistantCards.get(index));
        else
            return Optional.empty();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }


    public List<StudentsContainer> getClouds() {
        return new ArrayList<>(clouds);
    }

    public void removeCloud(StudentsContainer cloud) {
        clouds.remove(cloud);
    }

    public void setAdditionalMotherNatureMoves(int additionalMoves) {
        this.additionalMotherNatureMoves = additionalMoves;
    }

    enum Stage {
        PLAN,
        ATTACK
    }
}

