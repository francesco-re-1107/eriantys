package it.polimi.ingsw;

import it.polimi.ingsw.model.AssistantCard;

import java.util.Comparator;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class offers several methods useful across the project
 */
public class Utils {

    public static <T> T nullAlternative(T o1, T o2) {
        return (o1 == null) ? o2 : o1;
    }

    public static Comparator<AssistantCard> priorityComparator = Comparator.comparingInt(AssistantCard::getTurnPriority);

    private static Logger logger = Logger.getLogger("it.polimi.ingsw");

    public static Logger SetupLogger(){
        logger.setLevel(Level.FINEST);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new PrettyLogFormatter());
        logger.addHandler(handler);
        return logger;
    }
}
