package it.polimi.ingsw;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
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
        //default level is INFO
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

    /**
     * Check if the given nickname is valid
     * @param nickname
     * @return true if it is valid, false otherwise
     */
    public static boolean isValidNickname(String nickname) {
        return !nickname.isBlank() && nickname.length() <= Constants.MAX_NICKNAME_LENGTH;
    }

    /**
     * Loads server config properties from file if present, otherwise loads default values
     * @return ServerConfig object read from file
     */
    public static ServerConfig getServerConfig(){
        Properties prop = new Properties();
        try {
            InputStream is = Utils.class.getClassLoader().getResourceAsStream(Constants.SERVER_CONFIG_FILE_NAME);
            prop.load(is);
        } catch (Exception e) {
            LOGGER.info("Server config file not found, using default values");
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

    /**
     * Generate a random valid nickname.
     * @return the random nickname generated
     */
    public static String generateRandomNickname() {
        var r = new SecureRandom();
        var part1 = Constants.NICKNAMES_PART_1.get(r.nextInt(Constants.NICKNAMES_PART_1.size()));
        var part2 = Constants.NICKNAMES_PART_2.get(r.nextInt(Constants.NICKNAMES_PART_2.size()));

        //append a random digit
        return part1 + part2 + r.nextInt(10);
    }

    /**
     * Partition a list into n partitions (sublists) of equal size (elementsPerPartition).
     * The last list may be smaller than elementsPerPartition.
     * @param list the list to partition
     * @param elementsPerPartition number of elements per partition
     * @return a list of partitions
     * @param <T> the type of the list
     */
    public static <T> List<List<T>> partition(List<T> list, int elementsPerPartition) {
        var partitions = new ArrayList<List<T>>();

        for (int i = 0; i < list.size(); i += elementsPerPartition) {
            partitions.add(list.subList(i, Math.min(i + elementsPerPartition, list.size())));
        }

        return partitions;
    }

    /**
     * Check if the given string is an integer
     * @param string the string to check
     * @return true if the string is an integer, false otherwise
     */
    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parse integer without exceptions
     * @param string the string to parse
     * @return parsed integer or 0 if the string is not an integer
     */
    public static int parseInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Utils() {}
}
