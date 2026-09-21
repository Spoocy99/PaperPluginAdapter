package dev.spoocy.adapter.gui.items;

import dev.spoocy.adapter.gui.types.Gui;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@FunctionalInterface
public interface AwareItemProvider<G extends Gui> {

    ItemStack provide(@NotNull G gui, @NotNull Locale locale);

}
