package dev.spoocy.adapter.message.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class DefaultFont implements Font {

    public static final Key KEY = Style.DEFAULT_FONT;

    DefaultFont() {

    }

    @Override
    public @NonNull Key key() {
        return KEY;
    }

    @Override
    public @NonNull Component applyTo(@NotNull Component input) {
        return input;
    }

    @Override
    public int getWidth(int characterCodePoint) {

        if (characterCodePoint == ' ') {
            return 4;
        }

        if (characterCodePoint == 'i'
                || characterCodePoint == '!'
                || characterCodePoint == '.'
                || characterCodePoint == ','
                || characterCodePoint == ':'
        ) {
            return 1;
        }

        if (characterCodePoint == 'I') {
            return 3;
        }

        if (characterCodePoint == 'f' || characterCodePoint == 'k') {
            return 4;
        }

        return 5;
    }
}
