package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    GameController gc1, gc2;
    Game game;
    Player player1, player2;

    @BeforeEach
    void setUp() {
        game = new Game(2, false);

        player1 = game.addPlayer("p1");
        player2 = game.addPlayer("p2");

        game.startGame();

        gc1 = new GameController(game, player1);
        gc2 = new GameController(game, player2);
    }

    @Test
    void setOnGameUpdateListener() {
        AtomicBoolean called = new AtomicBoolean(false);

        gc1.setOnGameUpdateListener(game -> called.set(true));

        gc1.playAssistantCard(AssistantCard.getDefaultDeck().get(0));

        assertTrue(called.get());
    }

    @Test
    void disconnect() {
        gc1.disconnect();
        assertEquals(Game.State.PAUSED, game.getGameState());
        assertFalse(player1.isConnected());
    }

    @Test
    void leaveGame() {
        gc1.leaveGame();
        assertEquals(Game.State.TERMINATED, game.getGameState());
    }

    @Test
    void testSimpleGame() {
        //play assistant card
        gc1.playAssistantCard(AssistantCard.getDefaultDeck().get(0));
        gc2.playAssistantCard(AssistantCard.getDefaultDeck().get(1));

        //place students
        var bag = new RandomizedStudentsContainer(player1.getEntrance());
        var inIsland = new HashMap<Integer, StudentsContainer>();
        inIsland.put(2, bag.pickManyRandom(3));
        gc1.placeStudents(new StudentsContainer(), inIsland);
        assertEquals(4, game.getIslands().get(2).getStudents().getSize());

        //play character card
        //...

        //move mother nature
        int prevPos = game.getMotherNaturePosition();
        gc1.moveMotherNature(1);
        assertEquals(prevPos + 1, game.getMotherNaturePosition());

        //select cloud
        gc1.selectCloud(game.getCurrentRound().getClouds().get(0));
        assertEquals(1, game.getCurrentRound().getClouds().size());
    }
}