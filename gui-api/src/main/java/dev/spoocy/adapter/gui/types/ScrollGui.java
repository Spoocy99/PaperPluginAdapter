package dev.spoocy.adapter.gui.types;

import dev.spoocy.adapter.gui.animation.Animation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ScrollGui extends ContentGui, Resetable {

    @Nonnegative
    int getOffset();

    int getItemCount();

    boolean canScrollForward();

    boolean canScrollBackward();

    void scrollForward(@Nonnegative int amount);

    void scrollBackward(@Nonnegative int amount);

    void playAnimation(@NotNull Animation<?, ScrollGui> animation);

}
