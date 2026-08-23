package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.messages.MessageStyle;
import dev.spoocy.adapter.messages.PluginMessage;
import dev.spoocy.adapter.messages.PluginMessageContainer;
import dev.spoocy.adapter.messages.font.Font;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class NonEditable extends MessageProvider implements PluginMessage {

    @Override
    public PluginMessage style(@NotNull MessageStyle style) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage color(@NotNull TextColor color) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration... decorations) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage append(@NotNull PluginMessageContainer pluginMessage) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage multipleLines(int maxCharactersPerLine, @Nullable PluginMessageContainer linePrefix) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage multipleLinesC(int maxCharactersPerLine, @Nullable Component linePrefix) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage font(@NotNull Font font) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage prefix(@NotNull PluginMessageContainer pluginMessage) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage upperCase() {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage hover(@NotNull PluginMessageContainer message) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage hover(@NotNull HoverEvent<?> event) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage click(@NotNull ClickEvent event) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage args(@NotNull PluginMessageContainer... message) {
        throw new UnsupportedOperationException("This message is not editable.");
    }

    @Override
    public PluginMessage arg(@NotNull String key, @NotNull PluginMessageContainer message) {
        throw new UnsupportedOperationException("This message is not editable.");
    }
}
