package it.polimi.ingsw.model;

import it.polimi.ingsw.Utils;
import java.util.HashMap;
import java.util.Map;

public abstract class AStudentsContainer {

    private Map<Student, Integer> students;

    private int maxSize = Integer.MAX_VALUE;

    public AStudentsContainer() {
        this.students = new HashMap<>();
    }

    public AStudentsContainer(int maxSize) {
        this();
        this.maxSize = maxSize;
    }

    public void addAll(AStudentsContainer anotherContainer) {
        if (getSize() + anotherContainer.getSize() > maxSize)
            throw NoMoreSpaceException();

        this.students.putAll(anotherContainer.students);
    }

    public int getSize(){
        return students
                .values()
                .stream()
                .mapToInt(a -> a)
                .sum();
    }

    public void setMaxSize(int maxSize){
        this.maxSize = maxSize;
    }

    public Map<Student, Integer> getStudentsCopy() {
        return new HashMap<>(students);
    }

    public int getCountForStudent(Student student) {
        return Utils.nullAlternative(students.get(student), 0);
    }
}
