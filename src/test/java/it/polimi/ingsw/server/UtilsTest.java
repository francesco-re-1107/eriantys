package it.polimi.ingsw.server;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    @Test
    void partition() {
        var c = new StudentsContainer()
                .addStudent(Student.YELLOW)
                .addStudent(Student.GREEN)
                .addStudents(Student.BLUE, 2);

        assertEquals(4, c.toList().size());

        var partitioned = Utils.partition(c.toList(), 2);
        assertEquals(2, partitioned.size());
        System.out.println(partitioned);
        assertEquals(2, partitioned.get(0).size());
        assertEquals(2, partitioned.get(1).size());
    }
}
