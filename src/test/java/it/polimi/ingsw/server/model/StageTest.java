package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StageTest {

    @Test
    void isEqualOrPost() {
        //test negative
        assertFalse(Stage.isEqualOrPost(Stage.Attack.MOTHER_NATURE_MOVED, Stage.Attack.SELECTED_CLOUD));

        //test positive
        assertTrue(Stage.isEqualOrPost(Stage.Attack.MOTHER_NATURE_MOVED, Stage.Attack.STUDENTS_PLACED));

        //test positive
        assertTrue(Stage.isEqualOrPost(Stage.Attack.STUDENTS_PLACED, Stage.Plan.PLAN));
    }
}