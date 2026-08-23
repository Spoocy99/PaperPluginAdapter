package dev.spoocy.adapter.spigot;

import dev.spoocy.adapter.compatibility.AudienceProvider;
import dev.spoocy.adapter.spigot.audiences.SpigotConsoleAudience;
import dev.spoocy.adapter.spigot.audiences.SpigotPlayerAudience;
import dev.spoocy.utils.common.collections.Collector;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.apache.http.util.Args;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SpigotAudienceProvider implements AudienceProvider, Listener {

    public static final Locale DEFAULT_LOCALE = Locale.US;

    private final Plugin plugin;

    private final Map<UUID, SpigotPlayerAudience> cachedAudiences;
    private final Set<Audience> viewers;

    private final Audience console;
    private final Audience all;
    private final Audience players;

    public SpigotAudienceProvider(@NotNull Plugin plugin) {
        this.plugin = Args.notNull(plugin, "plugin");
        this.cachedAudiences = new ConcurrentHashMap<>();
        this.viewers = new HashSet<>();
        this.console = new SpigotConsoleAudience(plugin);

        this.all = new ForwardingAudience() {
            @Override
            public @NotNull Iterable<? extends Audience> audiences() {
                return SpigotAudienceProvider.this.viewers;
            }
        };

        this.players = new ForwardingAudience() {

            @Override
            public @NotNull Iterable<? extends Audience> audiences() {
                return SpigotAudienceProvider.this.cachedAudiences.values();
            }
        };

        addViewer(plugin.getServer().getConsoleSender());
    }

    @Override
    public void initialize() {
        this.registerEvent(PlayerJoinEvent.class, EventPriority.LOWEST, event -> this.addViewer(event.getPlayer()));
        this.registerEvent(PlayerQuitEvent.class, EventPriority.MONITOR, event -> this.removeViewer(event.getPlayer()));
    }

    @Override
    public void close() {
        HandlerList.unregisterAll(this);
    }

    public void addViewer(@NotNull CommandSender viewer) {
        Audience audience = this.toAudience(viewer);
        if(audience != null) {
            this.viewers.add(audience);
        }
    }

    public void removeViewer(@NotNull CommandSender viewer) {
        Audience audience = this.toAudience(viewer);
        if(audience != null) {
            this.viewers.remove(audience);
        }
    }

    @Nullable
    private Audience toAudience(@NotNull CommandSender sender) {
        if(sender instanceof Player) {
            return this.player((Player) sender);

        } else if (sender instanceof ConsoleCommandSender) {
            return this.console();

        } else if (sender instanceof Audience) {
            return (Audience) sender;

        } else {
            return null;
        }
    }

    @Override
    public Audience all() {
        return this.all;
    }

    @Override
    public Audience console() {
        return this.console;
    }

    @Override
    public Audience players() {
        return this.players;
    }

    @Override
    public Audience commandSender(@NotNull CommandSender sender) {
        Audience audience = this.toAudience(sender);

        if (audience == null) {
            throw new IllegalArgumentException("Could not convert CommandSender " + sender.getClass().getName() + " to an Audience");
        }

        return audience;
    }

    @Override
    public Audience player(@NotNull Player player) {
        return player(player.getUniqueId());
    }

    @Override
    public Audience player(@NotNull UUID uuid) {
        return this.cachedAudiences.computeIfAbsent(uuid, SpigotPlayerAudience::new);
    }

    @Override
    public Iterable<? extends Audience> world(@NotNull World world) {
        return Collector.of(world.getPlayers())
                .map(this::player)
                .asList();
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> void registerEvent(
            @NotNull Class<E> type,
            @NotNull EventPriority priority,
            @NotNull Consumer<E> callback
    ) {
        this.plugin.getServer().getPluginManager()
                .registerEvent(type, this, priority, (listener, event) -> callback.accept((E) event), this.plugin, true);
    }

}
