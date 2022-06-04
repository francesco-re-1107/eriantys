package it.polimi.ingsw;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
     * Loads server config properties from environment variables if present, otherwise loads default values
     * @return ServerConfig object read from file
     */
    public static ServerConfig getServerConfig(){
        var port = Constants.DEFAULT_SERVER_PORT;
        var portEnv = System.getenv().get(Constants.SERVER_PORT_ENV_KEY);

        var backupFolder = Constants.DEFAULT_BACKUP_FOLDER;
        var backupFolderEnv = System.getenv().get(Constants.BACKUP_FOLDER_ENV_KEY);

        //check if the port is ok
        if(isInteger(portEnv))
            port = Integer.parseInt(portEnv);

        //check if the backup folder is ok
        if(isValidPath(backupFolderEnv))
            backupFolder = Paths.get(backupFolderEnv);

        return new ServerConfig(port, backupFolder);
    }

    /**
     * Check if a given string is a valid path
     *
     * @param path the path to check
     * @return true if the path is valid, false otherwise
     */
    public static boolean isValidPath(String path) {
        if(path == null) return false;

        try {
            var p = Paths.get(path);
            return true;
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
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
        if(string == null) return false;

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

    /**
     * Wrap a long string into a new string with line breaks every maxLength characters and
     * spacing at the beginning and a the end of each line.
     * @param string the string to wrap
     * @param maxLength the maximum length of each line
     * @param spacing the spacing to add at the beginning and end of each line
     * @return the wrapped string
     */
    public static String wrapWithSpaces(String string, int maxLength, int spacing) {
        var lines = string.split("(?<=\\G.{" + (maxLength - spacing * 2) + "})");
        var s = new StringBuilder();

        for(var line : lines) {
            var l = line.trim(); // trim spaces at the beginning and end of the line

            s.append(" ".repeat(spacing));
            s.append(l);
            s.append(" ".repeat(maxLength - spacing - l.length()));
            s.append("\n");
        }

        return s.toString();
    }

    private Utils() {}
}
