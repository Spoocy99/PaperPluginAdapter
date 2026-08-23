package dev.spoocy.adapter.inventory;

import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.compatibility.annotations.CompatibilityProvided;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractCustomInventory extends CustomInventoryWrapper {

    @NotNull
    protected final Component title;
    protected final int[] maxStackSizes;

    protected boolean listening = false;

    protected Consumer<InventoryOpenEvent> onOpen = event -> {};
    protected Consumer<InventoryCloseEvent> onClose = event -> {};
    protected Consumer<InventoryClickEvent> onClick = event -> {};
    protected Consumer<InventoryDragEvent> onDrag = event -> {};
    protected Consumer<InventoryMoveItemEvent> onMove = event -> {};

    public AbstractCustomInventory(@NotNull Component title, int size) {
        this.title = title;
        this.maxStackSizes = new int[size];
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public void setMaxStackSize(int i) {
        Arrays.fill(maxStackSizes, i);
        getInventory().setMaxStackSize(i);
    }

    @Override
    public int[] getMaxStackSizes() {
        return Arrays.copyOf(maxStackSizes, maxStackSizes.length);
    }

    @Override
    public int getMaxSlotStackSize(int slot) {
        return maxStackSizes[slot];
    }

    @Override
    public void setMaxSlotStackSize(int slot, int maxStackSize) {
        this.maxStackSizes[slot] = maxStackSize;
    }

    @Override
    public void onOpen(@NotNull Consumer<InventoryOpenEvent> action) {
        this.onOpen = action;
    }

    @Override
    public void onClose(@NotNull Consumer<InventoryCloseEvent> action) {
        this.onClose = action;
    }

    @Override
    public void onClick(@NotNull Consumer<InventoryClickEvent> action) {
        this.onClick = action;
    }

    @Override
    public void onDrag(@NotNull Consumer<InventoryDragEvent> action) {
        this.onDrag = action;
    }

    @Override
    public void onMove(@NotNull Consumer<InventoryMoveItemEvent> action) {
        this.onMove = action;
    }

    @Override
    public boolean isListeningForActions() {
        return this.listening;
    }

    @Override
    public void setListeningForActions(boolean listening) {
        if(this.listening == listening) return;
        this.listening = listening;
        InventoryManager.INSTANCE.setListen(this, listening);
    }

    @CompatibilityProvided(
            paper = true,
            spigot = true
    )
    @Override
    public int close() {
        int i = 0;

        for (HumanEntity viewer : new ArrayList<>(this.getViewers())) {
            viewer.closeInventory();
            i++;
        }

        return i;
    }

    @Override
    public void handle(@NotNull InventoryOpenEvent event) {
        if(!this.listening) {
            BukkitLogger.warn("Received InventoryOpenEvent for inventory {} but listening is disabled!", this.toString());
            return;
        }
        this.onOpen.accept(event);
    }

    @Override
    public void handle(@NotNull InventoryCloseEvent event) {
        if(!this.listening) {
            BukkitLogger.warn("Received InventoryCloseEvent for inventory {} but listening is disabled!", this.toString());
            return;
        }
        this.onClose.accept(event);
    }

    @Override
    public void handle(@NotNull InventoryClickEvent event) {
        if(!this.listening) {
            BukkitLogger.warn("Received InventoryClickEvent for inventory {} but listening is disabled!", this.toString());
            return;
        }
        this.onClick.accept(event);
    }

    @Override
    public void handle(@NotNull InventoryDragEvent event) {
        if(!this.listening) {
            BukkitLogger.warn("Received InventoryDragEvent for inventory {} but listening is disabled!", this.toString());
            return;
        }
        this.onDrag.accept(event);
    }

    @Override
    public void handle(@NotNull InventoryMoveItemEvent event) {
        if(!this.listening) {
            BukkitLogger.warn("Received InventoryMoveItemEvent for inventory {} but listening is disabled!", this.toString());
            return;
        }
        this.onMove.accept(event);
    }
}
