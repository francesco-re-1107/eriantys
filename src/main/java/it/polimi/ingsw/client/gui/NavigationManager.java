package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.Utils;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class NavigationManager {

    private static NavigationManager instance;

    private Stage stage;

    private Scene scene;

    private Stack<Parent> backStack;

    private Map<Screen, Parent> screens;

    private boolean currentlyNavigating = false;

    private NavigationManager(Stage stage) {
        this.stage = stage;
        this.backStack = new Stack<>();
        this.screens = new ConcurrentHashMap<>();

        //load first screen
        screens.put(Screen.MAIN_MENU, loadScreen(Screen.MAIN_MENU));

        //load other screens in background thread
        new Thread(() -> {
            for (Screen s : Arrays.stream(Screen.values()).filter(s -> s != Screen.MAIN_MENU).toList()) {
                screens.put(s, loadScreen(s));
            }
            Utils.LOGGER.info("Finished loading screens");
        }).start();
    }

    private Parent loadScreen(Screen screen) {
        try {
            return FXMLLoader.load(getClass().getResource("/fxml/" + screen.name().toLowerCase() + ".fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            return new Label("Error loading screen " + screen.name() + ": " + e.getMessage());
        }
    }

    public static void init(Stage stage) {
        instance = new NavigationManager(stage);
    }

    public static NavigationManager getInstance() {
        return instance;
    }

    public void navigateTo(Screen screen) {
        navigateTo(screen, true);
    }

    public void navigateTo(Screen screen, boolean withBackStack) {
        if(currentlyNavigating) return;

        var newRoot = screens.get(screen);
        newRoot.setOpacity(1.0);

        if (scene == null) {
            scene = new Scene(newRoot);
            scene.setFill(Paint.valueOf("#000000"));
            stage.setScene(scene);

        } else {
            currentlyNavigating = true;

            var ft = new FadeTransition(new Duration(200), scene.getRoot());
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(event -> {
                if(withBackStack)
                    backStack.push(scene.getRoot());
                scene.setRoot(newRoot);

                currentlyNavigating = false;
            });

            ft.play();
        }

        Utils.LOGGER.info("Navigate to screen " + screen.name());
    }

    public void clearBackStack() {
        backStack.clear();
    }

    public void goBack() {
        if(currentlyNavigating) return;

        if (!backStack.isEmpty()) {
            currentlyNavigating = true;

            scene.setRoot(backStack.peek());

            var ft = new FadeTransition(new Duration(200), scene.getRoot());
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setOnFinished(event -> {
                backStack.pop();
                currentlyNavigating = false;
            });
            ft.play();
        }
        Utils.LOGGER.info("Go back");
    }

    public enum Screen {
        MAIN_MENU,
        GAME_CREATION_MENU,
        GAME_JOINING_MENU,
        WAITING_ROOM,
        GAME
    }
}