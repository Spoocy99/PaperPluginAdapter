package dev.spoocy.adapter.compatibility;

import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Entity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface CompatibilityProvider {

    void onLoad();

    void onEnable();

    void onDisable();

    @Contract("_ -> new")
    @NotNull ItemBuilder itemBuilder(@NotNull Material material);

    @Contract("_ -> new")
    @NotNull ItemBuilder itemBuilder(@NotNull ItemStack itemStack);

    @Contract(" -> !null")
    @NotNull AudienceProvider getAudienceProvider();

    @Contract("_ -> new")
    AdvancementAccess advancementAccess(@NotNull Advancement advancement);

    /*
     * Methods that differ between server implementations
     */
    void sendToConsole(@NotNull Component message);

    @Contract("_,_,_ -> new")
    <T extends Entity> T spawnEntity(
            @NotNull Location location,
            @NotNull Class<T> clazz,
            @Nullable Consumer<T> function
    );

    /**
     * Creates a new {@code Inventory} instance with the specified parameters.
     *
     * @param owner the {@code InventoryHolder} that owns the inventory, or {@code null} if this inventory has no owner
     * @param size  the number of slots in the inventory; must be a multiple of 9 and cannot exceed the maximum allowable size
     * @param title the {@code Component} representing the title of the inventory; must not be {@code null}
     *
     * @return a newly created {@code Inventory} instance with the provided parameters
     *
     * @throws IllegalArgumentException if the size is not a multiple of 9 or exceeds the allowable limit
     * @throws NullPointerException     if the {@code title} is {@code null}
     */
    @Contract("_,_,_ -> new")
    Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull Component title);

    /**
     * Creates a new {@code Inventory} instance with the specified parameters.
     *
     * @param owner the {@code InventoryHolder} that owns the inventory, or {@code null} if this inventory has no owner
     * @param type  the {@code InventoryType} representing the type of the inventory; must not be {@code null}
     * @param title the {@code Component} representing the title of the inventory; must not be {@code null}
     *
     * @return a newly created {@code Inventory} instance with the provided parameters
     *
     * @throws NullPointerException if {@code type} or {@code title} is {@code null}
     */
    @Contract("_,_,_ -> new")
    Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull Component title);

}
