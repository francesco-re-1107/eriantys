package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.exceptions.StudentsMaxReachedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomizedStudentsContainerTest {

    RandomizedStudentsContainer container;

    @BeforeEach
    void setUp() {
        this.container = new RandomizedStudentsContainer(1);
    }

    @Test
    void pickOneRandom() {
        Student s;

        s = container.pickOneRandom();
        assertEquals(0, container.getCountForStudent(s));

        s = container.pickOneRandom();
        assertEquals(0, container.getCountForStudent(s));

        s = container.pickOneRandom();
        assertEquals(0, container.getCountForStudent(s));

        s = container.pickOneRandom();
        assertEquals(0, container.getCountForStudent(s));

        s = container.pickOneRandom();
        assertEquals(0, container.getCountForStudent(s));

        assertEquals(0, container.getSize());

        assertThrows(
                StudentNotFoundException.class,
                () -> container.pickOneRandom()
        );
    }

    @Test
    void pickManyRandom() {
        StudentsContainer picked = container.pickManyRandom(3);
        assertEquals(3, picked.getSize());
        assertEquals(2, container.getSize());

        picked = container.pickManyRandom(5);
        assertEquals(2, picked.getSize());
        assertEquals(0, container.getSize());
    }
}