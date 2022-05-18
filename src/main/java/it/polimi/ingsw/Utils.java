package it.polimi.ingsw;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
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
        return !nickname.isBlank() && nickname.length() <= Constants.MAX_NICKNAME_LENGTH;
    }

    static {
        setupLogger();
    }

    private Utils() {}

    public static ServerConfig getServerConfig(){
        Properties prop = new Properties();
        try {
            InputStream is = Utils.class.getClassLoader().getResourceAsStream(Constants.SERVER_CONFIG_FILE_NAME);
            prop.load(is);
        } catch (Exception e) {
            LOGGER.severe(e.toString());
            return new ServerConfig(
                    Constants.DEFAULT_SERVER_PORT,
                    Constants.DEFAULT_BACKUP_FOLDER
            );
        }
        return new ServerConfig(
            Integer.parseInt(prop.getProperty("server.port")),
            prop.getProperty("server.backupFolder")
        );
    }


    /**
     * Check if two lists are equal
     *
     * @param list The first list to compare.
     * @param anotherList The second list to compare to.
     * @return true if lists are equal (doesn't consider order and frequencies).
     */
    public static <T> boolean isSameList(List<T> list, List<T> anotherList) {
        return new HashSet<>(list).equals(new HashSet<>(anotherList));
    }

    /**
     * Check if the running OS is Windows
     * @return true if the OS is Windows, false otherwise
     */
    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static String generateRandomNickname() {
        var r = new SecureRandom();
        var part1 = Constants.NICKNAMES_PART_1.get(r.nextInt(Constants.NICKNAMES_PART_1.size()));
        var part2 = Constants.NICKNAMES_PART_2.get(r.nextInt(Constants.NICKNAMES_PART_2.size()));

        //append a random digit
        return part1 + part2 + r.nextInt(10);
    }
}
