package it.polimi.ingsw;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
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

    public static String generateRandomNickname() {
        var r = new Random();
        var part1 = Constants.NICKNAMES_PART_1.get(r.nextInt(Constants.NICKNAMES_PART_1.size()));
        var part2 = Constants.NICKNAMES_PART_2.get(r.nextInt(Constants.NICKNAMES_PART_2.size()));

        var nick = new StringBuilder();
        nick.append(part1);
        nick.append(part2);

        //fill with random numbers
        while(nick.length() < Constants.MAX_NICKNAME_LENGTH) {
            nick.append(String.valueOf(Math.abs(r.nextInt())).charAt(0));
        }

        return nick.toString();
    }
}
