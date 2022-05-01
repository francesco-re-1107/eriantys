package it.polimi.ingsw.client.cli;

public enum ClientState {
    /**
     * Show the welcome screen
     */
    WELCOME,
    /**
     * Ask start player client state.
     */
    ASK_START_PLAYER,
    /**
     * List and join a game.
     */
    JOIN_GAME,
    /**
     * Form to create game
     */
    CREATE_GAME,
    /**
     * Show the lost screen.
     */
    LOST_GAME,
    /**
     * Show the won screen.
     */
    WON_GAME
}
