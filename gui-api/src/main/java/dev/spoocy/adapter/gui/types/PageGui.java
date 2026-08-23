package dev.spoocy.adapter.gui.types;

import dev.spoocy.adapter.gui.animation.Animation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface PageGui extends ContentGui, Resetable {

    boolean hasInfinitePages();

    @Nonnegative
    int getCurrentPage();

    int getMaxPage();

    boolean canGoForward();

    boolean canGoBackward();

    void goForward(@Nonnegative int pages);

    void goBackward(@Nonnegative int pages);

    void playAnimation(@NotNull Animation<PageGui> animation);

}
