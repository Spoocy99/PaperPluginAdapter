package dev.spoocy.adapter.gui.types;

import dev.spoocy.adapter.gui.animation.Animation;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface SimpleGui extends ContentGui {

    void playAnimation(@NotNull Animation<SimpleGui> animation);

}
