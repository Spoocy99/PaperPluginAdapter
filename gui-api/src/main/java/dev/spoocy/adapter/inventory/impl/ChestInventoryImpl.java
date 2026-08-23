package dev.spoocy.adapter.inventory.impl;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.inventory.AbstractBukkitInventory;
import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.compatibility.annotations.CompatibilityProvided;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ChestInventoryImpl extends AbstractBukkitInventory {

    public static ChestInventoryImpl create(
            @NotNull Component title,
            int rows
    ) {
        return new ChestInventoryImpl(title, rows, true);
    }

    private ChestInventoryImpl(@NotNull Component title, int rows, boolean register) {
        super(title, rows * 9, register);
    }

    @CompatibilityProvided(
            paper = true,
            spigot = true
    )
    @Override
    protected @NotNull Inventory createInventory(int size, @NotNull Component title) {
        return PluginConfig.compatibilityProvider().createInventory(this, size, title);
    }

    @Override
    public String toString() {
        return "ChestInventoryImpl{" +
                ", listening=" + listening +
                ", inventory=" + inventory +
                '}';
    }
}
