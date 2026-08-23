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

public class ClickImpl implements Click {

    private final Gui gui;
    private final GuiView view;
    private final int x;
    private final int y;
    private final ClickType type;
    private final InventoryAction action;
    private final Player player;
    private final Item clickedItem;

    public ClickImpl(
            @NotNull Gui gui,
            @Nullable GuiView view,
            int x,
            int y,
            @NotNull ClickType type,
            @NotNull InventoryAction action,
            @NotNull Player player,
            @NotNull Item clickedItem
    ) {
        this.gui = gui;
        this.view = view;
        this.x = x;
        this.y = y;
        this.type = type;
        this.action = action;
        this.player = player;
        this.clickedItem = clickedItem;
    }

    @Override
    public @NotNull Gui getClickedGui() {
        return gui;
    }

    @Override
    public @Nullable GuiView getView() {
        return this.view;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public @NotNull ClickType getClick() {
        return type;
    }

    @Override
    public @NotNull InventoryAction getAction() {
        return action;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public Item getClickedItem() {
        return clickedItem;
    }
}
