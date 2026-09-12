package dev.spoocy.adapter.message.types.impl;

import dev.spoocy.adapter.message.ComponentMessage;
import dev.spoocy.adapter.message.style.MessageStyle;
import dev.spoocy.adapter.message.types.AbstractPluginMessage;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.object.ObjectContents;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ObjectContentMessage extends AbstractPluginMessage<ComponentMessage> implements ComponentMessage {

    private final ObjectContents content;

    public ObjectContentMessage(@NotNull ObjectContents content, @NotNull MessageStyle style) {
        super(style);
        this.content = Args.notNull(content, "content");
    }

    @Override
    protected @NonNull ComponentMessage newInstance(@NotNull MessageStyle style) {
        return new ObjectContentMessage(this.content, style);
    }

    @Override
    public @NotNull Component rawComponent() {
        return Component.object(this.content);
    }
}
