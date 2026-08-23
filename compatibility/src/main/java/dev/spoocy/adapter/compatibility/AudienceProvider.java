package dev.spoocy.adapter.compatibility;

import net.kyori.adventure.audience.Audience;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.UUID;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface AudienceProvider extends Closeable {

    void initialize();

    @Override
    void close();

    Audience all();

    Audience console();

    Audience players();

    Audience commandSender(@NotNull CommandSender sender);

    Audience player(@NotNull Player player);

    Audience player(@NotNull UUID uuid);

    Iterable<? extends Audience> world(@NotNull World world);

}
