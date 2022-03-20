package it.polimi.ingsw.model;

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

    /**
     * Used by merge, create a new island starting from island1 and island2
     * @param island1
     * @param island2
     */
    private Island(Island island1, Island island2) {
        this();

        students.addAll(island1.students);
        students.addAll(island2.students);
        towerColor = island1.towerColor;
        towersCount = island1.towersCount + island2.towersCount;
    }

    public int getSize() {
        return towersCount;
    }

    public StudentsContainer getStudents(){
        return new StudentsContainer(this.students);
    }

    public Island merge(Island anotherIsland) {
        if (this.towerColor != anotherIsland.towerColor)
            throw new IslandNotCompatibleException();

        return new Island(this, anotherIsland);
    }

    public void setIslandConquered(Tower towerColor){
        this.towerColor = towerColor;

        //if island has no tower then add one
        if(this.towersCount == 0)
            this.towersCount = 1;
    }
}
