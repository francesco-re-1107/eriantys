package it.polimi.ingsw;

/**
 * This class holds all the constants across the project
 */
public final class Constants {

    /**
     * Number of students per color to put the main bag of the game
     */
    public static final int STUDENTS_BAG_NUMBER_PER_COLOR = 120/5;

    /**
     * Number of students per color to put in the bag used when populating island
     */
    public static final int ISLANDS_STUDENTS_BAG_NUMBER_PER_COLOR = 10/5;

    /**
     * Number of character cards selected at the beginning of the game
     */
    public static final int NUMBER_OF_CHARACTER_CARD = 3;

    /**
     * Represents the path for the configuration file used
     */
    public static final String CONFIG_FILE_PATH = "app.config";

    /**
     * Port number used by default by server
     */
    public static final int DEFAULT_SERVER_PORT = 6001;

    /**
     * Interval between two ping requests sent by the client
     */
    public static final long PING_INTERVAL = 2000;

    /**
     * Timeout used to determine if a client is still connected
     * If a ping request (or ping reply) doesn't arrive in this time, the client is considered disconnected
     */
    public static final int DISCONNECTION_TIMEOUT = 8000;
    public static final long GAMES_LIST_REFRESH_INTERVAL = 5000;

    /**
     * These constants are used in a game with 2 players (commented only once)
     */
    public static final class TwoPlayers {

        /**
         * Number of towers given to every player at the beginning of the game
         */
        public static final int TOWERS_COUNT = 8;

        /**
         * Max size of the entrance
         */
        public static final int ENTRANCE_SIZE = 7;

        /**
         * Number of students to put on a cloud
         */
        public static final int STUDENTS_PER_CLOUD = 3;

        /**
         * Number of students that a player must move in its turn
         */
        public static final int STUDENTS_TO_MOVE = 3;

        private TwoPlayers() {}
    }

    /**
     * These constants are used in a game with 3 players
     */
    public static final class ThreePlayers {

        public static final int TOWERS_COUNT = 6;

        public static final int ENTRANCE_SIZE = 9;

        public static final int STUDENTS_PER_CLOUD = 4;

        public static final int STUDENTS_TO_MOVE = 4;

        private ThreePlayers() {}
    }

    private Constants() {}
}
