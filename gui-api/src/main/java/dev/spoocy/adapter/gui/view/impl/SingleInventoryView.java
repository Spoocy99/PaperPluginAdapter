package dev.spoocy.adapter.gui.view.impl;

import dev.spoocy.adapter.gui.exceptions.IncompatibleGuiException;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.types.Resetable;
import dev.spoocy.adapter.inventory.CustomInventory;
import dev.spoocy.adapter.inventory.InventoryManager;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.message.LocalizedComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class SingleInventoryView extends AbstractGuiView {

    private final Set<Item> toUpdate = new HashSet<>();

    @Nullable
    protected Gui gui;

    @Nullable
    protected CustomInventory inventory;

    public SingleInventoryView(
            @NotNull Player viewer,
            @NotNull Locale locale,
            @NotNull LocalizedComponent title,
            boolean closeable,
            @Nullable Gui gui
    ) {
        super(viewer, locale, title, closeable);

        if (gui != null) {
            setGui0(gui);
        }
    }

    @Override
    protected void openView0() {
        if (this.inventory == null) {
            throw new IllegalStateException("No view built");
        }

        this.inventory.open(this.viewer);
    }

    @Override
    protected void closeView0() {
        if (this.inventory == null) {
            throw new IllegalStateException("No view built");
        }

        this.inventory.close();
    }

    @NotNull
    private Component buildTitle() {
        return super.title.cmp(this.locale);
    }

    @Override
    public synchronized void redraw(int x, int y) {
        if (this.inventory == null) {
            throw new IllegalStateException("No view built");
        }

        //BukkitLogger.trace("Redrawing slot at X: {} Y: {}", x, y);

        Item item = this.gui.getItem(x, y);
        int slot = toInventorySlot(x, y);

        this.inventory.setItem(
                slot,
                item != null ? item.getItemStack(this) : null
        );
    }

    @Override
    public void redraw() {
        if (this.gui == null) {
            throw new IllegalStateException("No view built");
        }

        setGui0(this.gui);
    }

    @Override
    public void resetDisplayedGui() {
        if (this.gui instanceof Resetable) {
            ((Resetable) this.gui).reset();
        }
    }

    @Override
    public @Nullable Gui getGuiAt(int x, int y) {
        if (toInventorySlot(x, y) < 0) return null;
        return this.gui;
    }

    /**
     * Converts the given {@link Gui} coordinates (x, y) into the corresponding inventory slot index.
     *
     * <p>
     * {@code -1} signifies that the given coordinates do not correspond to a valid inventory slot.
     *
     * @param x The x-coordinate within the GUI.
     * @param y The y-coordinate within the GUI.
     *
     * @return The calculated slot.
     */
    protected int toInventorySlot(int x, int y) {
        int slot = x + y * this.gui.getWidth();

        if(slot < 0 || slot > this.inventory.getSize()) {
            return -1;
        }

        return slot;
    }

    protected Coordinate toCoordinates(int slot) {
        if(slot < 0 || slot > this.inventory.getSize()) return Coordinate.UNKNOWN;

        int width = this.gui.getWidth();
        return Coordinate.of(slot % width, slot / width);
    }

    protected synchronized void setGui0(@NotNull Gui gui) {
        validateGui(gui);

        if (this.gui != gui) {
            if (this.gui != null) {
                this.gui.unsubscribe(this);
                this.gui.retrieveAllPresentItems().forEach(item -> item.unsubscribe(this));
            }

            this.gui = gui;
            this.gui.subscribe(this);
            this.gui.retrieveAllPresentItems().forEach(item -> item.subscribe(this));
        }

        // reset old inventory
        if (this.inventory != null) {
            this.inventory.setListeningForActions(false);
            this.inventory.onClick(e -> {
            });
            this.inventory.onOpen(e -> {
            });
            this.inventory.onClose(e -> {
            });
        }

        this.inventory = createInventory(this.buildTitle(), gui);
        this.inventory.onClick(this::handleClick);
        this.inventory.onOpen(this::handleOpen);
        this.inventory.onClose(this::handleClose);

        for (int y = 0; y < gui.getHeight(); y++) {
            for (int x = 0; x < gui.getWidth(); x++) {
                redraw(x, y);
            }
        }

        if (this.isOpen()) {
            this.openView0();
        }
    }

    protected abstract void validateGui(@NotNull Gui gui) throws IncompatibleGuiException;

    protected abstract CustomInventory createInventory(@NotNull Component title, @NotNull Gui gui);

    @Override
    public void notifyChanges(@NotNull Item item) {
        synchronized (this.toUpdate) {
            this.toUpdate.add(item);
        }
    }

    protected void onTick(long current) {

        Set<Item> snapshot;

        synchronized (this.toUpdate) {
            // first create snapshot
            snapshot = new HashSet<>(this.toUpdate);
            this.toUpdate.clear();
        }

        if (this.gui == null) return;

        for (int y = 0; y < this.gui.getHeight(); y++) {
            for (int x = 0; x < this.gui.getWidth(); x++) {
                Item item = this.gui.getItem(x, y);

                if (item == null) continue;

                if (snapshot.contains(item) || (item.getUpdateTick() > 0 && current % item.getUpdateTick() == 0)) {
                    this.redraw(x, y);
                }
            }
        }
    }

    protected void handleClick(@NotNull InventoryClickEvent event) {

        Coordinate coords = toCoordinates(event.getSlot());
        BukkitLogger.trace("Clicked slot {} >> X: {} Y: {}", event.getSlot(), coords.getX(), coords.getY());

        if (!this.gui.isInteractionAllowed(coords.getX(), coords.getY(), event.getClick(), event.getAction())) {
            event.setCancelled(true);
        }

        this.gui.handleClick(
                coords.getX(),
                coords.getY(),
                event,
                this
        );
    }

    protected void handleOpen(@NotNull InventoryOpenEvent event) {
        BukkitLogger.trace("InventoryView opened for " + viewer.getUniqueId());
    }

    protected void handleClose(@NotNull InventoryCloseEvent event) {
        BukkitLogger.trace("InventoryView closed for " + viewer.getUniqueId());

        // View is closed by other view
        if (!super.shouldHandleClose) {
            BukkitLogger.trace("Skipping close handling for " + viewer.getUniqueId() + " because another view was opened.");
            super.shouldHandleClose = true;

            if (this.isResetOnSwitch()) {
                this.resetDisplayedGui();
            }

            return;
        }

        // -- View is closed by the user

        if (!super.closeable) {
            BukkitLogger.trace("Re-opening non-closeable view for " + viewer.getUniqueId());
            Bukkit.getScheduler().runTaskLater(InventoryManager.INSTANCE.getInitializerPlugin(), this::openView0, 2);
            return;
        }

        // -- View should be closed

        if (this.isResetOnClose()) {
            this.resetDisplayedGui();
        }

        this.currentlyOpen = false;
        //BukkitLogger.trace("Finished close handling for " + viewer.getUniqueId());
    }
}
