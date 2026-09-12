package dev.spoocy.adapter.message.font;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface FontRegistry {

    @Nullable
    Font getFont(@NotNull Key key);

    @NotNull
    Font getFontOrDefault(@NotNull Key key);

    void registerFont(@NotNull Font font);

}
