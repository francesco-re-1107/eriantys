package it.polimi.ingsw;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class offers several methods useful across the project
 */
public class Utils {

    /**
     * If o1 is not null it is returned, otherwise o2 is returned
     * @param o1
     * @param o2
     * @return o1 if it is not null, o2 otherwise
     */
    public static <T> T nullAlternative(T o1, T o2) {
        return (o1 == null) ? o2 : o1;
    }

    /**
     * Default logger across the probject
     */
    public static final Logger LOGGER = Logger.getLogger("Logger");

    /**
     * Called when the program is started
     */
    private static void setupLogger(){
        LOGGER.setLevel(Level.INFO);
        LogManager.getLogManager().reset();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        handler.setFormatter(new PrettyLogFormatter());
        LOGGER.addHandler(handler);
    }

    /**
     * Check if the given nickname is valid
     * @param nickname
     * @return true if it is valid, false otherwise
     */
    public static boolean isValidNickname(String nickname) {
        return !nickname.isBlank();
    }

    static {
        setupLogger();
    }

    private Utils() {}
}
