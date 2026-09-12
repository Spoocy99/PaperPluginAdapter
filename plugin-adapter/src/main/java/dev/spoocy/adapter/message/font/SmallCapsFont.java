package dev.spoocy.adapter.message.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SmallCapsFont implements Font {

    private static final char[] SMALL_CAPS_CHARS = "ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".toCharArray();

    private static final TextReplacementConfig REPLACEMENT_CONFIG = TextReplacementConfig.builder()
            .match(".+?")
            .replacement((match, builder) -> {
                String content = match.group();
                StringBuilder transformed = new StringBuilder();
                for (char c : content.toCharArray()) {
                    transformed.append(toSmallCapsChar(c, c));
                }
                return Component.text(transformed.toString(), builder.build().style());
            }).build();

    SmallCapsFont() {
    }

    public static final Key KEY = Key.key("smallcaps");

    @Override
    public @NotNull Key key() {
        return KEY;
    }

    @Override
    public @NonNull Component applyTo(@NotNull Component input) {
        return input.replaceText(REPLACEMENT_CONFIG);
    }

    @Override
public int getWidth(int characterCodePoint) {
    switch (characterCodePoint) {
        case 'ᴀ':
        case 'ʙ':
        case 'ᴄ':
        case 'ᴅ':
        case 'ᴇ':
        case 'ғ':
        case 'ɢ':
        case 'ʜ':
        case 'ɪ':
        case 'ᴊ':
        case 'ᴋ':
        case 'ʟ':
        case 'ᴍ':
        case 'ɴ':
        case 'ᴏ':
        case 'ᴘ':
        case 'ǫ':
        case 'ʀ':
        case 's':
        case 'ᴛ':
        case 'ᴜ':
        case 'ᴠ':
        case 'ᴡ':
        case 'x':
        case 'ʏ':
        case 'ᴢ':
            return 5;

        default:
            return Font.DEFAULT.getWidth(characterCodePoint);
    }
}

    public static char toSmallCapsChar(char c, char defaultChar) {
        if (c >= 'A' && c <= 'Z') {
            return SMALL_CAPS_CHARS[c - 'A'];
        } else if (c >= 'a' && c <= 'z') {
            return SMALL_CAPS_CHARS[c - 'a'];
        }
        return defaultChar;
    }
}
