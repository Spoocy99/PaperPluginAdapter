package dev.spoocy.adapter.message;

import dev.spoocy.adapter.message.types.Arguments;
import dev.spoocy.adapter.message.types.ScopedMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.*;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface TextMessage extends ScopedMessage<TextMessage>, Arguments<TextMessage> {

    /**
     * Used to render this component.
     */
    @ApiStatus.Internal
    boolean isMiniMessageFormat();

    /**
     * Used to render this component.
     */
    @ApiStatus.Internal
    @NotNull
    String getContent();

    /**
     * Used to render this component.
     */
    @ApiStatus.Internal
    @Unmodifiable
    @NotNull
    List<TagResolver> getTagResolvers();

    /**
     * Creates a new {@link TextMessage} with the given content.
     *
     * @param content the new content
     *
     * @return the new message
     */
    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    TextMessage withContent(@NotNull String content);

}
