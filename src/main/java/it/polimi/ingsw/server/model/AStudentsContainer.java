package it.polimi.ingsw.server.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a generic students container which cannot be modified
 */
public abstract class AStudentsContainer implements Serializable {

    @Serial
    private static final long serialVersionUID = 2063985746502559014L;

    /**
     * Used internally to store the students
     */
    protected final EnumMap<Student, Integer> students;

    /**
     * Used internally to store the maximum number of values that this container can hold
     */
    protected int maxSize = Integer.MAX_VALUE;

    /**
     * Create a generic container (and not modifiable) with given maxSize
     */
    protected AStudentsContainer() {
        this.students = new EnumMap<>(Student.class);
    }

    /**
     * Create a generic container (and not modifiable) with given maxSize
     * @param maxSize maximum number of students that this container can hold
     */
    protected AStudentsContainer(int maxSize) {
        this();
        this.maxSize = maxSize;
    }

    /**
     * Create a new AStudentsContainer starting from another AStudentsContainer
     * @param studentsContainer the students container used for creating this new container
     */
    protected AStudentsContainer(AStudentsContainer studentsContainer) {
        this.students = new EnumMap<>(studentsContainer.students);
        this.maxSize = studentsContainer.maxSize;
    }

    /**
     * @return the number of students (every color) held in this container
     */
    public int getSize(){
        return students
                .values()
                .stream()
                .mapToInt(a -> a)
                .sum();
    }

    /**
     * Set the maximum number of students that this container can hold, if not set it is Integer.MAX_VALUE
     * @param maxSize the maximum number of students that this container can hold
     */
    public void setMaxSize(int maxSize){
        this.maxSize = maxSize;
    }

    /**
     * @return max size set for this container
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * @return a copy of the private map used for storing students
     */
    public Map<Student, Integer> getStudents() {
        return new EnumMap<>(students);
    }

    /**
     * @param student the student used for filtering
     * @return the number of students (of type student) held in this container
     */
    public int getCountForStudent(Student student) {
        return Objects.requireNonNullElse(students.get(student), 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AStudentsContainer that = (AStudentsContainer) o;
        return maxSize == that.maxSize && students.equals(that.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(students, maxSize);
    }

    @Override
    public String toString() {
        return students.toString();
    }

    /**
     * Check if this container has the given students
     * @param anotherContainer the container to check
     * @return true if this all the students present in anotherContainer are present also in this container, false otherwise
     */
    public boolean contains(AStudentsContainer anotherContainer) {
        for(var s : Student.values())
            if(getCountForStudent(s) < anotherContainer.getCountForStudent(s))
                return false;
        return true;
    }
}
