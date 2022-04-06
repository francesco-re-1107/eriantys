package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.charactercards.KnightCharacterCard;
import it.polimi.ingsw.model.charactercards.MinstrelCharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player p1, p2;

    @BeforeEach
    void setUp() {
        //test constructor two players
        p1 = new Player("player1", Tower.BLACK, 8, new StudentsContainer());
        p2 = new Player("player2", Tower.WHITE, 8, new StudentsContainer());

        /*
        //test constructor three players
        Player _p1 = new Player("_player1", Tower.BLACK, 6, new StudentsContainer());
        */
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
        p1.addCloudToEntrance(
                new StudentsContainer()
                        .addStudents(Student.BLUE,3)
                        .addStudents(Student.GREEN,2)
                        .addStudents(Student.RED,1)
        );

        int entrancePrevSize = p1.getEntrance().getSize();
        int schoolPrevSize = p1.getSchool().getSize();

        p1.addStudentsToSchool(
                new StudentsContainer()
                        .addStudents(Student.BLUE,1)
                        .addStudents(Student.GREEN,2)
        );

        assertEquals(entrancePrevSize - 3, p1.getEntrance().getSize());
        assertEquals(schoolPrevSize + 3, p1.getSchool().getSize());
    }

    @Test
    void getAssistantCardsLeftCount() {
        assertEquals(10, p1.getAssistantCardsLeftCount());

        AssistantCard card = AssistantCard.getDefaultDeck().get(0);
        p1.playAssistantCard(card);

        assertEquals(9, p1.getAssistantCardsLeftCount());
    }

    @Test
    void useCoins() {
        p1.useCoins(1);
        assertEquals(0, p1.getCoins());

        assertThrows(
                InvalidOperationException.class,
                () -> p1.useCoins(1)
        );
    }
}