package dev.spoocy.adapter.message.serialization;

import dev.spoocy.adapter.message.MessageLike;
import dev.spoocy.adapter.message.style.MessageStyle;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

/**
 * Renderer for {@link MessageStyle MessageStyle}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface PluginMessageStyleRenderer {

    @Contract(value = " -> new", pure = true)
    static @NotNull Builder builder() {
        return new PluginMessageStyleRendererImpl.BuilderImpl();
    }

    /**
     * Renders the component.
     */
    @NotNull
    Component render(@NotNull Component component, @NotNull MessageStyle style, @NotNull Function<MessageLike, Component> prefixHandler);

    /**
     * Renders the component lines.
     */
    @NotNull
    List<Component> renderLines(@NotNull Component component, @NotNull MessageStyle style, @NotNull Function<MessageLike, Component> prefixHandler);

    /**
     * Creates a new builder from this serializer.
     *
     * @return the builder
     */
    @Contract(value = " -> new", pure = true)
    @NotNull
    Builder toBuilder();

    interface Builder extends AbstractBuilder<PluginMessageStyleRenderer> {

        Builder wrapper(@NotNull ComponentLineWrapper wrapper);

        /**
         * Builds the serializer.
         *
         * @return the built serializer
         */
        @Override
        @NotNull PluginMessageStyleRenderer build();
    }

}
