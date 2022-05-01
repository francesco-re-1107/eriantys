package it.polimi.ingsw;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class offers several methods useful across the project
 */
public class Utils {

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

    public static AppConfig GetAppConfig(){
        Properties prop = new Properties();
        try {
            InputStream is = Utils.class.getClassLoader().getResourceAsStream("app.config");
            prop.load(is);
        } catch (Exception e) {
            LOGGER.severe(e.toString());
            return new AppConfig(
                    "127.0.0.1",
                    Constants.DEFAULT_SERVER_PORT
            );
        }
        return new AppConfig(
            prop.getProperty("server.address"),
            Integer.parseInt(prop.getProperty("server.port"))
        );
    }
}
