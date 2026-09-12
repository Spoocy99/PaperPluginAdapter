package dev.spoocy.adapter.message;

import dev.spoocy.adapter.language.FilesTranslation;
import dev.spoocy.adapter.message.serialization.PluginMessageStyleRenderer;
import dev.spoocy.adapter.message.serialization.RenderContext;
import dev.spoocy.adapter.message.style.MessageStyle;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GlobalTranslationImpl extends FilesTranslation implements GlobalTranslation {

    @Contract("_ -> new")
    public static @NotNull Builder builder(@NotNull Key key) {
        return new BuilderImpl(key);
    }

    private final TranslatableComponentRenderer<Locale> translatableComponentRenderer = TranslatableComponentRenderer
            .usingTranslationSource(this);

    private final PluginMessageStyleRenderer pluginMessageStyleRenderer;
    private final PlainTextComponentSerializer plainTextSerializer;
    private final LegacyComponentSerializer legacySerializer;

    public GlobalTranslationImpl(
            @NotNull Key key,
            @NotNull PluginMessageStyleRenderer pluginMessageStyleRenderer,
            @NotNull MiniMessage miniMessageSerializer,
            @NotNull PlainTextComponentSerializer plainTextSerializer,
            @NotNull LegacyComponentSerializer legacySerializer
    ) {
        super(key, miniMessageSerializer);

        this.pluginMessageStyleRenderer = Args.notNull(pluginMessageStyleRenderer, "pluginMessageSerializer");
        this.plainTextSerializer = Args.notNull(plainTextSerializer, "plainTextSerializer");
        this.legacySerializer = Args.notNull(legacySerializer, "legacySerializer");
    }

    @Override
    public @NotNull PluginMessageStyleRenderer messageRenderer() {
        return this.pluginMessageStyleRenderer;
    }

    @Override
    public @NotNull PlainTextComponentSerializer plainSerializer() {
        return this.plainTextSerializer;
    }

    @Override
    public @NotNull LegacyComponentSerializer legacySerializer() {
        return this.legacySerializer;
    }

    @Override
    public @NotNull Component render(@NotNull Message message, @Nullable RenderContext context, boolean freshStyle) {
        Component cmp = asComponent(message, context);
        MessageStyle style = message.style();

        if (freshStyle && style.isInheritStyle()) {
            style = style.inheritStyle(false);
        }

        return this.pluginMessageStyleRenderer.render(
                cmp,
                style,
                mess -> this.render(mess.asMessage(), context)
        );
    }

    @Override
    public @NotNull List<Component> renderLines(
            @NotNull Message message,
            @Nullable RenderContext context,
            boolean freshStyle
    ) {
        Component cmp = asComponent(message, context);
        MessageStyle style = message.style();

        if (freshStyle && style.isInheritStyle()) {
            style = style.inheritStyle(false);
        }

        return this.pluginMessageStyleRenderer.renderLines(
                cmp,
                style,
                mess -> this.render(mess.asMessage(), context)
        );
    }

    @NotNull
    private Component asComponent(@NotNull Message message, @Nullable RenderContext context) {
        if (message instanceof TranslatableMessage) {
            return this.asComponent((TranslatableMessage) message, context);
        }

        if (message instanceof TextMessage) {
            return this.asComponent((TextMessage) message, context);
        }

        if (message instanceof ComponentMessage) {
            return this.asComponent((ComponentMessage) message, context);
        }

        throw new IllegalArgumentException("Unknown plugin message type: " + message.getClass().getName());
    }

    @NotNull
    private Component asComponent(@NotNull TranslatableMessage message, @Nullable RenderContext context) {
        String key = message.getTranslationKey();

        TranslatableComponent cmp = message.asTranslatableComponent(context);

        if (context != null) {

            Locale locale = context.getLocale();

            // prioritize this translation over default minecraft language
            if (this.canTranslate(key, locale)) {
                return this.translatableComponentRenderer.render(cmp, locale);
            } else {
                return GlobalTranslator.render(cmp, locale);
            }

        }

        return cmp;
    }

    @NotNull
    private Component asComponent(@NotNull TextMessage message, @Nullable RenderContext context) {
        String content = message.getContent();
        Component cmp;

        if (message.isMiniMessageFormat()) {

            TagResolver[] resolvers = message.getTagResolvers().toArray(TagResolver[]::new);

            if (context == null) {
                // deserialize without context
                cmp = this.miniMessageSerializer().deserialize(content, resolvers);
            } else {
                // deserialize with context
                cmp = this.miniMessageSerializer().deserialize(content, context, resolvers);
            }

        } else {
            // no mini message format so just assume plain text
            cmp = Component.text(content);
        }

        return cmp;
    }

    @NotNull
    private Component asComponent(@NotNull ComponentMessage message, @Nullable RenderContext context) {
        return message.rawComponent();
    }

    static class BuilderImpl implements Builder {

        private final Key key;
        private PluginMessageStyleRenderer pluginMessageStyleRenderer;
        private MiniMessage minimessageSerializer;
        private PlainTextComponentSerializer plainTextSerializer;
        private LegacyComponentSerializer legacySerializer;

        private BuilderImpl(@NotNull Key key) {
            this.key = key;
        }

        @Override
        public @NotNull Builder messageRenderer(@NotNull PluginMessageStyleRenderer pluginMessageStyleRenderer) {
            this.pluginMessageStyleRenderer = Args.notNull(pluginMessageStyleRenderer, "pluginMessageSerializer");
            return this;
        }

        @Override
        public @NotNull Builder minimessage(@NotNull MiniMessage minimessage) {
            this.minimessageSerializer = Args.notNull(minimessage, "minimessage");
            return this;
        }

        @Override
        public @NotNull Builder plainTextSerializer(@NotNull PlainTextComponentSerializer plainTextSerializer) {
            this.plainTextSerializer = Args.notNull(plainTextSerializer, "plainTextSerializer");
            return this;
        }

        @Override
        public @NotNull Builder legacySerializer(@NotNull LegacyComponentSerializer legacySerializer) {
            this.legacySerializer = Args.notNull(legacySerializer, "legacySerializer");
            return this;
        }

        @Override
        public @NotNull GlobalTranslation build() {
            return new GlobalTranslationImpl(
                    this.key,
                    this.pluginMessageStyleRenderer,
                    this.minimessageSerializer,
                    this.plainTextSerializer,
                    this.legacySerializer
            );
        }
    }

}
