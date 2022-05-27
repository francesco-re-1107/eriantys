package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    Round r;
    List<StudentsContainer> clouds;
    List<Player> players;

    @BeforeEach
    void setUp() {
        players = new LinkedList<>(Arrays.asList(
                new Player("p1", Tower.BLACK, 8, new StudentsContainer()),
                new Player("p2", Tower.WHITE, 8, new StudentsContainer())
        ));

        StudentsContainer c1, c2, c3;

        c1 = new StudentsContainer();
        c1.addStudents(Student.BLUE, 2);
        c1.addStudents(Student.YELLOW, 1);

        c2 = new StudentsContainer();
        c2.addStudents(Student.RED, 2);
        c2.addStudents(Student.YELLOW, 4);

        c3 = new StudentsContainer();
        c3.addStudents(Student.PINK, 3);
        c3.addStudents(Student.RED, 1);

        clouds = new LinkedList<>(Arrays.asList(c1, c2, c3));
        r = new Round(players, clouds);
    }

    @Test
    void playAssistantCard() {
        //play first card
        r.playAssistantCard(players.get(0), AssistantCard.getDefaultDeck().get(0));

        assertEquals(players.get(1), r.getCurrentPlayer());

        //player that already played its card
        assertThrows(
                InvalidOperationError.class,
                () -> r.playAssistantCard(players.get(0), AssistantCard.getDefaultDeck().get(0))
        );

        //card already played by another player
        assertThrows(
                InvalidOperationError.class,
                () -> r.playAssistantCard(players.get(1), AssistantCard.getDefaultDeck().get(0))
        );

        r.playAssistantCard(players.get(1), AssistantCard.getDefaultDeck().get(1));

        //check if the stage has changed
        assertEquals(Stage.Attack.STARTED, r.getStage());

        //playing card in attack stage
        assertThrows(
                InvalidOperationError.class,
                () -> r.playAssistantCard(players.get(0), AssistantCard.getDefaultDeck().get(0))
        );
    }

    @Test
    void nextPlayer() {
        //next player in PLAN stage
        assertThrows(
                InvalidOperationError.class,
                () -> r.nextPlayer()
        );

        r.playAssistantCard(players.get(0), AssistantCard.getDefaultDeck().get(0));
        r.playAssistantCard(players.get(1), AssistantCard.getDefaultDeck().get(1));

        assertFalse(r.nextPlayer());

        //finished round
        assertTrue(r.nextPlayer());
    }

    @Test
    void getStage() {
        assertEquals(Stage.Plan.PLAN, r.getStage());
    }

    @Test
    void getPlayers() {
        assertEquals(r.getPlayers(), players);
    }

    @Test
    void getCardPlayedBy() {
        assertFalse(r.getCardPlayedBy(players.get(0)).isPresent());

        r.playAssistantCard(players.get(0), AssistantCard.getDefaultDeck().get(0));

        assertTrue(r.getCardPlayedBy(players.get(0)).isPresent());
    }

    @Test
    void getCurrentPlayer() {
        assertEquals(r.getCurrentPlayer(), players.get(0));
    }

    @Test
    void getClouds() {
        assertEquals(r.getClouds(), clouds);
    }

    @Test
    void setAdditionalMotherNatureMoves(){
        r.setAdditionalMotherNatureMoves(3);
        assertEquals(3, r.getAdditionalMotherNatureMoves());
    }

    @Test
    void removeCloud() {
        r.removeCloud(clouds.get(0));

        assertEquals(2, r.getClouds().size());
    }
}