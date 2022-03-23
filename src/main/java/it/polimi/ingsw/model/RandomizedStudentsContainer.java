package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentNotFoundException;
import java.util.Random;

/**
 * This class is an AStudentsContainer but offers methods for picking random students from the container.
 * It is used, for example, to represents the students bag of the game
 */
public class RandomizedStudentsContainer extends AStudentsContainer{

    /**
     * The random object used to pick random students
     * TODO: store the seed for persistance
     */
    private final Random random = new Random();

    /**
     * Create a randomized students container
     * @param studentsPerColor number of students per color that will be added to the bag
     *                         (e.g. 2 means: 2 Student.GREEN, 2 Student.YELLOW, 2 Student.BLUE...)
     */
    public RandomizedStudentsContainer(int studentsPerColor) {
        super();
        students.put(Student.BLUE, studentsPerColor);
        students.put(Student.GREEN, studentsPerColor);
        students.put(Student.PINK, studentsPerColor);
        students.put(Student.RED, studentsPerColor);
        students.put(Student.YELLOW, studentsPerColor);
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
            pickedStudent = Student.values()[random.nextInt(5)];
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
        if(howMany >= getSize())
            return new StudentsContainer(this); //return all this bag

        StudentsContainer picked = new StudentsContainer();

        while(picked.getSize() < howMany)
            picked.addStudent(pickOneRandom());

        return picked;
    }

}
