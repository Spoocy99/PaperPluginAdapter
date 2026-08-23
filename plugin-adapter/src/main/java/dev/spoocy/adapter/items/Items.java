package dev.spoocy.adapter.items;

import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import dev.spoocy.adapter.compatibility.items.SkullBuilder;
import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.items.skulls.SkullBuilderProvider;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public final class Items {

    private Items() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static ItemStack emptySkull() {
        try {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } catch (IllegalArgumentException e) {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
        }
    }

    public static ItemBuilder item(@NotNull Material material) {
        return PluginConfig.compatibilityProvider().itemBuilder(material);
    }
    public static ItemBuilder item(@NotNull ItemStack item) {
        return PluginConfig.compatibilityProvider().itemBuilder(item);
    }

    public static SkullBuilder skull() {
        return SkullBuilderProvider.createSkullBuilder(item(emptySkull()));
    }

}
