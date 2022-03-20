package it.polimi.ingsw.model;

import java.util.Random;

public class RandomizedStudentsContainer extends AStudentsContainer{

    private final Random random = new Random();

    public RandomizedStudentsContainer() {
        super();
        students.put(Student.BLUE, 26);
        students.put(Student.GREEN, 26);
        students.put(Student.PINK, 26);
        students.put(Student.RED, 26);
        students.put(Student.YELLOW, 26);
    }

    public StudentsContainer pickRandom(int howMany) {
        StudentsContainer picked = new StudentsContainer();
        while(picked.getSize() < howMany){
            Student pickedStudent = Student.values()[random.nextInt(5)];

            if(getCountForStudent(pickedStudent) > 0){
                picked.addStudent(pickedStudent);

                //remove student from the bag
                int count =  this.students.getOrDefault(pickedStudent, 0);
                this.students.put(pickedStudent, count - 1);
            }

        }

        return picked;
    }

}
