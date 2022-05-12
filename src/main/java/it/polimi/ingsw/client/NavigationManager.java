package it.polimi.ingsw.client;

public interface NavigationManager {

    void navigateTo(Screen destination);

    void navigateTo(Screen destination, boolean withBackStack);

    void clearBackStack();

    void goBack();

    void exitApp();
}
