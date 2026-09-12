package dev.spoocy.adapter.message;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.language.Translation;
import dev.spoocy.adapter.message.serialization.PluginMessageStyleRenderer;
import dev.spoocy.adapter.message.serialization.RenderContext;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface GlobalTranslation extends Translation {

    @Contract("_ -> new")
    @NotNull
    static Builder builder(@NotNull Key key) {
        return GlobalTranslationImpl.builder(key);
    }

    @Contract("_ -> new")
    @NotNull
    static Builder builder(@NotNull Plugin plugin) {
        String name = plugin.getName().toLowerCase(Locale.ROOT);
        return builder(Key.key(name, "global_translation"));
    }

    @NotNull
    static GlobalTranslation instance() {
        return PluginConfig.globalTranslation();
    }


    @Contract(pure = true)
    @NotNull
    static TranslatableComponentRenderer<Locale> renderer() {
        return GlobalTranslator.renderer();
    }

    @Override
    @NotNull
    MiniMessage miniMessageSerializer();

    @NotNull
    PluginMessageStyleRenderer messageRenderer();

    @NotNull
    PlainTextComponentSerializer plainSerializer();

    @NotNull
    LegacyComponentSerializer legacySerializer();

    @NotNull
    default Component render(@NotNull Message message, @Nullable RenderContext context) {
        return render(message, context, true);
    }

    @NotNull
    Component render(@NotNull Message message, @Nullable RenderContext context, boolean freshStyle);

    @NotNull
    default List<Component> renderLines(@NotNull Message message, @Nullable RenderContext context) {
        return renderLines(message, context, true);
    }

    @NotNull
    List<Component> renderLines(@NotNull Message message, @Nullable RenderContext context, boolean freshStyle);

    interface Builder {

        @Contract("_ -> this")
        @NotNull
        Builder messageRenderer(@NotNull PluginMessageStyleRenderer pluginMessageStyleRenderer);

        @Contract("_ -> this")
        @NotNull
        Builder plainTextSerializer(@NotNull PlainTextComponentSerializer plainTextSerializer);

        @Contract("_ -> this")
        @NotNull
        Builder minimessage(@NotNull MiniMessage miniMessage);

        @Contract("_ -> this")
        @NotNull
        Builder legacySerializer(@NotNull LegacyComponentSerializer legacySerializer);

        @Contract(" -> new")
        @NotNull
        GlobalTranslation build();

    }

}
