package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentNotFoundException;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 * This class is an AStudentsContainer but offers methods for picking random students from the container.
 * It is used, for example, to represents the students bag of the game
 */
public class RandomizedStudentsContainer extends AStudentsContainer{

    /**
     * The random object used to pick random students
     */
    private final Random random = new SecureRandom();

    /**
     * Create a randomized students container
     * @param studentsPerColor number of students per color that will be added to the bag
     *                         (e.g. 2 means: 2 Student.GREEN, 2 Student.YELLOW, 2 Student.BLUE...)
     */
    public RandomizedStudentsContainer(int studentsPerColor) {
        super();

        for(Student s : Student.values())
            students.put(s, studentsPerColor);
    }

    /**
     * Create a randomized students container starting from another container
     * @param studentsContainer the other container to copy into this new container
     */
    public RandomizedStudentsContainer(AStudentsContainer studentsContainer) {
        super(studentsContainer);
    }

    /**
     * Pick a random student from this bag, if the container is empty a StudentNotFoundException will be thrown
     * @return a random student picked from this bag
     */
    public Student pickOneRandom(){
        if(getSize() <= 0)
            throw new StudentNotFoundException();

        Student pickedStudent;

        do{
            pickedStudent = Student.values()[random.nextInt(Student.values().length)];
        } while(getCountForStudent(pickedStudent) <= 0);

        //remove student from the bag
        int count =  this.students.getOrDefault(pickedStudent, 0);
        this.students.put(pickedStudent, count - 1);

        return pickedStudent;
    }

    /**
     * Pick multiple random students from this bag, if the container is not big enough only (and all) the
     * available students will be returned.
     * @return multiple random students picked from this bag
     */
    public StudentsContainer pickManyRandom(int howMany) {
        if(howMany >= getSize()) {
            StudentsContainer pickAll = new StudentsContainer(this);
            this.students.clear();
            return pickAll; //return all this bag
        }

        StudentsContainer picked = new StudentsContainer();

        while(picked.getSize() < howMany)
            picked.addStudent(pickOneRandom());

        return picked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RandomizedStudentsContainer that = (RandomizedStudentsContainer) o;
        return Objects.equals(random, that.random);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), random);
    }
}
