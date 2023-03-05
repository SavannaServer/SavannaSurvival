package net.ramuremo.savannasurvival.utility;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public final class SavannaRunnable {
    private static final Set<TimerTask> tasks = new HashSet<>();
    private static final Timer timer = new Timer();
    private final TimerTask timerTask;

    public SavannaRunnable(@Nonnull TimerTask timerTask) {
        this.timerTask = timerTask;
    }

    public static void cancelAll() {
        for (TimerTask task : tasks) {
            task.cancel();
        }
        tasks.clear();
    }

    public void runLater(long delay) {
        timer.schedule(timerTask, delay);
        tasks.add(timerTask);
    }

    public void runTimer(long delay, long period) {
        timer.scheduleAtFixedRate(timerTask, delay, period);
        tasks.add(timerTask);
    }

    public void cancel() {
        timerTask.cancel();
    }
}
