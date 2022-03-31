package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.Test;


import java.util.HashMap;
import java.util.Map;

class Container extends AStudentsContainer {







    public Container() {
        Map<Student,Integer> students = getStudents();

    }
    public Container (int maxSize) {

        this.maxSize = maxSize;


    }

}
public class AStudentsContainerTest extends TestCase {
    @Test
    // getSize return the sum of all the students held in this container (all colors)
    public void testGetSize() {
        Container container = new Container();
        container.students.put(Student.YELLOW, 3);
        container.students.put(Student.GREEN ,5 );
        container.students.put(Student.RED, 6);
        container.students.put(Student.BLUE, 4);
        container.students.put(Student.PINK , 2);
        int i = container.getSize();
        assertEquals(20, i);

    }
    @Test
    // AStudentContainer max size must be equals either is used the constructor or the method setMaxSize
    public void testSetMaxSize() {
        Container container = new Container();
        container.setMaxSize(3);
        Container max = new Container(3);
        assertEquals(3, max.maxSize);
        assertEquals(3, container.maxSize);
    }

    public void testGetStudents() {
        Container container = new Container();
        Map<Student, Integer> students1;
        container.students.put(Student.YELLOW, 3);
        container.students.put(Student.GREEN ,5 );
        container.students.put(Student.RED, 6);
        container.students.put(Student.BLUE, 4);
        container.students.put(Student.PINK , 2);
        students1 = container.getStudents();
        assertEquals(students1, container.students);




    }
    @Test
    public void testGetCountForStudent() {

        Container container = new Container();
        container.students.put(Student.YELLOW, 3);
        container.students.put(Student.GREEN ,5 );
        container.students.put(Student.RED, 6);
        container.students.put(Student.BLUE, 4);
        container.students.put(Student.PINK , 2);

        assertEquals(3, container.getCountForStudent(Student.YELLOW));
        assertEquals(5, container.getCountForStudent(Student.GREEN));
        assertEquals(6, container.getCountForStudent(Student.RED));
        assertEquals(4, container.getCountForStudent(Student.BLUE));
        assertEquals(2, container.getCountForStudent(Student.PINK));
    }
}