package dev.spoocy.adapter.message.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Font {

    DefaultFont DEFAULT = new DefaultFont();
    SmallCapsFont SMALL_CAPS = new SmallCapsFont();

    @NotNull
    Key key();

    @NotNull
    Component applyTo(@NotNull Component input);

    int getWidth(int characterCodePoint);

    default int getWidth(char character) {
        return getWidth(String.valueOf(character).codePointAt(0));
    }

}
