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

public class WrappedMessage extends AbstractPluginMessage<ComponentMessage> implements ComponentMessage {

    private final Component component;

    public WrappedMessage(@NotNull Component component, @NotNull MessageStyle style) {
        super(style);
        this.component = component;
    }

    @Override
    protected @NonNull ComponentMessage newInstance(@NotNull MessageStyle style) {
        return new WrappedMessage(this.component, style);
    }

    @Override
    public @NotNull Component rawComponent() {
        return this.component;
    }

}
