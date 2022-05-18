package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;
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
    void incrementTowersCount() {
        int prev = p1.getTowersCount();
        p1.incrementTowersCount(2);
        assertEquals(prev + 2, p1.getTowersCount());
    }

    @Test
    void decrementTowersCount() {
        int prev = p1.getTowersCount();
        p1.decrementTowersCount(2);
        assertEquals(prev - 2, p1.getTowersCount());
        p1.decrementTowersCount(100);
        assertEquals(0, p1.getTowersCount());
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

    @Test
    void swapStudents() {
        p1.addCloudToEntrance(
                new StudentsContainer()
                        .addStudents(Student.BLUE,8)
                        .addStudents(Student.GREEN,8)
                        .addStudents(Student.RED,8)
        );

        p1.addStudentsToSchool(
                new StudentsContainer()
                        .addStudents(Student.BLUE,4)
                        .addStudents(Student.GREEN,4)
                        .addStudents(Student.RED,4)
        );

        int schoolPrevSize = p1.getSchool().getSize();
        int entrancePrevSize = p1.getEntrance().getSize();

        //different size containers
        assertThrows(
                InvalidOperationException.class,
                () -> p1.swapStudents(
                        new StudentsContainer(),
                        new StudentsContainer().addStudent(Student.BLUE)
                )
        );

        p1.swapStudents(
                new StudentsContainer()
                        .addStudents(Student.BLUE,1)
                        .addStudents(Student.GREEN,1),
                new StudentsContainer()
                        .addStudents(Student.RED,2)
        );

        int redsInSchool = p1.getSchool().getCountForStudent(Student.RED);
        int redsInEntrance = p1.getEntrance().getCountForStudent(Student.RED);
        assertEquals(6, redsInSchool);
        assertEquals(2, redsInEntrance);

        int bluesInSchool = p1.getSchool().getCountForStudent(Student.BLUE);
        int bluesInEntrance = p1.getEntrance().getCountForStudent(Student.BLUE);
        assertEquals(3, bluesInSchool);
        assertEquals(5, bluesInEntrance);

        int greensInSchool = p1.getSchool().getCountForStudent(Student.GREEN);
        int greensInEntrance = p1.getEntrance().getCountForStudent(Student.GREEN);
        assertEquals(3, greensInSchool);
        assertEquals(5, greensInEntrance);

        assertEquals(schoolPrevSize, p1.getSchool().getSize());
        assertEquals(entrancePrevSize, p1.getEntrance().getSize());
    }
}