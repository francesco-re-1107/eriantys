package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.exceptions.StudentsMaxReachedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        try{
            container.removeStudent(Student.BLUE);
            fail();
        }catch(StudentNotFoundException e){
            assertTrue(true);
        }

        container.addStudent(Student.BLUE);
        int prevSize = container.getCountForStudent(Student.BLUE);
        container.removeStudent(Student.BLUE);
        int currSize = container.getCountForStudent(Student.BLUE);

        assertEquals(prevSize - 1, currSize);
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

        assertEquals(container.getCountForStudent(Student.BLUE), 3);
        assertEquals(container.getCountForStudent(Student.GREEN), 4);
        assertEquals(container.getCountForStudent(Student.YELLOW), 2);


        assertThrows(
                StudentsMaxReachedException.class,
                () -> containerWithMaxSize.addAll(container)
        );
    }

    @Test
    void removeAll() {
    }

    @Test
    void getSize() {
        assertEquals(0, container.getSize());

        container.addStudent(Student.GREEN);
        assertEquals(1, container.getSize());

        container.addStudents(Student.RED, 3);
        assertEquals(4, container.getSize());
    }

}