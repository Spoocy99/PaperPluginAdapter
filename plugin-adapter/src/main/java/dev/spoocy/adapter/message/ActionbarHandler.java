package dev.spoocy.adapter.message;

import dev.spoocy.adapter.messages.PluginMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ActionbarHandler {

    void sendActionbar(@NotNull Player player, @NotNull PluginMessage message);

}
