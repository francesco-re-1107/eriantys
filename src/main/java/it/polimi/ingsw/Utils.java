package it.polimi.ingsw;

import it.polimi.ingsw.model.AssistantCard;

import java.util.Comparator;

/**
 * This class offers several methods useful across the project
 */
public class Utils {

    public static <T> T nullAlternative(T o1, T o2) {
        return (o1 == null) ? o2 : o1;
    }

    public static Comparator<AssistantCard> priorityComparator = Comparator.comparingInt(AssistantCard::getTurnPriority);
}
