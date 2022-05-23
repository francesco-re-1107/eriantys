package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Character;

import java.util.List;
import java.util.Map;

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
    public static final String CONFIG_FILE_PATH = "server.config";


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

    /**
     * Interval between two auto-saves of the server games
     */
    public static final long AUTO_SAVE_INTERVAL = 10000;

    /**
     * Interval between two refresh of the client lsit of games
     */
    public static final long GAMES_LIST_REFRESH_INTERVAL = 3000;

    /**
     * Max length of the nickname to be valid
     */
    public static final int MAX_NICKNAME_LENGTH = 20;

    /**
     * Default configuration file used
     */
    public static final String SERVER_CONFIG_FILE_NAME = "server.config";

    /**
     * Adjectives words used to create random nicknames
     */
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

    /**
     * Animals words used to create random nicknames
     */
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

    public static final Map<Character, String> CHARACTER_NAMES = Map.ofEntries(
            Map.entry(Character.CENTAUR, "Centauro"),
            Map.entry(Character.FARMER, "Contadino"),
            Map.entry(Character.KNIGHT, "Cavaliere"),
            Map.entry(Character.GRANDMA, "Nonna"),
            Map.entry(Character.POSTMAN, "Postino"),
            Map.entry(Character.HERALD, "Araldo"),
            Map.entry(Character.MINSTREL, "Menestrello"),
            Map.entry(Character.MUSHROOM_MAN, "Fungaro")
    );

    public static final Map<Character, String> CHARACTER_DESCRIPTIONS = Map.ofEntries(
            Map.entry(Character.CENTAUR, "Le torri non vengono considerate nel calcolo dell'influenza."),
            Map.entry(Character.FARMER, "Prendi il controllo dei professori anche se hai lo stesso numero di studenti di altri giocatori."),
            Map.entry(Character.KNIGHT, "Ricevi 2 punti addizionali nel calcolo dell'influenza."),
            Map.entry(Character.GRANDMA, "Puoi porre un divieto su un'isola a tua scelta."),
            Map.entry(Character.POSTMAN, "Madre natura potrà spostarsi di 2 punti addizionali."),
            Map.entry(Character.HERALD, "Puoi calcolare l'influenza su un'isola a tua scelta."),
            Map.entry(Character.MINSTREL, "Puoi scambiare 2 studenti della sala con 2 studenti dell'entrata."),
            Map.entry(Character.MUSHROOM_MAN, "Puoi selezionare un colore di uno studente, quest'ultimo non influirà nel calcolo dell'influenza.")
    );

    /**
     * Default folder used by the server to store backups
     */
    public static final String DEFAULT_BACKUP_FOLDER = "";

    /**
     * Polling interval used to check if there's something to read from input.
     * DISCLAIMER: Reading from input with polling is the only way to be able to interrupt the reading process.
     * e.g. if an update from the server is received (for example the game is gone into PAUSED state) while the user is
     * writing to input, then the input could be interrupted.
     */
    public static final long CLI_READ_POLLING_INTERVAL = 100;

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
