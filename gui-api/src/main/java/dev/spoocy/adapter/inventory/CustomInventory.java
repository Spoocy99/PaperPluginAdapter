package dev.spoocy.adapter.inventory;

import dev.spoocy.adapter.inventory.impl.ChestInventoryImpl;
import dev.spoocy.adapter.inventory.impl.DropperInventoryImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface CustomInventory extends InventoryHolder {

    static CustomInventory chest(@NotNull Component title, int rows) {
        return ChestInventoryImpl.create(title, rows);
    }

    static CustomInventory dropper(@NotNull Component title) {
        return DropperInventoryImpl.create(title);
    }

    @NotNull Component getTitle();

    int getSize();

    void setMaxStackSize(int i);

    int[] getMaxStackSizes();

    int getMaxSlotStackSize(int slot);

    void setMaxSlotStackSize(int slot, int maxStackSize);

    @Nullable
    ItemStack getItem(int slot);

    void setItem(int slot, @Nullable ItemStack item);

    @NotNull HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... itemStacks) throws IllegalArgumentException;

    @NotNull HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... itemStacks) throws IllegalArgumentException;

    @Nullable ItemStack @NotNull [] getContents();

    void setContents(@Nullable ItemStack @NotNull [] itemStacks) throws IllegalArgumentException;

    @Nullable ItemStack @NotNull [] getStorageContents();

    void setStorageContents(@Nullable ItemStack @NotNull [] itemStacks) throws IllegalArgumentException;

    boolean contains(@NotNull Material material) throws IllegalArgumentException;

    boolean contains(@Nullable ItemStack itemStack);

    boolean contains(@NotNull Material material, int i) throws IllegalArgumentException;

    boolean contains(@Nullable ItemStack itemStack, int i);

    boolean containsAtLeast(@Nullable ItemStack itemStack, int i);

    @NotNull HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException;

    @NotNull HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack itemStack);

    int first(@NotNull Material material) throws IllegalArgumentException;

    int first(@NotNull ItemStack itemStack);

    int firstEmpty();

    boolean isEmpty();

    void remove(@NotNull Material material) throws IllegalArgumentException;

    void remove(@NotNull ItemStack itemStack);

    void clear(int i);

    void clear();

    int close();

    @NotNull List<HumanEntity> getViewers();

    @NotNull InventoryType getType();

    @NotNull InventoryHolder getHolder();

    void open(@NotNull Player player);

    void onOpen(@NotNull Consumer<InventoryOpenEvent> action);

    void onClose(@NotNull Consumer<InventoryCloseEvent> action);

    void onClick(@NotNull Consumer<InventoryClickEvent> action);

    void onDrag(@NotNull Consumer<InventoryDragEvent> action);

    void onMove(@NotNull Consumer<InventoryMoveItemEvent> action);

    boolean isListeningForActions();

    void setListeningForActions(boolean listening);

    @ApiStatus.Internal
    void handle(@NotNull InventoryOpenEvent event);

    @ApiStatus.Internal
    void handle(@NotNull InventoryClickEvent event);

    @ApiStatus.Internal
    void handle(@NotNull InventoryCloseEvent event);

    @ApiStatus.Internal
    void handle(@NotNull InventoryDragEvent event);

    @ApiStatus.Internal
    void handle(@NotNull InventoryMoveItemEvent event);
}
