package it.polimi.ingsw.model;

import it.polimi.ingsw.model.influencecalculators.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InfluenceCalculatorsTest {

    Player p1, p2;
    Island i1;
    Map<Student, Player> professors;

    @BeforeEach
    void setUp() {
        StudentsContainer entrance = new StudentsContainer()
                .addStudents(Student.BLUE, 10)
                .addStudents(Student.RED, 10)
                .addStudents(Student.GREEN, 10)
                .addStudents(Student.YELLOW, 10);

        p1 = new Player("p1", Tower.BLACK, 8, entrance);
        p1.addStudentsToSchool(
                new StudentsContainer()
                        .addStudents(Student.BLUE, 2)
                        .addStudents(Student.RED, 4)
                        .addStudents(Student.YELLOW, 2)
        );

        p2 = new Player("p2", Tower.WHITE, 8, new StudentsContainer(entrance));
        p2.addStudentsToSchool(
                new StudentsContainer()
                        .addStudents(Student.BLUE, 4)
                        .addStudents(Student.RED, 3)
                        .addStudents(Student.GREEN, 5)
                        .addStudents(Student.YELLOW, 2)
        );

        //create an island with size of 2 and conquered black with 3 blue, 1 red, 2 yellow students
        i1 = new Island();
        i1.setConquered(Tower.BLACK);
        Island anotherIsland = new Island();
        anotherIsland.setConquered(Tower.BLACK);
        i1.merge(anotherIsland);
        i1.addStudents(
                new StudentsContainer()
                        .addStudents(Student.BLUE, 3)
                        .addStudents(Student.RED, 1)
                        .addStudents(Student.YELLOW, 2)
        );

        professors = new HashMap<>();
        professors.put(Student.RED, p1);
        professors.put(Student.BLUE, p2);
        professors.put(Student.GREEN, p2);
        professors.put(Student.YELLOW, p2);
    }

    @Test
    void defaultInfluenceCalculator() {
        InfluenceCalculator calculator = new DefaultInfluenceCalculator();

        int influence = calculator.calculateInfluence(p1, i1, professors);
        assertEquals(3, influence);

        influence = calculator.calculateInfluence(p2, i1, professors);
        assertEquals(5, influence);
    }

    @Test
    void noTowersInfluenceCalculator() {
        InfluenceCalculator calculator = new NoTowersInfluenceCalculator();

        int influence = calculator.calculateInfluence(p1, i1, professors);
        assertEquals(1, influence);

        influence = calculator.calculateInfluence(p2, i1, professors);
        assertEquals(5, influence);
    }

    @Test
    void noColoInfluenceCalculator() {
        InfluenceCalculator calculator = new NoColorInfluenceCalculator(Student.BLUE);

        int influence = calculator.calculateInfluence(p1, i1, professors);
        assertEquals(3, influence);

        influence = calculator.calculateInfluence(p2, i1, professors);
        assertEquals(2, influence);
    }

    @Test
    void AdditionalPointsInfluenceCalculator() {
        InfluenceCalculator calculator = new AdditionalPointsInfluenceCalculator(p1);

        int influence = calculator.calculateInfluence(p1, i1, professors);
        assertEquals(5, influence);

        influence = calculator.calculateInfluence(p2, i1, professors);
        assertEquals(5, influence);
    }

    @Test
    void professorsControlInfluenceCalculator() {
        InfluenceCalculator calculator = new ProfessorsControlInfluenceCalculator(p1);

        int influence = calculator.calculateInfluence(p1, i1, professors);
        assertEquals(5, influence);

        influence = calculator.calculateInfluence(p2, i1, professors);
        assertEquals(3, influence);
    }
}