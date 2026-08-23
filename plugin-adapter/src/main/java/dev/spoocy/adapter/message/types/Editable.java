package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.messages.MessageStyle;
import dev.spoocy.adapter.messages.PluginMessage;
import dev.spoocy.adapter.messages.PluginMessageContainer;
import dev.spoocy.adapter.messages.font.Font;
import dev.spoocy.adapter.messages.placeholder.Placeholder;
import dev.spoocy.utils.common.collections.Collector;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class Editable extends MessageProvider implements PluginMessage {

    private final MessageStyle style;

    public Editable(@NotNull MessageStyle style) {
        this.style = style;
    }

    private Editable create(@NotNull MessageStyle style) {
        return new Editable(style) {

            @Override
            protected Component cmp0(@NotNull Localization locale, @NotNull TagResolver[] placeholders, boolean upperCase) {
                return Editable.this.cmp0(locale,placeholders, upperCase);
            }

        };
    }

    @Override
    public PluginMessage style(@NotNull MessageStyle style) {
        return create(style);
    }

    @Override
    public PluginMessage color(@NotNull TextColor color) {
        return create(this.style.builder().color(color).build());
    }

    @Override
    public PluginMessage decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration... decorations) {
        TextDecoration[] result = Arrays.copyOf(decorations, decorations.length + 1);

        result[0] = decoration;
        System.arraycopy(decorations, 0, result, 1, decorations.length);

        return create(this.style
                .builder()
                .decorate(result)
                .build());
    }

    @Override
    public PluginMessage font(@NotNull Font font) {
        return create(this.style.builder().font(font).build());
    }

    @Override
    public PluginMessage prefix(@NotNull PluginMessageContainer pluginMessage) {
        return create(this.style.builder().prefix(pluginMessage).build());
    }

    @Override
    public PluginMessage append(@NotNull PluginMessageContainer pluginMessage) {
        return create(this.style.builder().suffix(pluginMessage).build());
    }

    @Override
    public PluginMessage upperCase() {
        return create(this.style.builder().upperCase().build());
    }

    @Override
    public PluginMessage multipleLines(int maxCharactersPerLine, @Nullable PluginMessageContainer linePrefix) {
        return create(this.style.builder().multipleLines(maxCharactersPerLine, linePrefix).build());
    }

    @Override
    public PluginMessage multipleLinesC(int maxCharactersPerLine, @Nullable Component linePrefix) {
        PluginMessage prefixMessage = linePrefix != null ? Message.wrap(linePrefix) : null;
        return create(this.style.builder().multipleLines(maxCharactersPerLine, prefixMessage).build());
    }

    @Override
    public PluginMessage hover(@NotNull HoverEvent<?> event) {
        return create(this.style.builder().hover(event).build());
    }

    @Override
    public PluginMessage hover(@NotNull PluginMessageContainer message) {
        return create(this.style.builder().hover(message).build());
    }

    @Override
    public PluginMessage click(@NotNull ClickEvent event) {
        return create(this.style.builder().click(event).build());
    }

    @Override
    public PluginMessage arg(@NotNull String key, @NotNull PluginMessageContainer message) {
        return create(this.style.builder().placeholder(key, message.getPluginMessage()).build());
    }

    private static final String NUMERIC_PLACEHOLDER = "arg#";
    @Override
    public PluginMessage args(@NotNull PluginMessageContainer... message) {
        MessageStyle.Builder builder = this.style.builder();
        int index = 1;

        for (PluginMessage msg : Collector.of(message).map(PluginMessageContainer::getPluginMessage)) {
            String placeholder = NUMERIC_PLACEHOLDER.replace("#", String.valueOf(index++));
            builder.placeholder(placeholder, msg);
        }

        return create(builder.build());
    }

    @NotNull
    private TagResolver[] buildPlaceholders(@NotNull Localization locale) {
        List<Placeholder> placeholders = this.style.placeholders();

        if(placeholders == null || placeholders.isEmpty()) {
            return new TagResolver[0];
        }

        return Collector.of(placeholders)
                .map(p -> p.toTagResolver(locale))
                .asArray(TagResolver[]::new);
    }

    @Override
    public List<Component> cmpList(@NotNull Localization locale, boolean reset) {

        try {
            return cmpList0(locale, buildPlaceholders(locale), this.style.upperCase(), reset);
        } catch (UnsupportedOperationException ignored) {
            // Ignore, no custom implementation
        }

        Component component = cmp0(locale, buildPlaceholders(locale), this.style.upperCase());

        int maxCharsPerLine = this.style.multipleLinesMaxCharsPerLine();

        if(maxCharsPerLine <= 0) {
            return List.of(applyTo(component, locale, reset));
        }

        PluginMessage linePrefix = this.style.multipleLinesPrefix();

        List<Component> components = cmpToLines(component, maxCharsPerLine, linePrefix != null ? linePrefix.cmp(locale, false) : null);
        components.replaceAll(cmp0 -> applyTo(cmp0, locale, reset));
        return components;
    }
    protected List<Component> cmpList0(@NotNull Localization locale, @NotNull TagResolver[] placeholders, boolean upperCase, boolean reset) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Component cmp(@NotNull Localization locale, boolean reset) {
        Component component = cmp0(locale, buildPlaceholders(locale), this.style.upperCase());

        int maxCharsPerLine = this.style.multipleLinesMaxCharsPerLine();
        PluginMessage linePrefix = this.style.multipleLinesPrefix();

        if (maxCharsPerLine > 0) {
            List<Component> lines = cmpToLines(component, maxCharsPerLine, linePrefix != null ? linePrefix.cmp(locale, false) : null);
            component = Component.join(JoinConfiguration.noSeparators(), lines);
        }

        return applyTo(component, locale, reset);
    }

    protected abstract Component cmp0(@NotNull Localization locale, @NotNull TagResolver[] placeholders, boolean upperCase);

    protected Component applyTo(@NotNull Component cmp0, @NotNull Localization locale, boolean reset) {
        Component component = cmp0;

        PluginMessage prefix = this.style.prefix();
        if (prefix != null) {
            component = prefix.cmp(locale, reset)
                    .append(component);
        }

        PluginMessage suffix = this.style.suffix();
        if (suffix != null) {
            component = component.append(suffix.cmp(locale));
        }

        TextColor color = this.style.color();
        if (color != null) {
            component = component.color(color);
        }

        Map<TextDecoration, TextDecoration.State> decorations = this.style.decorations();
        if (!decorations.isEmpty()) {
            component = component.decorations(decorations);
        }

        Key fontKey = this.style.fontKey();
        if (fontKey != null) {
            component = component.font(fontKey);
        }

        if(reset && prefix == null) {
            component = Component
                    .empty()
                    .color(PluginConfig.baseColor())
                    .decorations(Message.decorationsMap(TextDecoration.State.FALSE))
                    .append(component)
            ;
        }

        ClickEvent clickEvent = this.style.clickEvent(locale);
        if (clickEvent != null) {
            component = component.clickEvent(clickEvent);
        }

        HoverEvent<?> hoverEvent = this.style.hoverEvent(locale);
        if (hoverEvent != null) {
            component = component.hoverEvent(hoverEvent);
        }

        Font font = this.style.font();
        return font != null ? font.applyTo(component) : component;
    }

    protected static List<Component> cmpToLines(@NotNull Component cmp, int maxCharsPerLine, @Nullable Component linePrefix) {
        String text = PlainTextComponentSerializer.plainText().serialize(cmp);
        List<Component> lines = new LinkedList<>();
        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + maxCharsPerLine, text.length());

            if (end < text.length() && text.charAt(end) != ' ') {
                int lastSpace = text.lastIndexOf(' ', end);
                if (lastSpace > start) {
                    end = lastSpace;
                }
            }

            String lineText = text.substring(start, end).trim();
            Component lineComponent = Component.text(lineText);

            if (linePrefix != null) {
                lineComponent = linePrefix.append(lineComponent);
            }

            lines.add(lineComponent);

            start = end;
            while (start < text.length() && text.charAt(start) == ' ') start++;
        }

        return lines;
    }

    protected static String firstUpperCase(@NotNull String str) {
        int minimessageFormat = 0;
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '<') {
                minimessageFormat++;

            } else if (c == '>') {
                minimessageFormat--;

            } else if (minimessageFormat == 0) {

                if (Character.isUpperCase(c)) {
                    return str;

                } else if (Character.isLowerCase(c)) {
                    chars[i] = Character.toUpperCase(c);
                    return new String(chars);
                }

            }
        }
        return str;
    }

}
