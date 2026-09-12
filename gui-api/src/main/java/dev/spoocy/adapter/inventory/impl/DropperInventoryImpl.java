package dev.spoocy.adapter.inventory.impl;

import dev.spoocy.adapter.inventory.AbstractBukkitInventory;
import dev.spoocy.adapter.inventory.InventoryManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class DropperInventoryImpl extends AbstractBukkitInventory {

    public static DropperInventoryImpl create(@NotNull Component title) {
        return new DropperInventoryImpl(title, true);
    }

    private DropperInventoryImpl(@NotNull Component title,  boolean register) {
        super(title, 9, register);
    }

    @Override
    protected @NotNull Inventory createInventory(int size, @NotNull Component title) {
        return InventoryManager.INSTANCE.getFactory().createInventory(this, InventoryType.DROPPER, title);
    }

    @Override
    public String toString() {
        return "DropperInventoryImpl{" +
                ", listening=" + listening +
                ", inventory=" + inventory +
                '}';
    }
}
