package it.polimi.ingsw.client.cli;

/**
 * This class contains all the characters used to draw the CLI
 */
public class DrawingCharacters {

    /**
     * Empty space, used for example to paint backgrounds
     */
    public static final String EMPTY = " ";

    /**
     * This character is used to draw a generic student
     */
    public static final String STUDENT = "●";

    /**
     * This character is used to draw a generic professor
     */
    public static final String PROFESSOR = "■";

    /**
     * This character is used to draw a generic tower
     */
    public static final String TOWER = "♜";

    /**
     * This character is used to draw mother nature
     */
    public static final String MOTHER_NATURE = "♟";

    /**
     * This character is used for no entry on islands
     * WARNING: this character is actually 2 characters long
     */
    public static final String NO_ENTRY = "⛔";

    /**
     * This character is used as indicator for the current player
     */
    public static final String CURRENT_PLAYER = "➤";

    /**
     * This character is used for coins
     */
    public static final String COIN = "$";

    /**
     * Islands drawing characters
     */
    public static final String ISLAND_CORNER_TOP_LEFT = "╭";
    public static final String ISLAND_CORNER_TOP_RIGHT = "╮";
    public static final String ISLAND_CORNER_BOTTOM_LEFT = "╰";
    public static final String ISLAND_CORNER_BOTTOM_RIGHT = "╯";
    public static final String ISLAND_BORDER_HORIZONTAL = "─";
    public static final String ISLAND_BORDER_VERTICAL = "│";


    private DrawingCharacters() {}
}
