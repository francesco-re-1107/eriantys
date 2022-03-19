package it.polimi.ingsw;

public class Utils {

    public static <T> T nullAlternative(T o1, T o2) {
        return (o1 == null) ? o2 : o1;
    }
}
