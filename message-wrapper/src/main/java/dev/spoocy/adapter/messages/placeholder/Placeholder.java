package dev.spoocy.adapter.messages.placeholder;

import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.messages.PluginMessage;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Placeholder {

    @NotNull
    @TagPattern String key();

    @NotNull
    PluginMessage render();

    @NotNull
    TagResolver toTagResolver(@NotNull Localization localization);

}
