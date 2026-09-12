package dev.spoocy.adapter.message;

import dev.spoocy.adapter.message.types.ScopedMessage;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ComponentMessage extends ScopedMessage<ComponentMessage> {

    /**
     * Used to render this component.
     */
    @ApiStatus.Internal
    @NotNull
    Component rawComponent();

}
