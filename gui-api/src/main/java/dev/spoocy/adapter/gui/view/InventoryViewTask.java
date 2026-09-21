package dev.spoocy.adapter.gui.view;

import dev.spoocy.adapter.inventory.InventoryManager;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.common.misc.Args;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public final class InventoryViewTask {

    private final Consumer<Long> callback;

    private long menuTick = 0;
    private BukkitTask task;

    public InventoryViewTask(@NotNull Consumer<Long> callback) {
        this.callback = Args.notNull(callback, "callback");
    }

    public synchronized void start() {
        if(this.task != null) {
            return;
        }

        this.menuTick = 0;
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(
                InventoryManager.INSTANCE.getInitializerPlugin(),
                this::onTick,
                0L,
                1L
        );

        BukkitLogger.trace("InventoryView Tick Task started");
    }

    public synchronized void stop() {
        if(this.task == null) {
            return;
        }

        this.task.cancel();
        this.task = null;

        BukkitLogger.trace("InventoryView Tick Task stopped ({})", this.menuTick);
    }

    public synchronized int getCurrentTick() {
        return (int) this.menuTick;
    }

    public synchronized void onTick() {
        this.menuTick++;
        this.callback.accept(this.menuTick);
    }

}
