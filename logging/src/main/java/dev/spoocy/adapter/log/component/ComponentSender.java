package dev.spoocy.adapter.log.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@FunctionalInterface
public interface ComponentSender {

    LegacyComponentSerializer BUNGEE_TEXT_SERIALIZER = LegacyComponentSerializer
            .builder()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    ComponentSender SPIGOT = cmp -> Bukkit.getConsoleSender().sendMessage(BUNGEE_TEXT_SERIALIZER.serialize(cmp));
    ComponentSender PAPER = cmp -> Bukkit.getConsoleSender().sendMessage(cmp);

    void send(@NotNull Component cmp);
}
