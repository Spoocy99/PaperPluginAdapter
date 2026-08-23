package dev.spoocy.adapter.gui.animation;

import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.types.PageGui;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Animation<G extends Gui> {

    static Animation<PageGui> PAGE_SEQUENTIAL_CONTENT_REVEAL(int ticksPerFrame) {
        return new PageSequentialContentAnimation(ticksPerFrame);
    }

    void setGui(G gui);

    boolean isRunning();

    void start();

    void stop();

    Animation<G> onFrame(@NotNull Consumer<Integer> runnable);

    Animation<G> onStop(@NotNull Runnable runnable);
}
