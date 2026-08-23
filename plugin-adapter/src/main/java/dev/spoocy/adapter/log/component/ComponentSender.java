package dev.spoocy.adapter.log.component;

import dev.spoocy.adapter.message.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@FunctionalInterface
public interface ComponentSender {

    LoggerComponentSerializer BUNGEE_COLOR_CODES = LoggerComponentSerializer.create(
            Message::toLegacy
    );

    ComponentSender SPIGOT = cmp -> Bukkit.getConsoleSender().sendMessage(BUNGEE_COLOR_CODES.serialize(cmp));
    ComponentSender PAPER = cmp -> Bukkit.getConsoleSender().sendMessage(cmp);

    void send(@NotNull Component cmp);
}
