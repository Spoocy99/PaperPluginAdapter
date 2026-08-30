package dev.spoocy.adapter.gui.types;

import dev.spoocy.adapter.gui.animation.Animation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ListGui extends ContentGui, Resetable {

    @Nonnegative
    int getOffset();

    boolean canScrollUp();

    boolean canScrollDown();

    void scrollUp(@Nonnegative int lines);

    void scrollDown(@Nonnegative int lines);

    void playAnimation(@NotNull Animation<?, ListGui> animation);

}
