package dev.spoocy.adapter.gui.layout.slot;

import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.items.types.GuiControlItem;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.log.BukkitLogger;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SlotImpl implements Slot {

    @NotNull
    private final Gui parent;
    private final int x, y;
    @Nullable
    private Item currentItem;
    @NotNull
    private Type type;
    private InventoryAction[] allowedActions;
    private ClickType[] allowedClickTypes;

    protected SlotImpl(@NotNull Gui parent, int x, int y) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.currentItem = null;
        this.type = Type.BACKGROUND;
        this.allowedActions = Slot.NO_ACTIONS;
        this.allowedClickTypes = Slot.NO_CLICK_TYPES;
    }

    @Override
    public @NotNull Gui getGui() {
        return this.parent;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public @Nullable Item getItem() {
        return this.currentItem;
    }

    @NotNull
    public Type getType() {
        return type;
    }

    @Override
    public void setItem(@Nullable Item item, @NotNull Type type) {
        Item previousItem = this.currentItem;
        BukkitLogger.trace("Updating Slot Item (Gui: {} | X: {} | Y: {}) to {} (type: {})", this.parent, this.x, this.y, item == null ? "null" : item.getClass().getSimpleName(), type);

        if(item == null) {
            this.currentItem = null;
            this.type = type;
            this.allowedActions = NO_ACTIONS;
            this.allowedClickTypes = NO_CLICK_TYPES;

        } else {
            this.currentItem = item;
            this.type = type;
            this.allowedActions = item.getAllowedActions();
            this.allowedClickTypes = item.getAllowedClickTypes();

            if(item instanceof GuiControlItem<?>) {
                ((GuiControlItem) item).setGui(this.parent);
            }
        }

        this.parent.getSubscribers().forEach(subscriber -> subscriber.onUpdate(
                this.x,
                this.y,
                this.currentItem,
                previousItem
        ));
    }

    @Override
    public void actions(@NotNull InventoryAction... actions) {
        this.allowedActions = actions;
    }

    @Override
    public void clicks(@NotNull ClickType... clickTypes) {
        this.allowedClickTypes = clickTypes;
    }

    @Override
    public InventoryAction[] getAllowedActions() {
        return this.allowedActions;
    }

    @Override
    public ClickType[] getAllowedClickTypes() {
        return this.allowedClickTypes;
    }

    @Override
    public boolean isAllowed(@NotNull InventoryAction action) {
        for (InventoryAction allowed : getAllowedActions()) {
            if (allowed == action) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAllowed(@NotNull ClickType clickType) {
        for (ClickType allowed : getAllowedClickTypes()) {
            if (allowed == clickType) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "SlotImpl{" +
                "allowedActions=" + Arrays.toString(allowedActions) +
                ", parent=" + parent +
                ", x=" + x +
                ", y=" + y +
                ", currentItem=" + currentItem +
                ", type=" + type +
                ", allowedClickTypes=" + Arrays.toString(allowedClickTypes) +
                '}';
    }
}
