package it.polimi.ingsw.server.model;

/**
 * This piece of code aims to implement a multilevel enum.
 * It's used to prevent players from performing invalid operation throughout round and game stages (i.e. not placing students twice,
 * or after having moved mother nature)
 */
public interface Stage {
    enum Plan implements Stage {
        PLAN
    }
    enum Attack implements Stage {
        STARTED,
        STUDENTS_PLACED,
        CARD_PLAYED,
        MOTHER_NATURE_MOVED,
        SELECTED_CLOUD
    }

    static boolean IsEqOrPost(Stage st1, Stage st2) {
        if (st2 instanceof Stage.Plan)
            return true;
        return ((Attack) st1).ordinal() >= ((Attack) st2).ordinal();
    }
}