package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentNotFoundException;

import java.util.Random;

public class RandomizedStudentsContainer extends AStudentsContainer{

    private final Random random = new Random();

    public RandomizedStudentsContainer(int studentsPerColor) {
        super();
        students.put(Student.BLUE, studentsPerColor);
        students.put(Student.GREEN, studentsPerColor);
        students.put(Student.PINK, studentsPerColor);
        students.put(Student.RED, studentsPerColor);
        students.put(Student.YELLOW, studentsPerColor);
    }

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

    public StudentsContainer pickManyRandom(int howMany) {
        StudentsContainer picked = new StudentsContainer();

        while(picked.getSize() < howMany && getSize() > 0)
            picked.addStudent(pickOneRandom());

        return picked;
    }

}
