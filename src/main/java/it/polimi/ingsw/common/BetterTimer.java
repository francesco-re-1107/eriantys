package it.polimi.ingsw.common;

import java.util.Timer;
import java.util.TimerTask;

public class BetterTimer extends TimerTask {

    private Timer timer;

    private final Runnable runnable;

    private final long delay;

    public BetterTimer(Runnable runnable, long delay) {
        this.runnable = runnable;
        this.delay = delay;

        timer = new Timer();
    }

    public void start() {
        timer.schedule(this, delay);
    }

    public void restart() {
        timer = new Timer();
        timer.schedule(this, delay);
    }

    @Override
    public void run() {
        runnable.run();
    }

    public void stop() {
        timer.cancel();
        timer.purge();
    }
}
