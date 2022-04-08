package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.exceptions.StudentsMaxReachedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class StudentsContainerTest {

    StudentsContainer container;
    StudentsContainer containerWithMaxSize;

    @BeforeEach
    void setUp() throws Exception {
        //test constructors

        this.container = new StudentsContainer();
        this.containerWithMaxSize = new StudentsContainer(1);

        StudentsContainer anotherContainer =
                new StudentsContainer(container, 1);

        //testing equals for AStudentsContainer
        assertNotEquals(container, containerWithMaxSize);
        assertEquals(containerWithMaxSize, anotherContainer);

        assertEquals(1, anotherContainer.getMaxSize());
    }

    @Test
    void addStudent() {
        int prevSize = container.getCountForStudent(Student.BLUE);
        container.addStudent(Student.BLUE);
        int currSize = container.getCountForStudent(Student.BLUE);

        assertEquals(prevSize + 1, currSize);

        containerWithMaxSize.addStudent(Student.BLUE);
        assertThrows(
                StudentsMaxReachedException.class,
                () -> containerWithMaxSize.addStudent(Student.BLUE)
        );
    }

    @Test
    void addStudents() {
        int prevSize = container.getCountForStudent(Student.BLUE);
        container.addStudents(Student.BLUE, 10);
        int currSize = container.getCountForStudent(Student.BLUE);

        assertEquals(prevSize + 10, currSize);

        assertThrows(
                StudentsMaxReachedException.class,
                () -> containerWithMaxSize.addStudents(Student.BLUE, 10)
        );
    }

    @Test
    void removeStudent() {
        assertThrows(
                StudentNotFoundException.class,
                () -> container.removeStudent(Student.BLUE)
        );

        container.addStudent(Student.BLUE);
        int prevSize = container.getCountForStudent(Student.BLUE);
        container.removeStudent(Student.BLUE);
        int currSize = container.getCountForStudent(Student.BLUE);

        assertEquals(prevSize - 1, currSize);
    }

    @Test
    void removeStudents() {
        assertThrows(
                StudentNotFoundException.class,
                () -> container.removeStudents(Student.BLUE, 3)
        );

        container.addStudents(Student.BLUE, 10);

        int prevSize = container.getCountForStudent(Student.BLUE);
        container.removeStudents(Student.BLUE, 5);
        int currSize = container.getCountForStudent(Student.BLUE);

        assertEquals(prevSize - 5, currSize);
    }

    @Test
    void addAll() {
        StudentsContainer anotherContainer = new StudentsContainer();

        container.addStudents(Student.GREEN, 4);
        container.addStudents(Student.BLUE, 1);

        anotherContainer.addStudents(Student.YELLOW, 2);
        anotherContainer.addStudents(Student.BLUE, 2);

        int prevSize = container.getSize();

        container.addAll(anotherContainer);
        assertEquals(anotherContainer.getSize() + prevSize, container.getSize());

        assertEquals(3, container.getCountForStudent(Student.BLUE));
        assertEquals(4, container.getCountForStudent(Student.GREEN));
        assertEquals(2, container.getCountForStudent(Student.YELLOW));


        assertThrows(
                StudentsMaxReachedException.class,
                () -> containerWithMaxSize.addAll(container)
        );
    }

    @Test
    void removeAll() {
        StudentsContainer anotherContainer = new StudentsContainer();

        container.addStudents(Student.GREEN, 4);
        container.addStudents(Student.BLUE, 1);

        anotherContainer.addStudents(Student.GREEN, 2);
        anotherContainer.addStudents(Student.BLUE, 1);

        int prevSize = container.getSize();

        container.removeAll(anotherContainer);
        assertEquals(prevSize - anotherContainer.getSize(), container.getSize());

        assertEquals(2, container.getCountForStudent(Student.GREEN));
        assertEquals(0, container.getCountForStudent(Student.BLUE));

        assertThrows(
                StudentNotFoundException.class,
                () -> container.removeAll(anotherContainer)
        );
    }

    @Test
    void getSize() {
        assertEquals(0, container.getSize());

        container.addStudent(Student.GREEN);
        assertEquals(1, container.getSize());

        container.addStudents(Student.RED, 3);
        assertEquals(4, container.getSize());
    }

    @Test
    void setMaxSize() {
        container.setMaxSize(3);
        assertEquals(3, container.getMaxSize());
    }

    @Test
    void studentNumberReachedListeners() {
        AtomicBoolean callback1Called = new AtomicBoolean(false);
        AtomicBoolean callback2Called = new AtomicBoolean(false);
        AtomicBoolean callback3Called = new AtomicBoolean(false);

        container.addOnStudentNumberReachedListener(Student.RED, 3, (s, c) -> {
            assertEquals(Student.RED, s);
            assertTrue(c >= 3);
            callback1Called.set(true);
        });

        container.addOnStudentNumberReachedListener(Student.BLUE, 6, (s, c) -> {
            assertEquals(Student.BLUE, s);
            assertTrue(c >= 6);
            callback2Called.set(true);
        });

        container.addOnStudentNumberReachedListener(Student.PINK, 1, (s, c) -> {
            callback3Called.set(true);
        });

        container.addStudents(Student.RED, 2);
        assertFalse(callback2Called.get());
        container.addStudents(Student.RED, 2);
        assertTrue(callback1Called.get());

        container.addStudents(Student.BLUE, 5);
        assertFalse(callback2Called.get());
        container.addStudent(Student.BLUE);
        assertTrue(callback2Called.get());

        container.removeOnStudentNumberReachedListener(Student.PINK, 1);
        container.addStudent(Student.PINK);
        assertFalse(callback3Called.get());
    }

    @Test
    void getStudents() {
        assertNotNull(container.getStudents());
    }

}