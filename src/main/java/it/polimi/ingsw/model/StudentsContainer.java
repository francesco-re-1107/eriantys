package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

/**
 * This class represents a students container that can be modified with methods like addStudent, removeStudent, addAll...
 */
public class StudentsContainer extends AStudentsContainer{

    /**
     * Create an empty students container
     */
    public StudentsContainer() {
        super();
    }

    /**
     * Create an empty students container with a max size property
     * @param maxSize max number of students
     */
    public StudentsContainer(int maxSize) {
        super(maxSize);
    }

    /**
     * Create a new StudentsContainer starting from another AStudentsContainer
     * @param studentsContainer the students container used for creating this new container
     */
    public StudentsContainer(AStudentsContainer studentsContainer) {
        super();

        this.addAll(studentsContainer);
    }

    /**
     * Create a new StudentsContainer starting from another AStudentsContainer, with a max size property
     * if studentsContainer.getSize() > maxSize then a StudentsMaxReachedException will be thrown
     * @param studentsContainer the students container used for creating this new container
     * @param maxSize max number of students
     */
    public StudentsContainer(AStudentsContainer studentsContainer, int maxSize) {
        super(maxSize);

        this.addAll(studentsContainer);
    }

    /**
     * Add a student to this container
     * if the container is full then a StudentsMaxReachedException will be thrown
     * @param student the student to add
     */
    public void addStudent(Student student) {
        if (getSize() + 1 > maxSize)
            throw new StudentsMaxReachedException();

        int count =  this.students.getOrDefault(student, 0);
        this.students.put(student, count + 1);
    }

    /**
     * Add howMany student to this container
     * if the container is full then a StudentsMaxReachedException will be thrown
     * @param student the student to add
     * @param howMany number of students of this type to add to the container
     */
    public void addStudents(Student student, int howMany) {
        if (getSize() + howMany > maxSize)
            throw new StudentsMaxReachedException();

        int count =  this.students.getOrDefault(student, 0);
        this.students.put(student, count + howMany);
    }

    /**
     * Add a student to this container
     * if the container does not contain the sutend then a StudentNotFoundException will be thrown
     * @param student the student to remove
     */
    public void removeStudent(Student student){
        int count =  this.students.getOrDefault(student, 0);

        if (count <= 0)
            throw new StudentNotFoundException();

        this.students.put(student, count - 1);
    }

    /**
     * Add all students of anotherContainer to this container
     * if the container is full then a StudentsMaxReachedException will be thrown
     * @param anotherContainer
     */
    public void addAll(AStudentsContainer anotherContainer) {
        if (getSize() + anotherContainer.getSize() > maxSize)
            throw new StudentsMaxReachedException();

        anotherContainer.students.forEach(this::addStudents);
    }

    /**
     * Remove all students contained in anotherContainer from this container
     * @param anotherContainer
     */
    public void removeAll(AStudentsContainer anotherContainer) {
        //TODO: implement
    }

    /*
    public void addOnStudentNumberReachedListener(Student student, int count, ...) {

    }
    */
}
