package dev.spoocy.adapter.message.serialization;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.message.MessageLike;
import dev.spoocy.adapter.message.font.Font;
import dev.spoocy.adapter.message.font.FontRegistry;
import dev.spoocy.adapter.message.style.MessageStyle;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.ARGBLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class PluginMessageStyleRendererImpl implements PluginMessageStyleRenderer {

    private final ComponentLineWrapper wrapper;

    public PluginMessageStyleRendererImpl(@NotNull ComponentLineWrapper wrapper) {
        this.wrapper = Args.notNull(wrapper, "wrapper");
    }

    @Override
    public @NotNull Component render(@NotNull Component component, @NotNull MessageStyle style, @NotNull Function<MessageLike, Component> prefixHandler) {
        List<Component> parts = format(component, style, prefixHandler, false);
        Component base = parts.getFirst();

        for (int i = 1; i < parts.size(); i++) {
            base = base.appendNewline().append(parts.get(i));
        }

        return base;
    }

    @Override
    public @NotNull List<Component> renderLines(@NotNull Component component, @NotNull MessageStyle style, @NotNull Function<MessageLike, Component> prefixHandler) {
        return format(component, style, prefixHandler, true);
    }

    @NotNull
    private List<Component> format(@NotNull Component component, @NotNull MessageStyle style, @NotNull Function<MessageLike, Component> prefixHandler, boolean splitAtNewLine) {
        List<Component> parts = splitAtNewLine ? ComponentSplitter.splitByNewLine(component) : List.of(component);
        List<Component> styled = new ArrayList<>(parts.size());

        for (Component part : parts) {
            List<Component> applied = applyStyle(style, part, prefixHandler);
            styled.addAll(applied);
        }

        return styled;
    }
//
//    private static Style DEFAULT_STYLE;
//    private static Style defaultStyle() {
//        if(DEFAULT_STYLE == null) {
//            DEFAULT_STYLE = Style.empty().toBuilder()
//                    .color(PluginConfig.baseColor())
//                    .shadowColor(null)
//                    .decorations(Message.decorationsMap(TextDecoration.State.FALSE))
//                    .build();
//        }
//
//        return DEFAULT_STYLE;
//    }

    private static final Component EMPTY_STYLE_COMPONENT = Component.empty().style(b -> b
            .color(NamedTextColor.GRAY)
            .shadowColor(null)
            .decorations(Message.decorationsMap(TextDecoration.State.FALSE))
    );

    @Unmodifiable
    private List<Component> applyStyle(
            @NotNull MessageStyle style,
            @NotNull Component original,
            @NotNull Function<MessageLike, Component> prefixHandler
    ) {

        Component component = original;

        TextColor color = style.color();
        if (color != null) {
            component = component.color(color);
        }

        ARGBLike shadowColor = style.shadowColor();
        if (shadowColor != null) {
            component = component.shadowColor(shadowColor);
        }

        Map<TextDecoration, TextDecoration.State> decorations = style.decorations();
        for (Map.Entry<TextDecoration, TextDecoration.State> entry : decorations.entrySet()) {
            TextDecoration.State state = entry.getValue();

            // apply only overwritten decorations
            if(state != TextDecoration.State.NOT_SET) {
                component = component.decoration(entry.getKey(), state);
            }
        }

        ClickEvent<?> clickEvent = style.clickEvent();
        if (clickEvent != null) {
            component = component.clickEvent(clickEvent);
        }

        HoverEvent<?> hoverEvent = style.hoverEvent();
        if (hoverEvent != null) {
            component = component.hoverEvent(hoverEvent);
        }

        if (style.isUpperCase()) {
            component = CapitalizeFirstChar.apply(component);
        }

        if(!style.isInheritStyle()) {
            component = EMPTY_STYLE_COMPONENT.append(component);
        }

        MessageLike prefix = style.prefix();
        if (prefix != null) {
            Component prefixComponent = prefixHandler.apply(prefix);
            component = prefixComponent.append(component);
        }

        MessageLike suffix = style.suffix();
        if (suffix != null) {
            Component suffixComponent = prefixHandler.apply(suffix);
            component = component.append(suffixComponent);
        }

        Key fontKey = style.font();

        if (fontKey != null) {
            FontRegistry registry = PluginConfig.fonts();
            Font font = registry.getFont(fontKey);

            if (font == null) {
                throw new IllegalArgumentException("Font does not exist: " + fontKey);
            }

            component = font.applyTo(component).font(fontKey);
        }

//        component = component
//                .applyFallbackStyle(defaultStyle())
//                .compact()
//        ;

        int max = style.multipleLinesMaxCharsPerLine();
        if (max > 0) {
            ComponentLike linePrefix = style.multipleLinesPrefix();
            Component linePrefixComponent = linePrefix == null ? Component.empty() : linePrefix.asComponent();
            return this.wrapper.wrap(component, () -> linePrefixComponent, max);
        }

        return List.of(component);
    }

    @Override
    public @NotNull Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static final class BuilderImpl implements Builder {

        private Consumer<String> logger;
        private ComponentLineWrapper lineWrapper;

        BuilderImpl() {
            this.lineWrapper = null;
        }

        BuilderImpl(@NotNull PluginMessageStyleRendererImpl serializer) {
            this.lineWrapper = serializer.wrapper;
        }

        @Override
        public Builder wrapper(@NotNull ComponentLineWrapper wrapper) {
            Args.notNull(wrapper, "wrapper");
            this.lineWrapper = wrapper;
            return this;
        }

        @Override
        public @NotNull PluginMessageStyleRenderer build() {
            return new PluginMessageStyleRendererImpl(this.lineWrapper);
        }
    }

}
