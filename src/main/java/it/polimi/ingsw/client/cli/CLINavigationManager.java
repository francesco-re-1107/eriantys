package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.NavigationManager;
import it.polimi.ingsw.client.Screen;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.controllers.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class manages the navigation for the CLI
 */
public class CLINavigationManager implements NavigationManager {


    /**
     * Screens with their corresponding controllers
     */
    private final Map<Screen, ScreenController> screenControllers;

    /**
     * Backstack used for the inverse navigation
     */
    private final Deque<Screen> backstack;

    /**
     * Currently shown screen
     */
    private Screen currentScreen;

    /**
     * Initializes the navigation manager.
     * Creates alle the screen controllers
     */
    public CLINavigationManager() {
        this.backstack = new ArrayDeque<>();
        this.screenControllers = new ConcurrentHashMap<>();

        //instantiates screen controllers
        screenControllers.put(Screen.SERVER_CONNECTION_MENU, new CLIServerConnectionMenuController());
        screenControllers.put(Screen.MAIN_MENU, new CLIMainMenuScreenController());
        screenControllers.put(Screen.GAME_CREATION_MENU, new CLIGameCreationMenuController());
        screenControllers.put(Screen.GAME_JOINING_MENU, new CLIGameJoiningMenuController());
        screenControllers.put(Screen.WAITING_ROOM, new CLIWaitingRoomController());
        screenControllers.put(Screen.GAME, new CLIGameController());
    }

    @Override
    public void navigateTo(Screen destination) {
        navigateTo(destination, true);
    }

    @Override
    public void navigateTo(Screen destination, boolean withBackStack) {
        if(currentScreen == destination){
            //just refresh the screen
            screenControllers.get(currentScreen).onHide();
            screenControllers.get(currentScreen).onShow();
            return;
        }

        if(withBackStack && currentScreen != null) {
            backstack.push(currentScreen);
        }

        var lastScreen = currentScreen;
        currentScreen = destination;

        //call onHide on the current screen
        if(lastScreen != null)
            screenControllers.get(lastScreen).onHide();

        //call onShow on the new screen
        screenControllers.get(destination).onShow();
    }

    @Override
    public void clearBackStack() {
        backstack.clear();
    }

    @Override
    public void goBack() {
        if(backstack.isEmpty()) return;

        var previousScreen = backstack.pop();

        //call onHide on the current screen
        screenControllers.get(currentScreen).onHide();

        //call onShow on the new screen
        screenControllers.get(previousScreen).onShow();
    }

    @Override
    public void exitApp() {
        System.exit(0);
    }

    @Override
    public Screen getCurrentScreen() {
        return currentScreen;
    }
}
