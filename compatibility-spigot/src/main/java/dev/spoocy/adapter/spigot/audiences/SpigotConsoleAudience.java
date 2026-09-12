package dev.spoocy.adapter.spigot.audiences;

import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import org.apache.http.util.Args;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SpigotConsoleAudience extends AbstractAudience {

    private final Plugin plugin;

    public SpigotConsoleAudience(@NotNull Plugin plugin) {
        this.plugin = Args.notNull(plugin, "plugin");
    }

    public ConsoleCommandSender getConsole() {
        return this.plugin.getServer().getConsoleSender();
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        getConsole().spigot().sendMessage(renderBungee(message));
    }

    @Override
    public void sendMessage(
            @NotNull Component message,
            @NotNull ChatType.Bound boundChatType
    ) {
        getConsole().spigot().sendMessage(renderBungee(message));
    }

    @Override
    public void sendMessage(
            @NotNull SignedMessage signedMessage,
            @NotNull ChatType.Bound boundChatType
    ) {
        getConsole().sendMessage(signedMessage.message());
    }
}
