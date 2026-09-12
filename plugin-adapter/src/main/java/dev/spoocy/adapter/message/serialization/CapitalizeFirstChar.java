package dev.spoocy.adapter.message.serialization;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class CapitalizeFirstChar {

    private static final TextReplacementConfig REPLACEMENT_CONFIG = TextReplacementConfig.builder()
            .once()
            .match(".??[A-Za-z]")
            .replacement((match, builder) -> {

                String content = builder.content();
                String capitalized = Character.toUpperCase(content.charAt(0)) + content.substring(1);
                builder.content(capitalized);

                return builder.build();
            }).build();

    @Contract(pure = true)
    public static @NotNull Component apply(@NotNull Component component) {
        return component.replaceText(REPLACEMENT_CONFIG);
    }

}
