package dev.spoocy.adapter.messages;

import dev.spoocy.adapter.messages.font.Font;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@link PluginMessage}s are immutable components that can be styled
 * and modified to create rich text messages for plugins.
 * <p>
 * When editing a {@link PluginMessage}, a new instance is returned
 * with the applied changes, leaving the original instance unchanged.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface PluginMessage extends SerializableComponent, PluginMessageContainer {

    @Override
    default PluginMessage getPluginMessage() {
        return this;
    }

    /**
     * Applies the specified {@link MessageStyle} to the message.
     *
     * @param style The style to apply.
     *
     * @return A new {@link PluginMessage} instance with the applied style.
     */
    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage style(@NotNull MessageStyle style);

    /**
     * Sets the color of the message.
     *
     * @param color The {@link TextColor} to set.
     *
     * @return A new {@link PluginMessage} instance with the specified color.
     */
    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage color(@NotNull TextColor color);

    /**
     * Adds the specified {@link TextDecoration}(s) to the message.
     *
     * @param decoration  The first {@link TextDecoration} to add.
     * @param decorations Additional {@link TextDecoration}(s) to add.
     *
     * @return A new {@link PluginMessage} instance with the added decorations.
     */
    @CheckReturnValue
    @Contract("_, _ -> new")
    PluginMessage decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration... decorations);

    /**
     * Sets the font of the message.
     *
     * @param font The {@link Font} to set.
     *
     * @return A new {@link PluginMessage} instance with the specified font.
     */
    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage font(@NotNull Font font);

    /**
     * Prefixes the message with the specified {@link PluginMessageContainer}.
     *
     * @param pluginMessage The {@link PluginMessageContainer} to prefix.
     *
     * @return A new {@link PluginMessage} instance with the prefixed message.
     */
    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage prefix(@NotNull PluginMessageContainer pluginMessage);

    /**
     * Appends the specified {@link PluginMessageContainer} to the message.
     *
     * @param pluginMessage The {@link PluginMessageContainer} to append.
     *
     * @return A new {@link PluginMessage} instance with the appended message.
     */
    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage append(@NotNull PluginMessageContainer pluginMessage);

    @CheckReturnValue
    @Contract(" -> new")
    PluginMessage upperCase();

    @CheckReturnValue
    @Contract(" -> new")
    PluginMessage multipleLines();

    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage multipleLines(@Nullable PluginMessageContainer linePrefix);

    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage multipleLines(int maxCharactersPerLine);

    @CheckReturnValue
    @Contract("_, _ -> new")
    PluginMessage multipleLines(int maxCharactersPerLine, @Nullable PluginMessageContainer linePrefix);

    @CheckReturnValue
    @Contract("_, _ -> new")
    PluginMessage multipleLinesC(int maxCharactersPerLine, @Nullable Component linePrefix);

    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage hover(@NotNull HoverEvent<?> event);

    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage hover(@NotNull PluginMessageContainer message);

    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage click(@NotNull ClickEvent<?> event);

    @CheckReturnValue
    @Contract("_ -> new")
    default PluginMessage openUrl(@NotNull String url) {
        return click(ClickEvent.openUrl(url));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    default PluginMessage suggestCommand(@NotNull String command) {
        return click(ClickEvent.suggestCommand(command));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    default PluginMessage runCommand(@NotNull String command) {
        return click(ClickEvent.runCommand(command));
    }


    /*
     * Methods for args
     */

    @CheckReturnValue
    @Contract("_ -> new")
    PluginMessage args(@NotNull PluginMessageContainer... message);

    @CheckReturnValue
    @Contract("_, _ -> new")
    PluginMessage arg(@NotNull @TagPattern String key, @NotNull PluginMessageContainer message);

    @CheckReturnValue
    @Contract("_, _, _, _ -> new")
    default PluginMessage arg(@NotNull @TagPattern String key, @NotNull PluginMessageContainer message, @NotNull TextColor color, @NotNull TextDecoration... decorations) {
        PluginMessage arg = message.getPluginMessage().color(color);
        if(decorations.length != 0) {
            return arg.decoration(decorations[0], decorations.length > 1 ? java.util.Arrays.copyOfRange(decorations, 1, decorations.length) : new TextDecoration[0]);
        }
        return arg(key, arg);
    }

    @CheckReturnValue
    @Contract("_, _ -> new")
    PluginMessage textArg(@NotNull @TagPattern String key, @NotNull String text);

    @CheckReturnValue
    @Contract("_, _, _, _ -> new")
    PluginMessage textArg(@NotNull @TagPattern String key, @NotNull String text, @NotNull TextColor color, @NotNull TextDecoration... decorations);

    @CheckReturnValue
    @Contract("_, _ -> new")
    PluginMessage componentArg(@NotNull @TagPattern String key, @NotNull Component component);

    @CheckReturnValue
    @Contract("_, _ -> new")
    PluginMessage minimessageArg(@NotNull @TagPattern String key, @NotNull String minimessage);

    @CheckReturnValue
    @Contract("_, _ -> new")
    PluginMessage translatableArg(@NotNull @TagPattern String key, @NotNull String translationKey);

    @CheckReturnValue
    @Contract("_, _, _, _ -> new")
   PluginMessage translatableArg(@NotNull @TagPattern String key, @NotNull String translationKey, @NotNull TextColor color, @NotNull TextDecoration... decorations);

}
