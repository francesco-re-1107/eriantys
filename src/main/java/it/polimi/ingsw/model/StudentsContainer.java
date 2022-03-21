package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

public class StudentsContainer extends AStudentsContainer{

    public StudentsContainer() {
        super();
    }

    public StudentsContainer(int maxSize) {
        super(maxSize);
    }

    public StudentsContainer(AStudentsContainer studentsContainer) {
        super();

        this.addAll(studentsContainer);
    }

    public void addStudent(Student student) {
        int count =  this.students.getOrDefault(student, 0);
        this.students.put(student, count + 1);
    }

    public void removeStudent(Student student){
        int count =  this.students.getOrDefault(student, 0);

        if (count <= 0)
            throw new StudentNotFoundException();

        this.students.put(student, count - 1);
    }

    public void addAll(AStudentsContainer anotherContainer) {
        if (getSize() + anotherContainer.getSize() > maxSize)
            throw new StudentsMaxReachedException();

        this.students.putAll(anotherContainer.students);
    }

    /*
    public void addOnStudentNumberReachedListener(Student student, int count, ...) {

    }
    */
}
