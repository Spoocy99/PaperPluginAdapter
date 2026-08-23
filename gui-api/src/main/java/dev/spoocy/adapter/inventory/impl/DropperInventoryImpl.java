package dev.spoocy.adapter.inventory.impl;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.inventory.AbstractBukkitInventory;
import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.compatibility.annotations.CompatibilityProvided;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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

    @CompatibilityProvided(
            paper = true,
            spigot = true
    )
    @Override
    protected @NotNull Inventory createInventory(int size, @NotNull Component title) {
        return PluginConfig.compatibilityProvider().createInventory(this, InventoryType.DROPPER, title);
    }

    @Override
    public String toString() {
        return "DropperInventoryImpl{" +
                ", listening=" + listening +
                ", inventory=" + inventory +
                '}';
    }
}
