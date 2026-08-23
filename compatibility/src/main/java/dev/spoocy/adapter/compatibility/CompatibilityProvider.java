package dev.spoocy.adapter.compatibility;

import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
    default @NotNull ItemBuilder itemBuilder(@NotNull Material material) {
        return itemBuilder(new ItemStack(material));
    }

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
    Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull Component title);

    @Contract("_,_,_ -> new")
    Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull Component title);

    @Contract("_,_,_ -> new")
    <T extends Entity> T spawnEntity(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<T> function);

}
