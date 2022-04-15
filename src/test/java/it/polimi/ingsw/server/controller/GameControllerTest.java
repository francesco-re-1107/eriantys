package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        gc1.setOnGameUpdateListener(new GameController.GameUpdateListener() {
            @Override
            public void onGameUpdate(ReducedGame game) {
                called.set(true);
            }
        });

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
        gc1.playAssistantCard(AssistantCard.getDefaultDeck().get(0));
        gc2.playAssistantCard(AssistantCard.getDefaultDeck().get(1));

        /*
        gc1.placeStudents();
        gc1.moveMotherNature();
        gc1.selectCloud();

        assertEquals(player1.);
        */
    }
}