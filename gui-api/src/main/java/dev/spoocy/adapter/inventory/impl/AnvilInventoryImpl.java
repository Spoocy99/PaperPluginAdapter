package dev.spoocy.adapter.inventory.impl;

import dev.spoocy.adapter.inventory.AbstractBukkitInventory;
import dev.spoocy.adapter.inventory.InventoryManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AnvilInventoryImpl extends AbstractBukkitInventory {

    @Contract("_, _ -> new")
    public static @NotNull AnvilInventoryImpl create(@NotNull Component title, boolean listening) {
        return new AnvilInventoryImpl(title, listening);
    }

    public AnvilInventoryImpl(@NotNull Component title, boolean listening) {
        super(title, 3, listening);
    }

    @Override
    protected @NotNull Inventory createInventory(int size, @NotNull Component title) {
        return InventoryManager.INSTANCE.getFactory().createInventory(this, InventoryType.ANVIL, title);
    }

    @Override
    public String toString() {
        return "AnvilInventoryImpl{" +
                ", listening=" + listening +
                ", inventory=" + inventory +
                '}';
    }

}
