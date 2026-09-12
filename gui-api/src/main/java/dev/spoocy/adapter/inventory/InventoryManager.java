package dev.spoocy.adapter.inventory;

import dev.spoocy.adapter.compatibility.CompatibilityProvider;
import dev.spoocy.adapter.gui.saveable.PlayerViewProvider;
import dev.spoocy.adapter.gui.view.GuiView;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.log.LogAs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@LogAs("InventoryManager")
public class InventoryManager implements Listener {

    public static final InventoryManager INSTANCE = new InventoryManager();

    public static void onEnable(@NotNull Plugin plugin, @NotNull Supplier<CompatibilityProvider> factory) {
        INSTANCE.init(plugin, factory);
    }

    public static void onDisable() {
        INSTANCE.shutdown();
    }

    //private final Map<Inventory, CustomInventory> HANDLER_MAP = new HashMap<>();
    private final Map<Player, GuiView> currentlyOpen = new HashMap<>();
    private final Set<PlayerViewProvider<?>> clearOnDisconnect = new HashSet<>();

    private Plugin plugin;
    private Supplier<CompatibilityProvider> factory;

    private InventoryManager() { }

    @NotNull
    public Plugin getInitializerPlugin() {
        if(this.plugin == null) {
            throw new IllegalStateException("InventoryManager has not been loaded! Use InventoryManager.onLoad(Plugin, Factory)");
        }
        return this.plugin;
    }

    @NotNull
    public CompatibilityProvider getFactory() {
        if(this.factory == null) {
            throw new IllegalStateException("InventoryManager has not been loaded! Use InventoryManager.onLoad(Plugin, Factory)");
        }
        return this.factory.get();
    }

    public void init(@NotNull Plugin plugin, @NotNull Supplier<CompatibilityProvider> factory) {
         if(this.plugin != null) return;
        this.plugin = plugin;
        this.factory = factory;
        Bukkit.getPluginManager().registerEvents(INSTANCE, plugin);
    }

    public void shutdown() {
        if(this.plugin == null) return;
        this.plugin = null;
        HandlerList.unregisterAll(INSTANCE);
    }

//    public void setListen(@NotNull CustomInventory inventory, boolean listen) {
//        Inventory bukkit = inventory.getInventory();
//
//        if(listen) {
//            BukkitLogger.trace("Registered {} for event handling inventory {}", inventory, bukkit);
//            this.HANDLER_MAP.put(bukkit, inventory);
//        } else {
//            BukkitLogger.trace("Unregistered {} from event handling inventory {}", inventory, bukkit);
//            this.HANDLER_MAP.remove(bukkit);
//        }
//    }

    @Nullable
    public CustomInventory getHandler(@NotNull Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();

        if(holder instanceof CustomInventory) {
            return (CustomInventory) holder;
        }

        BukkitLogger.trace("Inventory {} is not a CustomInventory", inventory);
        return null;
    }

    @Nullable
    public GuiView getCurrentlyOpen(@NotNull Player player) {
        return this.currentlyOpen.get(player);
    }

    public void markOpen(@NotNull GuiView view) {
        BukkitLogger.trace("Player {} marked as viewing gui {}", view.getViewer().getName(), view.getClass().getSimpleName());
        this.currentlyOpen.put(view.getViewer(), view);
    }

    public void markClosed(@NotNull Player player) {
        BukkitLogger.trace("Player {} unmarked from viewing gui", player.getName());
        this.currentlyOpen.remove(player);
    }

    public void registerClearOnDisconnect(@NotNull PlayerViewProvider<?> provider) {
        BukkitLogger.trace("Registered provider {} for clearing on disconnect", provider.getClass().getSimpleName());
        this.clearOnDisconnect.add(provider);
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onQuit(@NotNull PlayerQuitEvent event) {
        //BukkitLogger.trace("InventoryManager called for Event handling: PlayerQuitEvent ({})", event.getPlayer().getName());

        for (PlayerViewProvider<?> provider : this.clearOnDisconnect) {
            provider.clearCached(event.getPlayer());
        }

        if(getCurrentlyOpen(event.getPlayer()) != null) {
            this.markClosed(event.getPlayer());
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        //BukkitLogger.trace("InventoryManager called for Event handling: InventoryOpenEvent ({})", event.getPlayer().getName());

        CustomInventory custom = getHandler(event.getInventory());
        if(custom != null) {
            //BukkitLogger.trace("Handling open event for custom inventory {}", custom);
            custom.handle(event);
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        //BukkitLogger.trace("InventoryManager called for Event handling: InventoryCloseEvent ({})", event.getPlayer().getName());

        CustomInventory custom = getHandler(event.getInventory());
        if(custom != null) {
            //BukkitLogger.trace("Handling close event for custom inventory {}", custom);
            custom.handle(event);
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        //BukkitLogger.trace("InventoryManager called for Event handling: InventoryClickEvent ({})", event.getWhoClicked().getName());

//        Player player = (Player) event.getWhoClicked();
//
//        GuiView view = this.getCurrentlyOpen(player);
//        if(view != null) {
//            // using view directly to handle the event
//            view.simulateClick(event);
//            return;
//        }

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return; // Click was outside any inventory, ignore
        }

        CustomInventory custom = getHandler(clickedInventory);
        if(custom != null) {
            //BukkitLogger.trace("Handling click event for custom inventory {}", custom);
            custom.handle(event);
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onInventoryDrag(@NotNull InventoryDragEvent event) {
        CustomInventory custom = getHandler(event.getInventory());
        if(custom != null) {
            //BukkitLogger.trace("Handling drag event for custom inventory {}", custom);
            custom.handle(event);
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onInventoryMove(@NotNull InventoryMoveItemEvent event) {
        //BukkitLogger.trace("InventoryManager called for Event handling: InventoryMoveItemEvent");

        CustomInventory source = getHandler(event.getSource());
        if(source != null) {
            //BukkitLogger.trace("Handling move event (source) for custom inventory {}", source);
            source.handle(event);
            return;
        }

        CustomInventory destination = getHandler(event.getDestination());
        if(destination != null) {
            //BukkitLogger.trace("Handling move event (dest) for custom inventory {}", destination);
            destination.handle(event);
        }
    }




}
