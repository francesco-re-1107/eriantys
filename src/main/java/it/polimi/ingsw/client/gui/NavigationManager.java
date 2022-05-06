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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NavigationManager {

    private static NavigationManager instance;

    private Stage stage;

    private Scene scene;

    private Stack<BackstackEntry> backstack;

    private Map<Screen, Parent> screens;

    private boolean currentlyNavigating = false;

    private List<ScreenChangedListener> listeners;

    private Screen currentScreen;

    private NavigationManager(Stage stage) {
        this.stage = stage;
        this.backstack = new Stack<>();
        this.screens = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();

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

            currentScreen = screen;
            notifyListeners();
        } else {
            currentlyNavigating = true;

            var ft = new FadeTransition(new Duration(200), scene.getRoot());
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(event -> {
                if(withBackStack)
                    backstack.push(new BackstackEntry(screen, scene.getRoot()));
                scene.setRoot(newRoot);

                currentScreen = screen;
                notifyListeners();
                currentlyNavigating = false;
            });

            ft.play();
        }

        Utils.LOGGER.info("Navigate to screen " + screen.name());
    }

    public void clearBackStack() {
        backstack.clear();
    }

    public void goBack() {
        if(currentlyNavigating) return;

        if (!backstack.isEmpty()) {
            currentlyNavigating = true;

            scene.setRoot(backstack.peek().root());

            var ft = new FadeTransition(new Duration(200), scene.getRoot());
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setOnFinished(event -> {
                currentScreen = backstack.peek().screen();

                backstack.pop();
                currentlyNavigating = false;

                notifyListeners();
            });
            ft.play();
        }
        Utils.LOGGER.info("Go back");
    }

    private void notifyListeners() {
        listeners.forEach(l -> l.onScreenChanged(currentScreen));
    }

    public void addOnScreenChangedListener(ScreenChangedListener listener) {
        this.listeners.add(listener);
    }

    public void removeOnScreenChangedListener(ScreenChangedListener listener) {
        this.listeners.remove(listener);
    }

    private record BackstackEntry(Screen screen, Parent root) { }

    public interface ScreenChangedListener {
        void onScreenChanged(Screen screen);
    }

    public enum Screen {
        MAIN_MENU,
        GAME_CREATION_MENU,
        GAME_JOINING_MENU,
        WAITING_ROOM,
        GAME
    }
}