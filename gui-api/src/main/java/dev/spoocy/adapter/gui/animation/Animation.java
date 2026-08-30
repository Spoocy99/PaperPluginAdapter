package dev.spoocy.adapter.gui.animation;

import dev.spoocy.adapter.gui.types.Gui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Animation<A extends Animation<A, G>, G extends Gui> {

    @Contract("_ -> new")
    static @NonNull PageSequentialContentAnimation PAGE_SEQUENTIAL_CONTENT_REVEAL(int ticksPerFrame) {
        return new PageSequentialContentAnimation(ticksPerFrame);
    }

    void setGui(G gui);

    boolean isRunning();

    void start();

    void stop();

    A onFrame(@NotNull Consumer<Integer> runnable);

    A onEnd(@NotNull Runnable runnable);

}
