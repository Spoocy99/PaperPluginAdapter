package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.message.MessageLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.ARGBLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Styleable<T extends Styleable<T>> {

    /**
     * Sets the {@link TextColor}.
     *
     * @param color The color.
     *
     * @return an object ({@code T})
     */
    T color(@Nullable TextColor color);

    /**
     * Sets the {@link TextColor} if none is set.
     *
     * @param color The color.
     *
     * @return an object ({@code T})
     */
    T colorIfAbsent(@Nullable TextColor color);

    /**
     * Sets the shadow color.
     * <p>
     * Setting the color to {@code null} will reset the shadow color to default.
     * To remove the shadow entirely, use {@link ShadowColor#none()}.
     *
     * @param argb The color.
     *
     * @return an object ({@code T})
     */
    T shadowColor(@Nullable ARGBLike argb);

    /**
     * Sets the shadow color if none is set.
     * <p>
     * Setting the color to {@code null} will reset the shadow color to default.
     * To remove the shadow entirely, use {@link ShadowColor#none()}.
     *
     * @param argb The color.
     *
     * @return an object ({@code T})
     */
    T shadowColorIfAbsent(@Nullable ARGBLike argb);

    /**
     * Sets the state of {@code decoration} to {@link TextDecoration.State#TRUE}.
     *
     * @param decoration The decoration.
     *
     * @return an object ({@code T})
     */
    default T decorate(@NotNull TextDecoration decoration) {
        return this.decoration(decoration, TextDecoration.State.TRUE);
    }

    /**
     * Sets {@code decorations} to {@link TextDecoration.State#TRUE}.
     *
     * @param decorations The decorations.
     *
     * @return an object ({@code T})
     */
    default T decorate(@NotNull TextDecoration... decorations) {
        Map<TextDecoration, TextDecoration.State> map = new EnumMap<>(TextDecoration.class);

        for (TextDecoration decoration : decorations) {
            map.put(decoration, TextDecoration.State.TRUE);
        }

        return this.decorations(map);
    }

    /**
     * Sets the value of a {@code decoration}.
     *
     * @param decoration The decoration.
     * @param flag The state.
     *
     * @return an object ({@code T})
     */
    default T decoration(@NotNull TextDecoration decoration, boolean flag) {
        return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
    }

    /**
     * Sets the value of a {@code decoration}.
     *
     * @param decoration The decoration.
     * @param state The state.
     *
     * @return an object ({@code T})
     */
    T decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state);

    /**
     * Sets the state of a {@code decoration} to {@link TextDecoration.State#TRUE} if none is set.
     *
     * @param decoration The decoration.
     * @param state      The state,
     *
     * @return an object ({@code T})
     */
    T decorationIfAbsent(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state);

    /**
     * Sets the {@code decorations}.
     *
     * @param decorations The decorations.
     *
     * @return an object ({@code T})
     */
    T decorations(@Nullable Map<TextDecoration, TextDecoration.State> decorations);

    /**
     * Sets the {@link HoverEvent}.
     *
     * @param event The event.
     *
     * @return an object ({@code T})
     */
    T hover(@Nullable HoverEvent<?> event);

    /**
     * Sets the {@link HoverEvent} if none is set.
     *
     * @param event The event.
     *
     * @return an object ({@code T})
     */
    T hoverIfAbsent(@Nullable HoverEvent<?> event);

    /**
     * Sets the {@link ClickEvent}.
     *
     * @param event The event.
     *
     * @return an object ({@code T})
     */
    T click(@Nullable ClickEvent<?> event);

    /**
     * Sets the {@link ClickEvent} if none is set.
     *
     * @param event The event.
     *
     * @return an object ({@code T})
     */
    T clickIfAbsent(@Nullable ClickEvent<?> event);

    /**
     * Sets the Font.
     *
     * @param font The font key.
     *
     * @return an object ({@code T})
     */
    T font(@Nullable Key font);

    /**
     * Adds a Prefix {@link MessageLike}.
     *
     * @param message The message.
     *
     * @return an object ({@code T})
     */
    T prefix(@Nullable MessageLike message);

    /**
     * Adds a Suffix {@link MessageLike}.
     *
     * @param message The message.
     *
     * @return an object ({@code T})
     */
    T append(@Nullable MessageLike message);

    /**
     * Sets uppercase.
     *
     * @param upperCase Whether to uppercase.
     *
     * @return an object ({@code T})
     */
    T upperCase(boolean upperCase);

    /**
     * Sets wrap config.
     *
     * @param maxPixelsPerLine The maximum pixels per line.
     * @param linePrefix The line prefix.
     *
     * @return an object ({@code T})
     */
    T multipleLines(int maxPixelsPerLine, @Nullable ComponentLike linePrefix);

    /**
     * Sets if it is possible to inherit style from a parent.
     *
     * @param inherit Whether to inherit style.
     *
     * @return an object ({@code T})
     */
    T inheritStyle(boolean inherit);

}
