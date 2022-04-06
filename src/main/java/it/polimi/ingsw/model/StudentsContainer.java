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
        super(studentsContainer);
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
     * @return this container for chaining
     */
    public StudentsContainer addStudent(Student student) {
        if (getSize() + 1 > maxSize)
            throw new StudentsMaxReachedException();

        int count =  this.students.getOrDefault(student, 0);
        this.students.put(student, count + 1);

        return this;
    }

    /**
     * Add howMany students of type student to this container
     * if the container is full then a StudentsMaxReachedException will be thrown
     * @param student the student to add
     * @param howMany number of students of this type to add to the container
     * @return this container for chaining
     */
    public StudentsContainer addStudents(Student student, int howMany) {
        if (getSize() + howMany > maxSize)
            throw new StudentsMaxReachedException();

        int count =  this.students.getOrDefault(student, 0);
        this.students.put(student, count + howMany);

        return this;
    }

    /**
     * Remove a student from this container
     * if the container does not contain the student then a StudentNotFoundException will be thrown
     * @param student the student to remove
     * @return this container for chaining
     */
    public StudentsContainer removeStudent(Student student){
        int count =  this.students.getOrDefault(student, 0);

        if (count <= 0)
            throw new StudentNotFoundException();

        this.students.put(student, count - 1);

        return this;
    }

    /**
     * Remove howMany students of type student from this container
     * if the container does not contain the students then a StudentNotFoundException will be thrown
     * @param student the student to remove
     * @param howMany number of students of this type to remove from the container
     * @return this container for chaining
     */
    public StudentsContainer removeStudents(Student student, int howMany) {
        if(getCountForStudent(student) < howMany)
            throw new StudentNotFoundException();

        int count =  this.students.getOrDefault(student, 0);
        this.students.put(student, count - howMany);

        return this;
    }

    /**
     * Add all students of anotherContainer to this container
     * if the container is full then a StudentsMaxReachedException will be thrown
     * @param anotherContainer students to add to this container
     * @return this container for chaining
     */
    public StudentsContainer addAll(AStudentsContainer anotherContainer) {
        if (getSize() + anotherContainer.getSize() > maxSize)
            throw new StudentsMaxReachedException();

        anotherContainer.students.forEach(this::addStudents);

        return this;
    }

    /**
     * Remove all students contained in anotherContainer from this container
     * @param anotherContainer students to remove from this container
     * @return this container for chaining
     */
    public StudentsContainer removeAll(AStudentsContainer anotherContainer) {
        anotherContainer.students.forEach(this::removeStudents);

        return this;
    }

    /*
    public void addOnStudentNumberReachedListener(Student student, int count, ...) {

    }
    */
}
