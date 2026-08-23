package dev.spoocy.adapter.gui.items.types;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.icon.Icon;
import dev.spoocy.adapter.messages.Localization;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class DisplayItem extends GuiButton {

    private final Icon icon;

    public DisplayItem(@NotNull Icon icon) {
        this.icon = icon;
    }

    @Override
    public ItemStack getItemStack(@NotNull Localization locale) {
        return this.icon.decode(locale);
    }

    @Override
    public void executeClick(@NotNull Click click) { }

    @Override
    public InventoryAction[] getAllowedActions() {
        return new InventoryAction[0];
    }

    @Override
    public ClickType[] getAllowedClickTypes() {
        return new ClickType[0];
    }

    @Override
    public String toString() {
        return "DisplayItem{" +
                "icon=" + icon +
                '}';
    }
}
