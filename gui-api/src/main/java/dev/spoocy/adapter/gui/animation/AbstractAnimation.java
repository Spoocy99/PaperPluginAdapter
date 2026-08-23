package dev.spoocy.adapter.gui.animation;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.gui.types.Gui;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractAnimation<G extends Gui> implements Animation<G> {

    private final int delay;

    private BukkitTask task;
    private G gui;
    private List<Consumer<Integer>> onFrame;
    private List<Runnable> onStop;
    private int frame;
    private int noViewerTicks;

    public AbstractAnimation(int delay) {
        if (delay < 1) throw new IllegalArgumentException("Delay must be at least 1 tick");
        this.delay = delay;
        this.onStop = new ArrayList<>();
        this.frame = 0;
        this.noViewerTicks = 0;
    }

    protected G getGui() {
        return gui;
    }

    @Override
    public void setGui(G gui) {
        if(this.gui != null) throw new IllegalStateException("Animation already bound to GUI");
        this.gui = gui;
    }

    @Override
    public Animation<G> onFrame(@NotNull Consumer<Integer> runnable) {
        this.onFrame.add(runnable);
        return this;
    }

    @Override
    public Animation<G> onStop(@NotNull Runnable runnable) {
        this.onStop.add(runnable);
        return this;
    }

    @Override
    public boolean isRunning() {
        return this.task != null;
    }

    @Override
    public void start() {
        if (this.gui == null) throw new IllegalStateException("Animation not bound to GUI");
        if (this.isRunning()) cancelAnimation();

        this.onStart();

        task = Bukkit.getScheduler().runTaskTimer(PluginAdapter.getInstance(), () -> {
            if (this.gui.getViewers().length == 0) {
                this.noViewerTicks++;

                if (this.noViewerTicks >= 3) {
                    stop();
                    return;
                }

            } else {
                this.noViewerTicks = 0;
            }


            playFrame(this.frame++);

            for (Consumer<Integer> consumer : this.onFrame) {
                consumer.accept(this.frame);
            }

        }, this.delay, this.delay);
    }

    protected abstract void onStart();

    protected abstract void onEnd();

    protected abstract void playFrame(int frame);

    @Override
    public void stop() {
        if(!this.isRunning()) return;
        cancelAnimation();
        this.onEnd();

        for (Runnable runnable : this.onStop) {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cancelAnimation() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

}
