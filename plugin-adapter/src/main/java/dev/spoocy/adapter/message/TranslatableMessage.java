package dev.spoocy.adapter.message;

import dev.spoocy.adapter.message.serialization.RenderContext;
import dev.spoocy.adapter.message.types.Arguments;
import dev.spoocy.adapter.message.types.ScopedMessage;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface TranslatableMessage extends ScopedMessage<TranslatableMessage>, Arguments<TranslatableMessage> {

    /**
     * Used to render this component.
     */
    @NotNull
    String getTranslationKey();

    /**
     * Used to render this component.
     */
    @NotNull
    TranslatableComponent asTranslatableComponent(@Nullable RenderContext context);

    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    TranslatableMessage args(@NotNull ComponentLike... arguments);

}
