package dev.spoocy.adapter.message.style;

import dev.spoocy.adapter.message.MessageLike;
import dev.spoocy.adapter.message.types.Styleable;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.ARGBLike;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Immutable
public interface MessageStyle extends Styleable<MessageStyle> {

    @Contract(pure = true)
    @NotNull
    static MessageStyle empty() {
        return StyleImpl.EMPTY;
    }

    @Contract(pure = true)
    @NotNull
    static MessageStyle noDecorations() {
        return StyleImpl.NO_DECORATIONS;
    }

    @Contract(value = " -> new", pure = true)
    @NotNull
    static Builder builder() {
        return new StyleImpl.BuilderImpl();
    }

    @Contract("_ -> new")
    @NotNull
    static MessageStyle of(@NotNull Style style) {
        return builder()
                .color(style.color())
                .shadowColor(style.shadowColor())
                .decorations(style.decorations())
                .font(style.font())
                .click(style.clickEvent())
                .hover(style.hoverEvent())
                .build();
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    default MessageStyle style(@NotNull Consumer<Builder> consumer) {
        Builder builder = this.asBuilder();
        consumer.accept(builder);
        return builder.build();
    }

    @Nullable
    TextColor color();

    @Nullable
    ARGBLike shadowColor();

    @NotNull
    Map<TextDecoration, TextDecoration.State> decorations();

    @Nullable
    Key font();

    @Nullable
    ClickEvent<?> clickEvent();

    @Nullable
    HoverEvent<?> hoverEvent();

    @Nullable
    MessageLike prefix();

    @Nullable
    MessageLike suffix();

    boolean isUpperCase();

    boolean isInheritStyle();

    default boolean shouldSplitAtMaxChars() {
        return multipleLinesMaxCharsPerLine() > 0;
    }

    int multipleLinesMaxCharsPerLine();

    @Nullable
    ComponentLike multipleLinesPrefix();

    @Contract(" -> new")
    @NotNull
    Builder asBuilder();

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle color(@Nullable TextColor color) {
        return style(b -> b.color(color));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle colorIfAbsent(@Nullable TextColor color) {
        return style(b -> b.colorIfAbsent(color));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle shadowColor(@Nullable ARGBLike argb) {
        return style(b -> b.shadowColor(argb));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle shadowColorIfAbsent(@Nullable ARGBLike argb) {
        return style(b -> b.shadowColorIfAbsent(argb));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle decorate(@NotNull TextDecoration decoration) {
        return Styleable.super.decorate(decoration);
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle decorate(@NotNull TextDecoration... decorations) {
        return Styleable.super.decorate(decorations);
    }

    @CheckReturnValue
    @Contract("_, _  -> new")
    @Override
    @NotNull
    default MessageStyle decoration(@NotNull TextDecoration decoration, boolean flag) {
        return Styleable.super.decoration(decoration, flag);
    }

    @CheckReturnValue
    @Contract("_, _  -> new")
    @Override
    @NotNull
    default MessageStyle decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
        return this.style(b -> b.decoration(decoration, state));
    }

    @CheckReturnValue
    @Contract("_, _  -> new")
    @Override
    @NotNull
    default MessageStyle decorationIfAbsent(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
        return this.style(b -> b.decorationIfAbsent(decoration, state));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle decorations(@Nullable Map<TextDecoration, TextDecoration.State> decorations) {
        return this.style(b -> b.decorations(decorations));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle click(@Nullable ClickEvent<?> event) {
        return style(b -> b.click(event));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle clickIfAbsent(@Nullable ClickEvent<?> event) {
        return style(b -> b.clickIfAbsent(event));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle hover(@Nullable HoverEvent<?> event) {
        return style(b -> b.hover(event));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle hoverIfAbsent(@Nullable HoverEvent<?> event) {
        return style(b -> b.hoverIfAbsent(event));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle font(@Nullable Key font) {
        return this.style(b -> b.font(font));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle prefix(@Nullable MessageLike message) {
        return style(b -> b.prefix(message));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle append(@Nullable MessageLike message) {
        return style(b -> b.append(message));
    }

    @CheckReturnValue
    @Contract("_, _ -> new")
    @Override
    @NotNull
    default MessageStyle multipleLines(int maxPixelsPerLine, @Nullable ComponentLike linePrefix) {
        return style(b -> b.multipleLines(maxPixelsPerLine, linePrefix));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle upperCase(boolean upperCase) {
        return style(b -> b.upperCase(upperCase));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default MessageStyle inheritStyle(boolean inherit) {
        return style(b -> b.inheritStyle(inherit));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    default MessageStyle merge(@NotNull Style componentStyle) {
        return style(b -> {
            b
                    .colorIfAbsent(componentStyle.color())
                    .shadowColorIfAbsent(componentStyle.shadowColor())
                    .clickIfAbsent(componentStyle.clickEvent())
                    .hoverIfAbsent(componentStyle.hoverEvent())
            ;

            for(Map.Entry<TextDecoration, TextDecoration.State> entry : componentStyle.decorations().entrySet()) {
                b.decorationIfAbsent(entry.getKey(), entry.getValue());
            }

        });
    }

    interface Builder extends Styleable<Builder> {

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder color(@Nullable TextColor color);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder colorIfAbsent(@Nullable TextColor color);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder shadowColor(@Nullable ARGBLike argb);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder shadowColorIfAbsent(@Nullable ARGBLike argb);

        @Contract("_ -> this")
        @Override
        @NotNull
        default Builder decorate(@NotNull TextDecoration... decorations) {
            return Styleable.super.decorate(decorations);
        }

        @Contract("_ -> this")
        @Override
        @NotNull
        default Builder decorate(@NotNull TextDecoration decoration) {
            return Styleable.super.decorate(decoration);
        }

        @Contract("_, _ -> this")
        @Override
        @NotNull
        default Builder decoration(@NotNull TextDecoration decoration, boolean flag) {
            return Styleable.super.decoration(decoration, flag);
        }

        @Contract("_, _ -> this")
        @Override
        @NotNull
        Builder decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state);

        @Contract("_, _ -> this")
        @Override
        @NotNull
        Builder decorationIfAbsent(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder decorations(@Nullable Map<TextDecoration, TextDecoration.State> decorations);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder hover(@Nullable HoverEvent<?> event);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder hoverIfAbsent(@Nullable HoverEvent<?> event);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder click(@Nullable ClickEvent<?> event);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder clickIfAbsent(@Nullable ClickEvent<?> event);

        @Contract("_ -> this")
        @Override
        @NonNull
        Builder font(@Nullable Key font);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder prefix(@Nullable MessageLike message);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder append(@Nullable MessageLike message);

        @Contract("_ -> this")
        @Override
        @NotNull
        Builder upperCase(boolean upperCase);

        @Contract("_, _ -> this")
        @Override
        @NotNull
        Builder multipleLines(int maxPixelsPerLine, @Nullable ComponentLike linePrefix);

        @Contract("_, _ -> this")
        @Override
        @NotNull
        Builder inheritStyle(boolean inherit);

        @Contract(value = " -> new")
        @NotNull
        MessageStyle build();
    }

}
