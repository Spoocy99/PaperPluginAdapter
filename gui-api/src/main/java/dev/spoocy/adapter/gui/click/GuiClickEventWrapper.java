package dev.spoocy.adapter.gui.click;

import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.GuiView;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GuiClickEventWrapper implements Click {

    @NotNull
    private final Gui gui;
    @Nullable
    private final GuiView view;
    @Nullable
    private final Item item;
    private final Coordinate coordinate;
    @NotNull
    private final InventoryClickEvent event;

    public GuiClickEventWrapper(
            @NotNull Gui gui,
            @Nullable GuiView view,
            @NotNull Coordinate coordinate,
            @Nullable Item item,
            @NotNull InventoryClickEvent event
    ) {
        this.gui = gui;
        this.view = view;
        this.item = item;
        this.coordinate = coordinate;
        this.event = event;
    }

    @Override
    public @NotNull Gui getClickedGui() {
        return this.gui;
    }

    @Override
    public @Nullable GuiView getView() {
        return this.view;
    }

    @Override
    public int getX() {
        return this.coordinate.getX();
    }

    @Override
    public int getY() {
        return this.coordinate.getY();
    }

    @Override
    public @NotNull Player getPlayer() {
        return (Player) this.event.getWhoClicked();
    }

    @Override
    public @Nullable Item getClickedItem() {
        return this.item;
    }

    @Override
    public @NotNull ClickType getClick() {
        return this.event.getClick();
    }

    @Override
    public @NotNull InventoryAction getAction() {
        return this.event.getAction();
    }
}
