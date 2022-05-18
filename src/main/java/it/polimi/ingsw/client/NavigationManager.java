package it.polimi.ingsw.client;

/**
 * This interface is used to manage the navigation of the client (both GUI and CLI).
 */
public interface NavigationManager {
    /**
     * This method is used to navigate to a new screen with a backstack entry
     * @param destination the screen to navigate to
     */
    void navigateTo(Screen destination);

    /**
     * This method is used to navigate to a new screen
     * @param destination the screen to navigate to
     * @param withBackStack whether this navigation should be added to the backstack
     */
    void navigateTo(Screen destination, boolean withBackStack);

    /**
     * Clear all the backstack entries
     */
    void clearBackStack();

    /**
     * This method is used to go back one screen (with respect to the backstack) if possible
     */
    void goBack();

    /**
     * Exit the application
     */
    void exitApp();

    /**
     * @return the currently shown screen
     */
    Screen getCurrentScreen();
}
