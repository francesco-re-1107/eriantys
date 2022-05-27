package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;
import it.polimi.ingsw.common.exceptions.StudentsMaxReachedError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a students container that can be modified with methods like addStudent, removeStudent, addAll...
 */
public class StudentsContainer extends AStudentsContainer {

    private final Map<Trigger, StudentNumberReachedListener> triggerListeners = new HashMap<>();

    /**
     * Create an empty students container
     */
    public StudentsContainer() {
        super();
    }

    /**
     * Create an empty students container with a max size property
     *
     * @param maxSize max number of students
     */
    public StudentsContainer(int maxSize) {
        super(maxSize);
    }

    /**
     * Create a new StudentsContainer starting from another AStudentsContainer
     *
     * @param studentsContainer the students container used for creating this new container
     */
    public StudentsContainer(AStudentsContainer studentsContainer) {
        super(studentsContainer);
    }

    /**
     * Create a new StudentsContainer starting from another AStudentsContainer, with a max size property
     * if studentsContainer.getSize() > maxSize then a StudentsMaxReachedException will be thrown
     *
     * @param studentsContainer the students container used for creating this new container
     * @param maxSize           max number of students
     */
    public StudentsContainer(AStudentsContainer studentsContainer, int maxSize) {
        super(maxSize);

        this.addAll(studentsContainer);
    }

    /**
     * Add a student to this container
     * if the container is full then a StudentsMaxReachedException will be thrown
     *
     * @param student the student to add
     * @return this container for chaining
     */
    public StudentsContainer addStudent(Student student) {
        if (getSize() + 1 > maxSize)
            throw new StudentsMaxReachedError();

        int count = this.students.getOrDefault(student, 0) + 1;
        this.students.put(student, count);

        new HashMap<>(triggerListeners)
                .entrySet()
                .stream()
                .filter(e -> e.getKey().student() == student && e.getKey().count() == count)
                .map(Map.Entry::getValue)
                .forEach(e -> e.onStudentNumberReachedListener(student, count));

        return this;
    }

    /**
     * Add howMany students of type student to this container
     * if the container is full then a StudentsMaxReachedException will be thrown
     *
     * @param student the student to add
     * @param howMany number of students of this type to add to the container
     * @return this container for chaining
     */
    public StudentsContainer addStudents(Student student, int howMany) {
        if (getSize() + howMany > maxSize)
            throw new StudentsMaxReachedError();

        int count = this.students.getOrDefault(student, 0) + howMany;
        this.students.put(student, count);

        //create a copy
        new HashMap<>(triggerListeners)
                .entrySet()
                .stream()
                .filter(e -> e.getKey().student() == student && count >= e.getKey().count())
                .map(Map.Entry::getValue)
                .forEach(e -> e.onStudentNumberReachedListener(student, count));

        return this;
    }

    /**
     * Remove a student from this container
     * if the container does not contain the student then a StudentNotFoundException will be thrown
     *
     * @param student the student to remove
     * @return this container for chaining
     */
    public StudentsContainer removeStudent(Student student) {
        int count = this.students.getOrDefault(student, 0);

        if (count <= 0)
            throw new InvalidOperationError("Cannot remove student, not enough students");

        this.students.put(student, count - 1);

        return this;
    }

    /**
     * Remove howMany students of type student from this container
     * if the container does not contain the students then a StudentNotFoundException will be thrown
     *
     * @param student the student to remove
     * @param howMany number of students of this type to remove from the container
     * @return this container for chaining
     */
    public StudentsContainer removeStudents(Student student, int howMany) {
        if (getCountForStudent(student) < howMany)
            throw new InvalidOperationError("Cannot remove student, not enough students");

        int count = this.students.getOrDefault(student, 0);
        this.students.put(student, count - howMany);

        return this;
    }

    /**
     * Add all students of anotherContainer to this container
     * if the container is full then a StudentsMaxReachedException will be thrown
     *
     * @param anotherContainer students to add to this container
     * @return this container for chaining
     */
    public StudentsContainer addAll(AStudentsContainer anotherContainer) {
        if (getSize() + anotherContainer.getSize() > maxSize)
            throw new StudentsMaxReachedError();

        anotherContainer.students.forEach(this::addStudents);

        return this;
    }

    /**
     * Remove all students contained in anotherContainer from this container
     *
     * @param anotherContainer students to remove from this container
     * @return this container for chaining
     */
    public StudentsContainer removeAll(AStudentsContainer anotherContainer) {
        anotherContainer.students.forEach(this::removeStudents);

        return this;
    }

    public List<Student> toList() {
        var array = new ArrayList<Student>();

        this.students.forEach((student, count) -> {
            for (int i = 0; i < count; i++)
                array.add(student);
        });

        return array;
    }

    /**
     * Add a listener that will be triggered when the given student and count will be reached
     * @param student
     * @param count
     * @param listener
     */
    public void addOnStudentNumberReachedListener(Student student, int count, StudentNumberReachedListener listener) {
        triggerListeners.put(new Trigger(student, count), listener);
    }

    /**
     * Remove a previously registered listener.
     * If the listener doesn't exist, nothing will happen.
     * @param student
     * @param count
     */
    public void removeOnStudentNumberReachedListener(Student student, int count) {
        triggerListeners.remove(new Trigger(student, count));
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Listener for specific student and count
     */
    public interface StudentNumberReachedListener extends Serializable{
        void onStudentNumberReachedListener(Student student, int count);
    }

    /**
     * Used internally for listeners
     */
    private record Trigger(Student student, int count) implements Serializable {}
}
