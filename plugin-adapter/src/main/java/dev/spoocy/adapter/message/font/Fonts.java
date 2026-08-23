package dev.spoocy.adapter.message.font;

import dev.spoocy.adapter.messages.font.Font;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class Fonts {
    public static final Font DEFAULT = input -> input;
    public static final SmallCapsFont SMALL_CAPS = new SmallCapsFont();


    private Fonts() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
