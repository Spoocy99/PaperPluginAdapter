package dev.spoocy.adapter.gui.items;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@FunctionalInterface
public interface ItemProvider {

    ItemStack provide(@NotNull Locale locale);

}
