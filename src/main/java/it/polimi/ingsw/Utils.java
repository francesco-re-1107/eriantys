package it.polimi.ingsw;

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

    private static Logger logger = Logger.getLogger("it.polimi.ingsw");

    public static Logger setupLogger(Level level){
        logger.setLevel(level);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(level);
        handler.setFormatter(new PrettyLogFormatter());
        logger.addHandler(handler);
        return logger;
    }

    public static Logger setupLogger(){
        return setupLogger(Level.INFO);
    }
}
