package it.polimi.ingsw.client.cli.views;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.fusesource.jansi.Ansi.ansi;

public class ProgressIndicatorView extends BaseView {

    /**
     * Timer used for showing a loading animation
     */
    private Timer loadingAnimationTimer;

    /**
     * X coordinate of the point in which display the loading animation
     */
    private int x;

    /**
     * Y coordinate of the point in which display the loading animation
     */
    private int y;

    private int currentCharacterDisplayed = 0;

    public static final List<String> LOADING_ANIMATION_CHARACTERS = List.of(
            /*"⡿",
            "⣟",
            "⣯",
            "⣷",
            "⣾",
            "⣽",
            "⣻",
            "⢿"*/
            "⠟",
            "⠯",
            "⠷",
            "⠾",
            "⠽",
            "⠻"
    );
    public static final long LOADING_ANIMATION_PERIOD = 100L;

    public ProgressIndicatorView(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw() {
        //do nothing
    }

    /**
     * Start the animation at the given position
     */
    public void startLoading() {
        stopLoading();

        loadingAnimationTimer = new Timer();
        loadingAnimationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cursor.print(ansi()
                                .fgDefault()
                                .bgDefault()
                                .a(LOADING_ANIMATION_CHARACTERS.get(currentCharacterDisplayed))
                                .reset(),
                        x, y
                );
                currentCharacterDisplayed = (currentCharacterDisplayed + 1) % LOADING_ANIMATION_CHARACTERS.size();
            }
        }, 0, LOADING_ANIMATION_PERIOD);
    }

    /**
     * Stop the running animation if any
     */
    public void stopLoading() {
        cursor.clearCell(x, y);

        currentCharacterDisplayed = 0;
        if (loadingAnimationTimer != null) {
            loadingAnimationTimer.cancel();
            loadingAnimationTimer.purge();
        }
        loadingAnimationTimer = null;
    }
}
