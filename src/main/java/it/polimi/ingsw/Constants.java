package it.polimi.ingsw;

import java.util.List;

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
     * Hostname used by default by the client
     */
    public static final String DEFAULT_HOSTNAME = "localhost";

    /**
     * Port number used by default by server and client
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
    public static final int MAX_NICKNAME_LENGTH = 20;
    public static final List<String> NICKNAMES_PART_1 = List.of(
            "Attractive",
            "Bald",
            "Beautiful",
            "Chubby",
            "Clean",
            "Elegant",
            "Fancy",
            "Flabby",
            "Glamorous",
            "Gorgeous",
            "Handsome",
            "Magnificent",
            "Muscular",
            "Plain",
            "Plump",
            "Scruffy",
            "Shapely",
            "Short",
            "Skinny",
            "Stocky",
            "Ugly",
            "Unsightly"
    );

    public static final List<String> NICKNAMES_PART_2 = List.of(
            "Penguin",
            "Pony",
            "Puppy",
            "Raccoon",
            "Rabbit",
            "Rat",
            "Robin",
            "Squirrel",
            "Turtle",
            "Wolf",
            "Zebra",
            "Bear",
            "Cat",
            "Dog",
            "Fox",
            "Horse",
            "Lion",
            "Monkey",
            "Pig",
            "Rabbit",
            "Sheep",
            "Tiger",
            "Zebra"
    );

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
