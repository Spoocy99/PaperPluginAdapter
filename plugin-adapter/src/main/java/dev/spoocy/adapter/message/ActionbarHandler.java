package dev.spoocy.adapter.message;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ActionbarHandler {

    void sendActionbar(@NotNull Player player, @NotNull Component message);

}
