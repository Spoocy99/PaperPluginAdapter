package dev.spoocy.adapter.gui.click;

import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.GuiView;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Click {

    @NotNull
    Gui getClickedGui();

    @Nullable
    GuiView getView();

    int getX();

    int getY();

    @NotNull
    ClickType getClick();

    @NotNull
    InventoryAction getAction();

    @NotNull
    Player getPlayer();

    @Nullable
    Item getClickedItem();

    default boolean isLeftClick() {
        return getClick().isLeftClick();
    }

    default boolean isRightClick() {
        return getClick().isRightClick();
    }

    default boolean isShiftClick() {
        return getClick().isShiftClick();
    }

}
