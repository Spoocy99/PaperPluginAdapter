package dev.spoocy.adapter.message.types.impl;

import dev.spoocy.adapter.message.ComponentMessage;
import dev.spoocy.adapter.message.style.MessageStyle;
import dev.spoocy.adapter.message.types.AbstractPluginMessage;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class EmptyMessage extends AbstractPluginMessage<ComponentMessage> implements ComponentMessage {

    public static final EmptyMessage INSTANCE = new EmptyMessage(MessageStyle.empty());

    private static final Component EMPTY_COMPONENT = Component.empty();

    private EmptyMessage(@NotNull MessageStyle style) {
        super(style);
    }

    @Override
    protected @NonNull ComponentMessage newInstance(@NotNull MessageStyle style) {
        return new EmptyMessage(style);
    }

    @Override
    public @NotNull Component rawComponent() {
        return EMPTY_COMPONENT;
    }
}
