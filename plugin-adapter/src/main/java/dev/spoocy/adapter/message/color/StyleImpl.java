package dev.spoocy.adapter.message.color;

import com.google.common.collect.ImmutableMap;
import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.message.font.Fonts;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.messages.MessageStyle;
import dev.spoocy.adapter.messages.PluginMessage;
import dev.spoocy.adapter.messages.PluginMessageContainer;
import dev.spoocy.adapter.messages.font.Font;
import dev.spoocy.adapter.messages.placeholder.Placeholder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.*;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.util.*;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class StyleImpl implements MessageStyle {

    @Nullable
    private final TextColor color;
    @NotNull
    private final Map<TextDecoration, TextDecoration.State> decorations;
    @Nullable
    private final Key fontKey;
    @Nullable
    private final Font font;
    @Nullable
    private final PluginMessage prefix;
    @Nullable
    private final PluginMessage suffix;
    private final boolean upperCase;
    private final int multipleLinesMaxCharsPerLine;
    @Nullable
    private final PluginMessage linePrefix;
    @Nullable
    private final ClickEvent clickEvent;
    @Nullable
    private final Function<Localization, HoverEvent<?>> hoverEvent;
    @Nullable
    private final List<Placeholder> placeholders;

    public StyleImpl(
            @Nullable TextColor color,
            @NotNull Map<TextDecoration, TextDecoration.State> decorations,
            @Nullable Key fontKey,
            @Nullable Font font,
            @Nullable PluginMessage prefix,
            @Nullable PluginMessage suffix,
            boolean upperCase,
            int multipleLinesMaxCharsPerLine,
            @Nullable PluginMessage linePrefix,
            @Nullable ClickEvent clickEvent,
            @Nullable Function<Localization, HoverEvent<?>> hoverEvent,
            @Nullable List<Placeholder> placeholders
    ) {
        this.color = color;
        this.decorations = decorations.isEmpty() ? ImmutableMap.of() : ImmutableMap.copyOf(decorations);
        this.fontKey = fontKey;
        this.font = font;
        this.prefix = prefix;
        this.suffix = suffix;
        this.upperCase = upperCase;
        this.multipleLinesMaxCharsPerLine = multipleLinesMaxCharsPerLine;
        this.linePrefix = linePrefix;
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
        this.placeholders = placeholders;
    }

    @Override
    public @Nullable TextColor color() {
        return this.color;
    }

    @Override
    public @NotNull Map<TextDecoration, TextDecoration.State> decorations() {
        return this.decorations;
    }

    @Override
    public @Nullable Key fontKey() {
        return this.fontKey;
    }

    @Override
    public @Nullable Font font() {
        return this.font;
    }

    @Override
    public @Nullable ClickEvent clickEvent(@NotNull Localization locale) {
        return this.clickEvent;
    }

    @Override
    public @Nullable HoverEvent<?> hoverEvent(@NotNull Localization locale) {
        if (this.hoverEvent == null) {
            return null;
        }
        return this.hoverEvent.apply(locale);
    }

    @Override
    public @Nullable List<Placeholder> placeholders() {
        return this.placeholders;
    }

    @Override
    public @Nullable PluginMessage prefix() {
        return this.prefix;
    }

    @Override
    public @Nullable PluginMessage suffix() {
        return this.suffix;
    }

    @Override
    public boolean upperCase() {
        return this.upperCase;
    }

    @Override
    public int multipleLinesMaxCharsPerLine() {
        return this.multipleLinesMaxCharsPerLine;
    }

    @Override
    public @Nullable PluginMessage multipleLinesPrefix() {
        return this.linePrefix;
    }

    @Override
    public Builder builder() {
        return new BuilderImpl(this);
    }

    public static class BuilderImpl implements MessageStyle.Builder {

        private final Map<TextDecoration, TextDecoration.State> decorations;

        private TextColor color;
        private Key fontKey;
        private Font font;
        private PluginMessageContainer prefix;
        private PluginMessageContainer suffix;
        private boolean upperCase;
        private int multipleLinesMaxCharsPerLine;
        private PluginMessageContainer linePrefix;
        private ClickEvent clickEvent;
        private Function<Localization, HoverEvent<?>> hoverEvent;
        private List<Placeholder> placeholders;

        public BuilderImpl() {
            this.decorations = new EnumMap<>(TextDecoration.class);

            this.color = null;
            this.font = Fonts.DEFAULT;
            this.prefix = null;
            this.suffix = null;
            this.upperCase = false;
            this.multipleLinesMaxCharsPerLine = 0;
            this.linePrefix = null;
            this.hoverEvent = null;
            this.clickEvent = null;
            this.placeholders = null;
        }

        public BuilderImpl(@NotNull StyleImpl style) {
            this.decorations = style.decorations.isEmpty()
                    ? new EnumMap<>(TextDecoration.class)
                    : new EnumMap<>(style.decorations);

            this.color = style.color;
            this.font = style.font;
            this.prefix = style.prefix;
            this.suffix = style.suffix;
            this.upperCase = style.upperCase;
            this.multipleLinesMaxCharsPerLine = style.multipleLinesMaxCharsPerLine;
            this.linePrefix = style.linePrefix;
            this.hoverEvent = style.hoverEvent;
            this.clickEvent = style.clickEvent;
            this.placeholders = style.placeholders == null ? null : new ArrayList<>(style.placeholders);
        }

        @Override
        public @NotNull Builder font(@Nullable Key font) {
            this.fontKey = font;
            return this;
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
        public @NotNull Builder decorate(@NotNull TextDecoration decoration) {
            this.decorations.put(decoration, TextDecoration.State.TRUE);
            return this;
        }

        @Override
        public @NotNull Builder decorate(@NotNull TextDecoration... decorations) {
            for (TextDecoration decoration : decorations) {
                this.decorations.put(decoration, TextDecoration.State.TRUE);
            }
            return this;
        }

        @Override
        public @NotNull Builder decoration(@NotNull TextDecoration decoration, boolean flag) {
            this.decorations.put(decoration, TextDecoration.State.byBoolean(flag));
            return this;
        }

        @Override
        public @NotNull Builder decorations(@NotNull Map<TextDecoration, TextDecoration.State> decorations) {
            this.decorations.putAll(decorations);
            return this;
        }

        @Override
        public @NotNull Builder decoration(@NotNull TextDecoration decoration, TextDecoration.@NotNull State state) {
            this.decorations.put(decoration, state);
            return this;
        }

        @Override
        public @NotNull Builder decorationIfAbsent(@NotNull TextDecoration decoration, TextDecoration.@NotNull State state) {
            this.decorations.putIfAbsent(decoration, state);
            return this;
        }

        @Override
        public Builder decorateIfNotNull(@Nullable TextDecoration[] decorations) {
            if (decorations != null) {
                this.decorate(decorations);
            }
            return this;
        }

        @Override
        public Builder font(@Nullable Font font) {
            this.font = font;
            return this;
        }

        @Override
        public Builder prefix(@Nullable PluginMessageContainer pluginMessage) {
            this.prefix = pluginMessage;
            return this;
        }

        @Override
        public Builder suffix(@Nullable PluginMessageContainer pluginMessage) {
            this.suffix = pluginMessage;
            return this;
        }

        @Override
        public Builder upperCase() {
            this.upperCase = true;
            return this;
        }

        @Override
        public Builder multipleLines(int maxCharactersPerLine, @Nullable PluginMessageContainer linePrefix) {
            this.multipleLinesMaxCharsPerLine = maxCharactersPerLine;
            this.linePrefix = linePrefix;
            return this;
        }

        @Override
        public Builder hover(@Nullable HoverEvent<?> event) {
            this.hoverEvent = locale -> event;
            return this;
        }

        @Override
        public Builder hover(@Nullable PluginMessageContainer message) {
            this.hoverEvent = locale -> HoverEvent.showText(message.getPluginMessage().cmp(locale, false));
            return this;
        }

        @Override
        public Builder click(@Nullable ClickEvent event) {
            this.clickEvent = event;
            return this;
        }

        @Override
        public @NotNull Builder placeholder(@NotNull @TagPattern String key, @NotNull PluginMessageContainer message) {
            if (this.placeholders == null) {
                this.placeholders = new ArrayList<>();
            }

            this.placeholders.add(new PlaceholderImpl(key, message.getPluginMessage()));
            return this;
        }

        @Override
        public @NotNull MessageStyle build() {
            return new StyleImpl(
                    this.color,
                    this.decorations,
                    this.fontKey,
                    this.font,
                    this.prefix != null ? this.prefix.getPluginMessage() : null,
                    this.suffix != null ? this.suffix.getPluginMessage() : null,
                    this.upperCase,
                    this.multipleLinesMaxCharsPerLine,
                    this.linePrefix != null ? this.linePrefix.getPluginMessage() : null,
                    this.clickEvent,
                    this.hoverEvent,
                    this.placeholders
            );
        }
    }

    private static class PlaceholderImpl implements Placeholder {
        @NotNull
        private final String key;
        @NotNull
        private final PluginMessage message;

        public PlaceholderImpl(@NotNull @TagPattern String key, @NotNull PluginMessage message) {
            this.key = key;
            this.message = message;
        }

        @TagPattern
        @Override
        public @NotNull String key() {
            return this.key;
        }

        @Override
        public @NotNull PluginMessage render() {
            return this.message;
        }

        @Override
        public @NotNull TagResolver toTagResolver(@NotNull Localization locale) {
            return net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component(
                    this.key,
                    this.message.cmp(locale, false)
            );
        }


    }

}
