package dev.spoocy.adapter.message.font;

import dev.spoocy.adapter.messages.font.Font;
import dev.spoocy.utils.common.text.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SmallCapsFont implements Font {

    private static final char[] SMALL_CAPS_CHARS = "ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".toCharArray();

    public static char toSmallCapsChar(char c, char defaultChar) {
        if (c >= 'A' && c <= 'Z') {
            return SMALL_CAPS_CHARS[c - 'A'];
        } else if (c >= 'a' && c <= 'z') {
            return SMALL_CAPS_CHARS[c - 'a'];
        }
        return defaultChar;
    }

    public static String toSmallCaps(@Nullable String text, final boolean keepMiniMessage, final boolean keepColors) {

        if (StringUtils.isNullOrEmpty(text)) {
            return text;
        }

        StringBuilder builder = new StringBuilder();

        boolean color = false;
        int miniMessage = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (keepColors) {

                if (color) {
                    builder.append(c);
                    color = false;
                }

                if (c == '&' || c == '§') {
                    color = true;
                    builder.append(c);
                    continue;
                }
            }

            if (keepMiniMessage) {

                if (miniMessage > 0) {
                    builder.append(c);
                }

                if (c == '<') {
                    miniMessage++;
                    builder.append(c);
                    continue;
                }

                if (c == '>') {
                    miniMessage--;
                    builder.append(c);
                    continue;
                }
            }

            builder.append(toSmallCapsChar(c, c));

        }
        return builder.toString();
    }

    private static final TextReplacementConfig smallCapsConfig = TextReplacementConfig.builder()
            .match(".+?")
            .replacement((match, builder) -> {
                String content = match.group();
                StringBuilder transformed = new StringBuilder();
                for (char c : content.toCharArray()) {
                    transformed.append(toSmallCapsChar(c, c));
                }
                return Component.text(transformed.toString(), builder.build().style());
            }).build();

    @Override
    public Component applyTo(@NotNull Component input) {
        return input.replaceText(smallCapsConfig);
    }

    SmallCapsFont() { }
}
