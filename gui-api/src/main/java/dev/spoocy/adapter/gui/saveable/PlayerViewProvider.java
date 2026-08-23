package dev.spoocy.adapter.gui.saveable;

import com.google.common.collect.ImmutableList;
import dev.spoocy.adapter.event.ListenAdapter;
import dev.spoocy.adapter.gui.view.GuiView;
import dev.spoocy.adapter.inventory.InventoryManager;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.log.LogAs;
import dev.spoocy.utils.common.collections.Collector;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Predicate;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@LogAs("PlayerViewProvider")
public abstract class PlayerViewProvider<V extends GuiView> implements ViewProvider<Player, V>, ListenAdapter {

    private final HashMap<Player, V> data = new HashMap<>();

    public PlayerViewProvider(boolean clearOnDisconnect) {
        if (clearOnDisconnect) {
            InventoryManager.INSTANCE.registerClearOnDisconnect(this);
        }
    }

    @Override
    public V getOrCreate(@NotNull Player key) {
        return this.data.computeIfAbsent(key, this::createView);
    }

    @Override
    public V open(@NotNull Player key) {
        V view = this.getOrCreate(key);
        view.open();
        return view;
    }

    @Override
    public void redrawAll() {
        all().forEach(GuiView::redraw);
    }

    @Override
    public Collection<V> all() {
        return ImmutableList.copyOf(data.values());
    }

    @Override
    public Collection<V> all(@NotNull Predicate<V> filter) {
        return Collector.of(this.data.values())
                .filter(filter)
                .asImmutableList();
    }

    @Override
    public void clearCache() {
        BukkitLogger.trace("Clearing all cached views (" + this.data.size() + " entries)");
        this.data.clear();
    }

    public void clearCached(@NotNull Player player) {
        BukkitLogger.trace("Clearing cached view for player: " + player.getName() + " (" + player.getUniqueId() + ")");
        this.data.remove(player);
    }

    public abstract V createView(@NotNull Player player);

}
