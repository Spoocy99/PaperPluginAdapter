package dev.spoocy.adapter.gui.animation;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.log.BukkitLogger;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractAnimation<A extends AbstractAnimation<A, G>, G extends Gui> implements Animation<A, G> {

    private final List<Consumer<Integer>> onFrame = new ArrayList<>();
    private final List<Runnable> onEnd = new ArrayList<>();
    private final int delay;

    private G gui;

    private BukkitTask task;
    private int frame;
    private int noViewerTicks;

    public AbstractAnimation(int delay) {
        if (delay < 1) throw new IllegalArgumentException("Delay must be at least 1 tick");
        this.delay = delay;
        this.frame = 0;
        this.noViewerTicks = 0;
    }

    protected G getGui() {
        return this.gui;
    }

    @Override
    public void setGui(G gui) {
        if (this.gui != null) throw new IllegalStateException("Animation already bound to GUI");
        this.gui = gui;
    }

    @Override
    public A onFrame(@NotNull Consumer<Integer> runnable) {
        this.onFrame.add(runnable);
        return instance();
    }

    @Override
    public A onEnd(@NotNull Runnable runnable) {
        this.onEnd.add(runnable);
        return instance();
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

        task = Bukkit.getScheduler().runTaskTimer(
                PluginAdapter.getInstance(), () -> {
                    if (this.gui.getViewers().length == 0) {
                        this.noViewerTicks++;

                        if (this.noViewerTicks >= 3) {
                            stop();
                            return;
                        }

                    } else {
                        this.noViewerTicks = 0;
                    }


                    if (!playFrame(this.frame++)) {
                        stop();
                        return;
                    }

                    for (Consumer<Integer> consumer : this.onFrame) {
                        consumer.accept(this.frame);
                    }

                }, this.delay, this.delay
        );
    }

    protected abstract void onStart();

    protected abstract void onEnd();

    /**
     * Executes a single frame of the animation.
     *
     * @param frame the index of the frame to be played, starting from 0
     *
     * @return {@code true} if the animation should continue, {@code false} if the animation is finished
     */
    protected abstract boolean playFrame(int frame);

    protected abstract A instance();

    @Override
    public void stop() {
        if (!this.isRunning()) return;
        cancelAnimation();
        this.onEnd();

        for (Runnable runnable : this.onEnd) {
            try {
                runnable.run();
            } catch (Exception e) {
                BukkitLogger.error("Error while stopping animation.", e);
            }
        }
    }

    private void cancelAnimation() {
        this.noViewerTicks = 0;
        this.frame = 0;

        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

}
