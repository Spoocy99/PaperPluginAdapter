package dev.spoocy.adapter.paper;

import dev.spoocy.adapter.compatibility.AudienceProvider;
import dev.spoocy.adapter.compatibility.PAudience;
import dev.spoocy.utils.common.collections.Collector;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class PaperAudienceProvider implements AudienceProvider {

    @Override
    public void initialize() { }

    @Override
    public Audience all() {
        return Bukkit.getServer();
    }

    @Override
    public Audience console() {
        return Bukkit.getConsoleSender();
    }

    @Override
    public Audience players() {
        return PAudience.combine(Collector.of(Bukkit.getOnlinePlayers())
            .map(this::player)
            .asList());
    }

    @Override
    public Audience commandSender(@NotNull CommandSender sender) {
        return sender;
    }

    @Override
    public Audience player(@NotNull Player player) {
        return player;
    }

    @Override
    public Audience player(@NotNull UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public Iterable<? extends Audience> world(@NotNull World world) {
        return world.getPlayers();
    }

    @Override
    public void close() { }
}
