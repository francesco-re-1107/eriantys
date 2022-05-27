package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;

import java.io.Serializable;

/**
 * This class represents an island of the game.
 */
public class Island implements Serializable {

    /**
     * Stores the students on this island
     */
    private final StudentsContainer students;

    /**
     * Stores the number of towers on this island
      */
    private int towersCount = 0;

    /**
     * Stores the tower color of this island if conquered
     */
    private Tower towerColor;

    /**
     * Whether a No Entry tile is positioned on this island
     */
    private boolean noEntry = false;

    /**
     * Create an empty island
     */
    public Island() {
        this.students = new StudentsContainer();
    }

    /**
     * Create an island with a starting student
     * @param defaultStudent the initial student to put on th island
     */
    public Island(Student defaultStudent) {
        this();

        addStudent(defaultStudent);
    }

    /**
     * Get the size of this island (e.g. 3 means that this island was created merging 3 different islands)
     * @return the size of the island
     */
    public int getSize() {
        if(towersCount == 0)
            return 1;
        else
            return towersCount;
    }

    /**
     * @return the number of towers on this island
     */
    public int getTowersCount(){
        return towersCount;
    }

    /**
     * @return the towers color of this island if conquered, null otherwise
     */
    public Tower getTowerColor() {
        return towerColor;
    }

    /**
     * @return a copy of the students located on this island
     */
    public StudentsContainer getStudents(){
        return new StudentsContainer(this.students);
    }

    /**
     * Add a student to this island
     * @param student the student to add
     */
    public void addStudent(Student student){
        this.students.addStudent(student);
    }

    /**
     * Add many students to this island
     * @param studentsContainer the students to add
     */
    public void addStudents(StudentsContainer studentsContainer){
        this.students.addAll(studentsContainer);
    }

    /**
     * Merge this island with anotherIsland. All students on anotherIsland will be merged with this island, the same for
     * the towers. They need to be compatible (@see isMergeCompatible)
     * @param anotherIsland the island to merge with
     */
    public void merge(Island anotherIsland) {
        if (!isMergeCompatible(anotherIsland))
            throw new InvalidOperationError("Islands not compatible, cannot merge");

        students.addAll(anotherIsland.students);
        towersCount += anotherIsland.towersCount;
    }

    /**
     * Used when a player has the most influence on this island
     * @param towerColor color of the tower of the player
     */
    public void setConquered(Tower towerColor){
        this.towerColor = towerColor;

        //if island has no tower then add one
        if(this.towersCount == 0)
            this.towersCount = 1;
    }

    /**
     * @return whether the island is conquered
     */
    public boolean isConquered(){
        return towersCount != 0;
    }

    /**
     * @return true if on this island is present a no entry tile
     */
    public boolean isNoEntry() {
        return noEntry;
    }

    /**
     * Set whether on this island is present a No Entry tile
     * @param noEntry
     */
    public void setNoEntry(boolean noEntry) {
        this.noEntry = noEntry;
    }

    /**
     * Check if two islands are merge-compatible
     * @param anotherIsland the other island
     * @return true if this island  and anotherIsland can be merged, false otherwise
     */
    public boolean isMergeCompatible(Island anotherIsland){
        return this.isConquered() && anotherIsland.isConquered()
                && this.towerColor == anotherIsland.towerColor;
    }
}
