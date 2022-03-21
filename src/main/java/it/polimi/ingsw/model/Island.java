package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IslandNotCompatibleException;

public class Island {

    private final StudentsContainer students;

    private int towersCount = 0;

    private Tower towerColor;

    public Island() {
        this.students = new StudentsContainer();
    }

    public Island(Student defaultStudent) {
        this();

        this.students.addStudent(defaultStudent);
    }

    public int getSize() {
        if(towersCount == 0)
            return 1;
        else
            return towersCount;
    }

    public int getTowersCount(){
        return towersCount;
    }

    public StudentsContainer getStudents(){
        return new StudentsContainer(this.students);
    }

    public void merge(Island anotherIsland) {
        if (this.towerColor != anotherIsland.towerColor)
            throw new IslandNotCompatibleException();

        students.addAll(anotherIsland.students);
        towersCount += anotherIsland.towersCount;
    }

    public void setIslandConquered(Tower towerColor){
        this.towerColor = towerColor;

        //if island has no tower then add one
        if(this.towersCount == 0)
            this.towersCount = 1;
    }

    public boolean isConquered(){
        return towersCount == 0;
    }

    public boolean checkMergeCompatibility(Island anotherIsland){
        return this.isConquered() && anotherIsland.isConquered()
                && this.towerColor == anotherIsland.towerColor;
    }
}
