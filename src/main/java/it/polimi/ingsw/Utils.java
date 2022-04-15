package it.polimi.ingsw;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class offers several methods useful across the project
 */
public class Utils {

    public static <T> T nullAlternative(T o1, T o2) {
        return (o1 == null) ? o2 : o1;
    }

    public static final Logger LOGGER = Logger.getLogger("Logger");

    private static void setupLogger(){
        LOGGER.setLevel(Level.INFO);
        LogManager.getLogManager().reset();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        handler.setFormatter(new PrettyLogFormatter());
        LOGGER.addHandler(handler);
    }

    static {
        setupLogger();
    }

}
