package dev.spoocy.adapter.inventory;

import dev.spoocy.adapter.log.BukkitLogger;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class CustomInventoryWrapper implements CustomInventory {

    @Override
    public @NotNull InventoryHolder getHolder() {
        return this;
    }

    @Override
    public void open(@NotNull Player player) {
        player.openInventory(this.getInventory());
    }

    @Override
    public int getSize() {
        return getInventory().getSize();
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        return getInventory().getItem(slot);
    }

    @Override
    public void setItem(int slot, @Nullable ItemStack item) {
        BukkitLogger.trace("Setting item in slot {} of inventory of type {} to {}", slot, getType().name(), item == null ? "null" : item.getType().name());
        this.getInventory().setItem(slot, item);
    }

    @Override
    public @NotNull HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
        return this.getInventory().addItem(itemStacks);
    }

    @Override
    public @NotNull HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
        return this.getInventory().removeItem(itemStacks);
    }

    @Override
    public @Nullable ItemStack @NotNull [] getContents() {
        return this.getInventory().getContents();
    }

    @Override
    public void setContents(@Nullable ItemStack @NotNull [] itemStacks) throws IllegalArgumentException {
        this.getInventory().setContents(itemStacks);
    }

    @Override
    public @Nullable ItemStack @NotNull [] getStorageContents() {
        return this.getInventory().getStorageContents();
    }

    @Override
    public void setStorageContents(@Nullable ItemStack @NotNull [] itemStacks) throws IllegalArgumentException {
        this.getInventory().setStorageContents(itemStacks);
    }

    @Override
    public boolean contains(@NotNull Material material) throws IllegalArgumentException {
        return this.getInventory().contains(material);
    }

    @Override
    public boolean contains(@Nullable ItemStack itemStack) {
        return this.getInventory().contains(itemStack);
    }

    @Override
    public boolean contains(@NotNull Material material, int i) throws IllegalArgumentException {
        return this.getInventory().contains(material, i);
    }

    @Override
    public boolean contains(@Nullable ItemStack itemStack, int i) {
        return this.getInventory().contains(itemStack, i);
    }

    @Override
    public boolean containsAtLeast(@Nullable ItemStack itemStack, int i) {
        return this.getInventory().containsAtLeast(itemStack, i);
    }

    @Override
    public @NotNull HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack itemStack) {
        return this.getInventory().all(itemStack);
    }

    @Override
    public @NotNull HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
        return this.getInventory().all(material);
    }

    @Override
    public int first(@NotNull Material material) throws IllegalArgumentException {
        return this.getInventory().first(material);
    }

    @Override
    public int first(@NotNull ItemStack itemStack) {
        return this.getInventory().first(itemStack);
    }

    @Override
    public int firstEmpty() {
        return this.getInventory().firstEmpty();
    }

    @Override
    public boolean isEmpty() {
        return this.getInventory().isEmpty();
    }

    @Override
    public void remove(@NotNull Material material) throws IllegalArgumentException {
        getInventory().remove(material);
    }

    @Override
    public void remove(@NotNull ItemStack itemStack) {
        getInventory().remove(itemStack);
    }

    @Override
    public void clear(int i) {
        getInventory().clear(i);
    }

    @Override
    public void clear() {
        getInventory().clear();
    }

    @Override
    public @NotNull List<HumanEntity> getViewers() {

        List<HumanEntity> viewers;

        try {
            viewers = getInventory().getViewers();
        } catch (Throwable e) {
            viewers = null;
            BukkitLogger.error("Failed to get viewers for inventory of type {}: ", getType().name(), e);
        }

        return viewers == null ? List.of() : viewers;
    }

    @Override
    public @NotNull InventoryType getType() {
        return getInventory().getType();
    }

    @Override
    public abstract @NotNull Inventory getInventory();


}

