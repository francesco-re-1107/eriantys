package it.polimi.ingsw.client;

/**
 * This class is implemented by every screen controller (both GUI and CLI).
 */
public interface ScreenController {

    /**
     * This method is called when the screen is shown.
     */
    void onShow();

    /**
     * This method is called when the screen is hidden.
     */
    void onHide();

}
