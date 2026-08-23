package dev.spoocy.adapter.gui.layout.slot;

import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.types.Gui;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Slot {

    InventoryAction[] ALL_ACTIONS = InventoryAction.values();
    InventoryAction[] NO_ACTIONS = new InventoryAction[0];
    ClickType[] ALL_CLICK_TYPES = ClickType.values();
    ClickType[] NO_CLICK_TYPES = new ClickType[0];

    static Slot create(@NotNull Gui gui, int x, int y) {
        return new SlotImpl(gui, x, y);
    }

    @NotNull
    Gui getGui();

    int getX();

    int getY();

    @Nullable
    Item getItem();

    @NotNull
    Type getType();

    void setItem(@Nullable Item item, @NotNull Type type);

    void actions(@NotNull InventoryAction... actions);

    default void allActions() {
        actions(ALL_ACTIONS);
    }

    void clicks(@NotNull ClickType... clickTypes);

    default void allClickTypes() {
        clicks(ALL_CLICK_TYPES);
    }

    default void empty() {
        setItem(null, Type.EMPTY);
    }

    default void handle(@NotNull Item item) {
        setItem(item, Type.HANDLE);
    }

    default void content(@NotNull Item item) {
        setItem(item, Type.CONTENT);
    }

    default void contentPair(@Nullable Item item) {
        setItem(item, Type.CONTENT_PAIR);
    }

    default void contentBackground(@Nullable Item item) {
        setItem(item, Type.CONTENT_BACKGROUND);
    }

    default void background(@Nullable Item item) {
        setItem(item, Type.BACKGROUND);
    }

    InventoryAction[] getAllowedActions();

    ClickType[] getAllowedClickTypes();

    boolean isAllowed(@NotNull InventoryAction action);

    boolean isAllowed(@NotNull ClickType clickType);

    enum Type {
        /*
         * The slot is required to be empty.
         */
        EMPTY,

        /*
         * The slot is used to display an item that is clickable.
         */
        HANDLE,

        /*
         * The slot is used to display content.
         */
        CONTENT,

        /*
         * The slot is used to display pair items of displayed content.
         */
        CONTENT_PAIR,

        /*
         * The slot is used to display content.
         */
        CONTENT_BACKGROUND,

        /*
         * The slot is used to display a background item.
         */
        BACKGROUND
    }

}
