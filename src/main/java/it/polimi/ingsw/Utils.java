package it.polimi.ingsw;

import it.polimi.ingsw.model.AssistantCard;

import java.util.Comparator;

public class Utils {

    public static <T> T nullAlternative(T o1, T o2) {
        return (o1 == null) ? o2 : o1;
    }

    public static Comparator<AssistantCard> priorityComparator = new Comparator<AssistantCard>() {
        @Override public int compare(AssistantCard s1, AssistantCard s2) {
            return s1.getTurnPriority() - s2.getTurnPriority();
        }
    };
}
