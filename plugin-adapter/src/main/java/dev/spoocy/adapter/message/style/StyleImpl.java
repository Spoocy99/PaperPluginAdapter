package dev.spoocy.adapter.message.style;

import dev.spoocy.adapter.message.MessageLike;
import dev.spoocy.utils.common.misc.Args;
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
import org.jspecify.annotations.NonNull;

import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class StyleImpl implements MessageStyle {

    public static MessageStyle EMPTY = new BuilderImpl().build();

    public static MessageStyle NO_DECORATIONS = new BuilderImpl()
            .decorations(DecorationMap.none())
            .build();

    @Nullable
    private final TextColor color;
    @Nullable
    private final ShadowColor shadowColor;
    @NotNull
    private final DecorationMap decorations;
    @Nullable
    private final ClickEvent<?> clickEvent;
    @Nullable
    private final HoverEvent<?> hoverEvent;
    @Nullable
    private final Key fontKey;
    @Nullable
    private final MessageLike prefix;
    @Nullable
    private final MessageLike suffix;
    private final int multipleLinesMaxCharsPerLine;
    @Nullable
    private final ComponentLike linePrefix;
    private final boolean upperCase;
    private final boolean inheritStyle;

    private StyleImpl(
            @Nullable TextColor color,
            @Nullable ShadowColor shadowColor,
            @NotNull DecorationMap decorations,
            @Nullable ClickEvent<?> clickEvent,
            @Nullable HoverEvent<?> hoverEvent,
            @Nullable Key fontKey,
            @Nullable MessageLike prefix,
            @Nullable MessageLike suffix,
            int multipleLinesMaxCharsPerLine,
            @Nullable ComponentLike linePrefix,
            boolean upperCase,
            boolean inheritStyle
    ) {
        this.color = color;
        this.shadowColor = shadowColor;
        this.decorations = decorations;
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
        this.fontKey = fontKey;
        this.prefix = prefix;
        this.suffix = suffix;
        this.multipleLinesMaxCharsPerLine = multipleLinesMaxCharsPerLine;
        this.linePrefix = linePrefix;
        this.upperCase = upperCase;
        this.inheritStyle = inheritStyle;
    }

    @Override
    public @Nullable TextColor color() {
        return this.color;
    }

    @Override
    public @Nullable ShadowColor shadowColor() {
        return this.shadowColor;
    }

    @Override
    public @NotNull Map<TextDecoration, TextDecoration.State> decorations() {
        return this.decorations;
    }

    @Override
    public @Nullable Key font() {
        return this.fontKey;
    }

    @Override
    public @Nullable ClickEvent<?> clickEvent() {
        return this.clickEvent;
    }

    @Override
    public @Nullable HoverEvent<?> hoverEvent() {
        return this.hoverEvent;
    }

    @Override
    public @Nullable MessageLike prefix() {
        return this.prefix;
    }

    @Override
    public @Nullable MessageLike suffix() {
        return this.suffix;
    }

    @Override
    public int multipleLinesMaxCharsPerLine() {
        return this.multipleLinesMaxCharsPerLine;
    }

    @Override
    public @Nullable ComponentLike multipleLinesPrefix() {
        return this.linePrefix;
    }

    @Override
    public boolean isUpperCase() {
        return this.upperCase;
    }

    @Override
    public boolean isInheritStyle() {
        return this.inheritStyle;
    }

    @Override
    public @NonNull Builder asBuilder() {
        return new BuilderImpl(this);
    }

    public static class BuilderImpl implements MessageStyle.Builder {

        @Nullable
        private TextColor color;
        @Nullable
        private ShadowColor shadowColor;
        @NotNull
        private DecorationMap decorations;
        @Nullable
        private ClickEvent<?> clickEvent;
        @Nullable
        private HoverEvent<?> hoverEvent;
        @Nullable
        private Key fontKey;
        @Nullable
        private MessageLike prefix;
        @Nullable
        private MessageLike suffix;
        private int multipleLinesMaxCharsPerLine;
        @Nullable
        private ComponentLike linePrefix;

        private boolean upperCase;
        private boolean inheritStyle;


        public BuilderImpl() {
            this.decorations = DecorationMap.empty();
            this.upperCase = false;
            this.inheritStyle = true;
        }

        public BuilderImpl(@NotNull StyleImpl style) {
            this.color = style.color;
            this.shadowColor = style.shadowColor;
            this.decorations = style.decorations;
            this.fontKey = style.fontKey;
            this.prefix = style.prefix;
            this.suffix = style.suffix;
            this.upperCase = style.upperCase;
            this.multipleLinesMaxCharsPerLine = style.multipleLinesMaxCharsPerLine;
            this.linePrefix = style.linePrefix;
            this.hoverEvent = style.hoverEvent;
            this.clickEvent = style.clickEvent;
            this.inheritStyle = style.inheritStyle;
        }


        @Override
        public @NotNull Builder color(@Nullable TextColor color) {
            this.color = color;
            return this;
        }

        @Override
        public @NotNull Builder colorIfAbsent(@Nullable TextColor color) {
            if (this.color == null) {
                this.color = color;
            }
            return this;
        }

        @Override
        public @NotNull Builder shadowColor(@Nullable ARGBLike argb) {
            this.shadowColor = argb == null ? null : ShadowColor.shadowColor(argb);
            return this;
        }

        @Override
        public @NotNull Builder shadowColorIfAbsent(@Nullable ARGBLike argb) {
            if (this.shadowColor == null) {
                this.shadowColor = argb == null ? null : ShadowColor.shadowColor(argb);
            }
            return this;
        }

        @Override
        public @NotNull Builder decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
            Args.notNull(decoration, "decoration");
            Args.notNull(state, "state");

            this.decorations = this.decorations.with(decoration, state);
            return this;
        }

        @Override
        public @NotNull Builder decorationIfAbsent(
                @NotNull TextDecoration decoration,
                @NotNull TextDecoration.State state
        ) {
            Args.notNull(decoration, "decoration");
            Args.notNull(state, "state");

            TextDecoration.State oldState = this.decorations.get(decoration);

            if (oldState == TextDecoration.State.NOT_SET) {
                this.decorations = this.decorations.with(decoration, state);
            }

            return this;
        }

        @Override
        public @NotNull Builder decorations(@Nullable Map<TextDecoration, TextDecoration.State> decorations) {
            if(decorations == null) {
                this.decorations = DecorationMap.empty();
            } else {
                this.decorations = DecorationMap.fromMap(decorations);
            }
            return this;
        }

        @Override
        public @NotNull Builder hover(@Nullable HoverEvent<?> event) {
            this.hoverEvent = event;
            return this;
        }

        @Override
        public @NotNull Builder hoverIfAbsent(@Nullable HoverEvent<?> event) {
            if (this.hoverEvent == null) {
                this.hoverEvent = event;
            }
            return this;
        }

        @Override
        public @NotNull Builder click(@Nullable ClickEvent<?> event) {
            this.clickEvent = event;
            return this;
        }

        @Override
        public @NotNull Builder clickIfAbsent(@Nullable ClickEvent<?> event) {
            if (this.clickEvent == null) {
                this.clickEvent = event;
            }
            return this;
        }

        @Override
        public @NonNull Builder font(@Nullable Key font) {
            this.fontKey = requireNonNull(font, "font");
            return this;
        }

        @Override
        public @NotNull Builder prefix(@Nullable MessageLike message) {
            this.prefix = message;
            return this;
        }

        @Override
        public @NotNull Builder append(@Nullable MessageLike message) {
           this.suffix = message;
            return this;
        }

        @Override
        public @NotNull Builder upperCase(boolean upperCase) {
            this.upperCase = upperCase;
            return this;
        }

        @Override
        public @NotNull Builder multipleLines(int maxPixelsPerLine, @Nullable ComponentLike linePrefix) {
            this.multipleLinesMaxCharsPerLine = maxPixelsPerLine;
            this.linePrefix = linePrefix;
            return this;
        }

        @Override
        public @NotNull Builder inheritStyle(boolean inherit) {
            this.inheritStyle = inherit;
            return this;
        }

        @Override
        public @NotNull MessageStyle build() {
            return new StyleImpl(
                    this.color,
                    this.shadowColor,
                    this.decorations,
                    this.clickEvent,
                    this.hoverEvent,
                    this.fontKey,
                    this.prefix,
                    this.suffix,
                    this.multipleLinesMaxCharsPerLine,
                    this.linePrefix,
                    this.upperCase,
                    this.inheritStyle
            );
        }
    }

}
