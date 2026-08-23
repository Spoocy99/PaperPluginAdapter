package dev.spoocy.adapter.gui.items.types;

import dev.spoocy.adapter.gui.layout.slot.Slot;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class GuiButton extends GuiItem {

    @Override
    public boolean isButton() {
        return true;
    }

    @Override
    public InventoryAction[] getAllowedActions() {
        return Slot.NO_ACTIONS;
    }

    @Override
    public ClickType[] getAllowedClickTypes() {
        return Slot.NO_CLICK_TYPES;
    }
}
