package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.charactercards.JesterCharacterCard;
import it.polimi.ingsw.model.charactercards.KnightCharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player p1, p2;

    @BeforeEach
    void setUp() {
        //test constructor two players
        p1 = new Player("player1", Tower.BLACK, 2);
        p2 = new Player("player2", Tower.WHITE, 2);

        //test constructor three players
        Player _p1 = new Player("_player1", Tower.BLACK, 3);
    }

    @Test
    void canAffordCharacterCard() {
        assertFalse(p1.canAffordCharacterCard(new KnightCharacterCard()));
        assertTrue(p1.canAffordCharacterCard(new JesterCharacterCard()));
    }

    @Test
    void buyCharacterCard() {
        p1.buyCharacterCard(new JesterCharacterCard());
        assertEquals(0, p1.getCoins());

        //should not buy
        assertFalse(p1.buyCharacterCard(new JesterCharacterCard()));
    }

    @Test
    void canPlayAssistantCard() {
        AssistantCard card = AssistantCard.getDefaultDeck().get(0);
        p1.playAssistantCard(card);

        assertFalse(p1.canPlayAssistantCard(card));
    }

    @Test
    void playAssistantCard() {
        AssistantCard card = AssistantCard.getDefaultDeck().get(0);
        p1.playAssistantCard(card);
        assertTrue(p1.getDeck().get(card));

        assertThrows(
                InvalidOperationException.class,
                () -> p1.playAssistantCard(card)
        );
    }

    @Test
    void getNickname() {
        assertEquals("player1", p1.getNickname());
    }

    @Test
    void setTowersCount() {
        p1.setTowersCount(2);
        assertEquals(2, p1.getTowersCount());
    }

    @Test
    void getTowerColor() {
        assertEquals(Tower.BLACK, p1.getTowerColor());
    }

    @Test
    void getCoins() {
        assertEquals(1, p1.getCoins());
    }

    @Test
    void addCloudToEntrance() {
        StudentsContainer prev = p1.getEntrance();

        StudentsContainer c = new StudentsContainer();
        c.addStudents(Student.RED, 2);
        c.addStudents(Student.BLUE, 1);

        p1.addCloudToEntrance(c);

        StudentsContainer curr = p1.getEntrance();

        assertEquals(prev.getSize() + 3, curr.getSize());
    }

    @Test
    void addStudentsToSchool() {
        //TODO: implement
    }

    @Test
    void getAssistantCardsLeftCount() {
        assertEquals(10, p1.getAssistantCardsLeftCount());

        AssistantCard card = AssistantCard.getDefaultDeck().get(0);
        p1.playAssistantCard(card);

        assertEquals(9, p1.getAssistantCardsLeftCount());
    }
}