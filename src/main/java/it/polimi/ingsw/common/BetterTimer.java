package it.polimi.ingsw.common;

import java.util.Timer;
import java.util.TimerTask;

public class BetterTimer {

    private Timer timer;

    private final Runnable runnable;

    private final long delay;

    public BetterTimer(Runnable runnable, long delay) {
        this.runnable = runnable;
        this.delay = delay;

        timer = new Timer();
        start();
    }

    public void restart() {
        stop();
        timer = new Timer();
        start();
    }

    public void stop() {
        timer.cancel();
        timer.purge();
    }

    public void start(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, delay);
    }
}
