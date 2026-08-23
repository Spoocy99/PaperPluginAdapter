package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.messages.Localization;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class EmptyMessage extends NonEditable {

    @Override
    public List<Component> cmpList(@NotNull Localization locale, boolean reset) {
        return List.of(cmp(locale, reset));
    }

    @Override
    public Component cmp(@NotNull Localization locale, boolean reset) {
        Component empty = Component.empty();
            if (reset) {
                empty = empty
                    .color(PluginConfig.baseColor())
                    .decorations(Message.decorationsMap(TextDecoration.State.FALSE))
                ;
            }
        return empty;
    }
}
