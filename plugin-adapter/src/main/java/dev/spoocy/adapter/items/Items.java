package dev.spoocy.adapter.items;

import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import dev.spoocy.adapter.core.PluginAdapter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public final class Items {

    @Contract(" -> new")
    public static @NotNull ItemStack emptySkull() {
        try {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } catch (IllegalArgumentException e) {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
        }
    }

    @Contract("_ -> new")
    public static @NotNull ItemBuilder item(@NotNull Material material) {
        return PluginAdapter.getInstance().getCompatibilityProvider().itemBuilder(material);
    }
    @Contract("_ -> new")
    public static @NotNull ItemBuilder item(@NotNull ItemStack item) {
        return PluginAdapter.getInstance().getCompatibilityProvider().itemBuilder(item);
    }

    @Contract(" -> new")
    public static @NotNull ItemBuilder skull() {
        return item(Material.PLAYER_HEAD);
    }

    private Items() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
