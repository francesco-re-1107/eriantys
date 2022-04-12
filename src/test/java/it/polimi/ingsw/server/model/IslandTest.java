package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.IslandNotCompatibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

    Island i1, i2;

    @BeforeEach
    void setUp() {
        i1 = new Island();
        i2 = new Island(Student.RED);
    }

    @Test
    void getSize() {
        assertEquals(1, i1.getSize());
        assertEquals(1, i2.getSize());

        i1.setConquered(Tower.BLACK);
        i2.setConquered(Tower.BLACK);

        assertEquals(1, i1.getSize());
        assertEquals(1, i2.getSize());

        i1.addStudent(Student.BLUE);
        i1.addStudent(Student.RED);

        i1.merge(i2);
        assertEquals(2, i1.getSize());
        assertEquals(3, i1.getStudents().getSize());
    }

    @Test
    void getTowersCount() {

    }

    @Test
    void getTowerColor() {
        i1.setConquered(Tower.BLACK);

        assertEquals(Tower.BLACK, i1.getTowerColor());
    }

    @Test
    void getStudents() {
        assertEquals(0, i1.getStudents().getSize());
        assertEquals(1, i2.getStudents().getSize());
    }

    @Test
    void addStudent() {
        int prevSize = i1.getStudents().getCountForStudent(Student.BLUE);
        i1.addStudent(Student.BLUE);
        int currSize = i1.getStudents().getCountForStudent(Student.BLUE);

        assertEquals(prevSize + 1, currSize);
    }

    @Test
    void addStudents() {
        StudentsContainer c = new StudentsContainer();
        c.addStudents(Student.BLUE, 3);
        c.addStudents(Student.YELLOW, 2);

        int prevSize = i1.getStudents().getSize();
        i1.addStudents(c);
        int currSize = i1.getStudents().getSize();

        assertEquals(prevSize + 5, currSize);
    }

    @Test
    void merge() {
        assertThrows(
                IslandNotCompatibleException.class,
                () -> i1.merge(i2)
        );

        i1.setConquered(Tower.BLACK);
        i2.setConquered(Tower.BLACK);

        int studentsPrevSize = i1.getStudents().getSize();
        int towersPrevCount = i1.getTowersCount();
        i1.merge(i2);

        //island size
        assertEquals(2, i1.getSize());

        //tower color
        assertEquals(Tower.BLACK, i1.getTowerColor());

        //students
        assertEquals(
                studentsPrevSize + i2.getStudents().getSize(),
                i1.getStudents().getSize()
        );

        //towers
        assertEquals(
                towersPrevCount + i2.getTowersCount(),
                i1.getTowersCount()
        );
    }

    @Test
    void setConquered() {
        assertFalse(i1.isConquered());
        i1.setConquered(Tower.WHITE);
        assertTrue(i1.isConquered());
    }

    @Test
    void setNoEntry() {
        assertFalse(i1.isNoEntry());
        i1.setNoEntry(true);
        assertTrue(i1.isNoEntry());
    }

    @Test
    void isMergeCompatible() {
        assertFalse(i1.isMergeCompatible(i2));


        i1.setConquered(Tower.WHITE);
        assertFalse(i1.isMergeCompatible(i2));

        i2.setConquered(Tower.BLACK);
        assertFalse(i1.isMergeCompatible(i2));

        i2.setConquered(Tower.WHITE);
        assertTrue(i1.isMergeCompatible(i2));

    }
}