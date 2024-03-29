package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                InvalidOperationError.class,
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

    @Test
    void equals() {
        var container2 = new RandomizedStudentsContainer(1);
        assertEquals(container, container2);
        assertEquals(container.hashCode(), container2.hashCode());
    }
}