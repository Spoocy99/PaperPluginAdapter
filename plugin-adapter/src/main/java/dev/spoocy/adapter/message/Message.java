package dev.spoocy.adapter.message;

import dev.spoocy.adapter.message.color.StyleImpl;
import dev.spoocy.adapter.message.types.*;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.messages.MessageStyle;
import dev.spoocy.adapter.messages.PluginMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */


public class Message {
    public static final int DEFAULT_MAX_CHARS_PER_LINE = 30;
    public static final PluginMessageProcessor PROCESSOR = new PluginMessageProcessor();

    public static final MessageStyle EMPTY_STYLE = style().build();
    public static final PluginMessage EMPTY = new EmptyMessage();
    public static final Component EMPTY_CMP = Component.empty();

    public static MessageStyle.Builder style() {
        return new StyleImpl.BuilderImpl();
    }

    public static PluginMessage emptyMess() {
        return msg(" ");
    }

    public static Component emptyCmp() {
        return Component.text(" ");
    }

    /**
     * Wraps a Component into a {@link PluginMessage}.
     * The Component will be provided as-is when the message is rendered.
     * No additional formatting or processing will be applied.
     *
     * @param component The component to wrap.
     *
     * @return An instance of a Plugin message.
     */
    public static PluginMessage wrap(@NotNull Component component) {
        return new WrappedMessage(component);
    }

    /**
     * Wraps a Function that takes a Localization and returns a
     * Component into a {@link PluginMessage}.
     * The Function will be called with the appropriate Localization
     * when the message is rendered, allowing for dynamic content
     * based on the player's language settings.
     * <p>
     * No additional formatting or processing is possible and related methods
     * will throw {@link UnsupportedOperationException}.
     *
     * @param function The function to wrap.
     *
     * @return An instance of a Plugin message.
     */
    public static PluginMessage wrap(@NotNull Function<Localization, Component> function) {
        return new WrappedMessage(function);
    }

    /**
     * Creates a MiniMessage-based PluginMessage from a message string.
     * MiniMessage formatting codes will be formated.
     * <p>
     * Additional formatting and processing can be applied using the methods
     * provided by the {@link PluginMessage} interface.
     *
     * @param message The MiniMessage-formatted message string.
     *
     * @return An instance of a Plugin message.
     */
    public static PluginMessage msg(@NotNull String message) {
        return new MiniMessage(message);
    }

    public static PluginMessage msgList(@NotNull List<String> messages) {
        return new MinimessageListMessage(messages);
    }

    public static PluginMessage text(@NotNull String text) {
        return text( l -> text);
    }

    public static PluginMessage text(@NotNull String text, @NotNull TextColor color, @NotNull TextDecoration... decorations) {
        return text(l -> text, color, decorations);
    }

    public static PluginMessage text(@NotNull Function<Localization, String> text) {
        return new TextMessage(text);
    }

    public static PluginMessage text(@NotNull Function<Localization, String> text, @NotNull TextColor color, @NotNull TextDecoration... decorations) {
        return new TextMessage(text, color, decorations);
    }

    public static PluginMessage translatable(@NotNull String key) {
        return new TranslatableMessage(key);
    }

    public static PluginMessage translatable(@NotNull String key, @NotNull TextColor color, @NotNull TextDecoration... decorations) {
        return new TranslatableMessage(key, color, decorations);
    }

    public static PluginMessage translatableList(@NotNull String key) {
        return new ListTranslatableMessage(key);
    }

    /*
     * Component methods
     */

    public static Component cmp(@NotNull String message) {
        return PROCESSOR.miniMessageSerializer().deserialize(message);
    }

    public static Component cmp(@NotNull String text, @NotNull TextColor color, @Nullable TextDecoration... decorations) {
        Component cmp = Component.text(text)
                .color(color);

        if(decorations != null && decorations.length > 0) {
            cmp = cmp.decorations(getDecorationsMap(Set.of(decorations)));
        }

        return cmp;
    }

    public static Map<TextDecoration, TextDecoration.State> getDecorationsMap(@NotNull Set<TextDecoration> decorations) {
        final Map<TextDecoration, TextDecoration.State> map = new EnumMap<>(TextDecoration.class);
        for (TextDecoration decoration : TextDecoration.values()) {
                map.put(decoration, decorations.contains(decoration)
                        ? TextDecoration.State.TRUE : TextDecoration.State.FALSE);
        }
        return map;
    }

    public static Map<TextDecoration, TextDecoration.State> decorationsMap(@NotNull TextDecoration.State state) {
        final Map<TextDecoration, TextDecoration.State> map = new EnumMap<>(TextDecoration.class);
        for (TextDecoration decoration : TextDecoration.values()) {
            map.put(decoration, state);
        }
        return map;
    }

    public static String toLegacy(@NotNull Component component) {
        return PROCESSOR.bungeeSerializer().serialize(component);
    }

    public static String toPlainText(@NotNull Component component) {
        return PROCESSOR.plainSerializer().serialize(component);
    }

    public static String toMiniMessageFormat(@NotNull Component component) {
        return PROCESSOR.miniMessageSerializer().serialize(component);
    }

}
