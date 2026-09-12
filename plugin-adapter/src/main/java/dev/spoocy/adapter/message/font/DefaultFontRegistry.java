package dev.spoocy.adapter.message.font;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class DefaultFontRegistry implements FontRegistry {

    private static final Font DEFAULT_MINECRAFT_FONT = Font.DEFAULT;
    private final Map<Key, Font> fonts = new HashMap<>();

    public DefaultFontRegistry() { }

    @Override
    public @Nullable Font getFont(@NotNull Key key) {
        if(DEFAULT_MINECRAFT_FONT.key().equals(key)) {
            // keep default font!
            return DEFAULT_MINECRAFT_FONT;
        }

        return this.fonts.get(key);
    }

    @Override
    public @NotNull Font getFontOrDefault(@NotNull Key key) {
        return this.fonts.getOrDefault(key, DEFAULT_MINECRAFT_FONT);
    }

    @Override
    public void registerFont(@NotNull Font font) {
        Key key = font.key();

        if(this.fonts.containsKey(key) || DEFAULT_MINECRAFT_FONT.key().equals(key)) {
            throw new IllegalArgumentException("Font with key " + key + " already exists");
        }

        this.fonts.put(key, font);
    }

}
