package dev.spoocy.adapter.message.color;

import dev.spoocy.adapter.core.config.PluginConfig;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class Color {

    public static final char COLOR_CHAR = '\u00A7';

    /*
     * Minecraft Decorations
     */
    public static final TextDecoration
            MAGIC = TextDecoration.OBFUSCATED,
            BOLD = TextDecoration.BOLD,
            STRIKETHROUGH = TextDecoration.STRIKETHROUGH,
            UNDERLINE = TextDecoration.UNDERLINED,
            ITALIC = TextDecoration.ITALIC;

    /*
     * Minecraft names Codes
     */
    public static final TextColor
            BLACK = TextColor.color(0, 0, 0),
            DARK_BLUE = TextColor.color(0, 0, 170),
            DARK_GREEN = TextColor.color(0, 170, 0),
            DARK_AQUA = TextColor.color(0, 170, 170),
            DARK_RED = TextColor.color(170, 0, 0),
            DARK_PURPLE = TextColor.color(170, 0, 170),
            GOLD = TextColor.color(255, 170, 0),
            GRAY = TextColor.color(170, 170, 170),
            DARK_GRAY = TextColor.color(85, 85, 85),
            BLUE = TextColor.color(85, 85, 255),
            GREEN = TextColor.color(85, 255, 85),
            AQUA = TextColor.color(85, 255, 255),
            RED = TextColor.color(255, 85, 85),
            PURPLE = TextColor.color(255, 85, 255),
            YELLOW = TextColor.color(255, 255, 85),
            WHITE = TextColor.color(255, 255, 255);

    /*
     * Other Custom Colors
     */
    public static final TextColor
            ORANGE = TextColor.color(255, 165, 0),
            PINK = TextColor.color(255, 192, 203),
            CYAN = TextColor.color(0, 255, 255),
            MAGENTA = TextColor.color(255, 0, 255),
            LIME = TextColor.color(0, 255, 0);

    /*
     * Light Custom Colors
     */
    public static final TextColor
            LIGHTER_GRAY = TextColor.color(200, 200, 200),
            LIGHT_BLUE = TextColor.color(100, 150, 255),
            LIGHT_GREEN = TextColor.color(150, 255, 150),
            LIGHT_AQUA = TextColor.color(150, 255, 255),
            LIGHT_RED = TextColor.color(255, 150, 150),
            LIGHT_YELLOW = TextColor.color(255, 255, 150),
            LIGHT_PINK = TextColor.color(255, 150, 255),
            LIGHT_ORANGE = TextColor.color(255, 200, 100),
            LIGHT_CYAN = TextColor.color(150, 255, 255),
            LIGHT_MAGENTA = TextColor.color(255, 150, 255),
            LIGHT_LIME = TextColor.color(150, 255, 150);

    /*
     * Dark Custom Colors
     */
    public static final TextColor
            DARKER_GRAY = TextColor.color(50, 50, 50),
            DARKER_BLUE = TextColor.color(0, 50, 150),
            DARKER_GREEN = TextColor.color(0, 150, 0),
            DARKER_AQUA = TextColor.color(0, 150, 150),
            DARKER_RED = TextColor.color(150, 0, 0),
            DARKER_PURPLE = TextColor.color(150, 0, 150),
            DARKER_YELLOW = TextColor.color(150, 150, 0);

    /**
     * Custom Plugin Colors
     * @see PluginConfig
     */
    public static final TextColor
            BASE = processor(() -> PluginConfig.baseColor().value()),
            HIGHLIGHT = processor(() -> PluginConfig.primaryColor().value()),
            ERROR = processor(() -> PluginConfig.errorColor().value());

    public static TextColor fromBukkit(@NotNull ChatColor color) {
        return TextColor.color(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue());
    }

    public static TextColor processor(@NotNull Supplier<Integer> processor) {
        return new ColorProcessor(processor);
    }

    private static class ColorProcessor implements TextColor {

        private final Supplier<Integer> processor;

        private ColorProcessor(@NotNull Supplier<Integer> processor) {
            this.processor = processor;
        }

        @Override
        public int value() {
            return processor.get();
        }
    }

}
