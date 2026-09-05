package dev.spoocy.adapter.messages;

import dev.spoocy.adapter.messages.font.Font;
import dev.spoocy.adapter.messages.placeholder.Placeholder;
import dev.spoocy.utils.common.tuple.Pair;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Wrapper interface for {@link Style} with
 * additional message styling options for
 * {@link PluginMessage}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public interface MessageStyle {

    @Nullable
    TextColor color();

    @NotNull
    Map<TextDecoration, TextDecoration.State> decorations();

    @Nullable
    Key fontKey();

    @Nullable
    Font font();

    @Nullable
    ClickEvent<?> clickEvent(@NotNull Localization locale);

    @Nullable
    HoverEvent<?> hoverEvent(@NotNull Localization locale);

    @Nullable
    List<Placeholder> placeholders();

    @Nullable
    PluginMessage prefix();

    @Nullable
    PluginMessage suffix();

    boolean upperCase();

    int multipleLinesMaxCharsPerLine();

    @Nullable
    PluginMessage multipleLinesPrefix();

    @Contract("-> new")
    Builder builder();

    interface Builder {

        @Contract("_ -> this")
        @NotNull Builder font(final @Nullable Key font);

        @Contract("_ -> this")
        @NotNull Builder color(@Nullable TextColor color);

        @Contract("_ -> this")
        @NotNull Builder colorIfAbsent(final @Nullable TextColor color);

        @Contract("_ -> this")
        @NotNull Builder decorate(final @NotNull TextDecoration decoration);

        @Contract("_ -> this")
        @NotNull Builder decorate(final @NotNull TextDecoration... decorations);

        @Contract("_, _ -> this")
        @NotNull Builder decoration(final @NotNull TextDecoration decoration, final boolean flag);

        @Contract("_ -> this")
        @NotNull Builder decorations(final @NotNull Map<TextDecoration, TextDecoration.State> decorations);

        @Contract("_, _ -> this")
        @NotNull Builder decoration(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state);

        @Contract("_, _ -> this")
        @NotNull Builder decorationIfAbsent(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state);

        @Contract("_ -> this")
        Builder decorateIfNotNull(@Nullable TextDecoration[] decorations);

        @Contract("_ -> this")
        Builder font(@Nullable Font font);

        @Contract("_ -> this")
        Builder prefix(@Nullable PluginMessageContainer pluginMessage);

        @Contract("_ -> this")
        Builder suffix(@Nullable PluginMessageContainer pluginMessage);

        @Contract("-> this")
        Builder upperCase();

        @Contract("_, _ -> this")
        Builder multipleLines(int maxCharactersPerLine, @Nullable PluginMessageContainer linePrefix);

        @Contract("_ -> this")
        Builder hover(@Nullable HoverEvent<?> event);

        @Contract("_ -> this")
        Builder hover(@Nullable PluginMessageContainer message);

        @Contract("_ -> this")
        Builder click(@Nullable ClickEvent<?> event);

        @Contract("_ -> this")
        @NotNull default Builder openUrl(@Nullable String url) {
            return click(url != null ? ClickEvent.openUrl(url) : null);
        }

        @Contract("_ -> this")
        @NotNull default Builder suggestCommand(@Nullable String command) {
            return click(command != null ? ClickEvent.suggestCommand(command) : null);
        }

        @Contract("_ -> this")
        @NotNull default Builder runCommand(@Nullable String command) {
            return click(command != null ? ClickEvent.runCommand(command) : null);
        }

        @Contract("_, _ -> this")
        @NotNull Builder placeholder(@NotNull @TagPattern String key, @NotNull PluginMessageContainer message);

        @Contract("-> new")
        @NotNull
        MessageStyle build();
    }

}
