package dev.spoocy.adapter.gui.items;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.items.builder.BasicButton;
import dev.spoocy.adapter.gui.items.builder.BasicGuiItem;
import dev.spoocy.adapter.gui.items.builder.ControlButton;
import dev.spoocy.adapter.gui.items.builder.GuiItemBuilder;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.GuiView;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Item {

    boolean isButton();

    /**
     * Get display item for specific locale.
     *
     * @param locale target locale
     *
     * @return item stack to display
     */
    ItemStack getItemStack(@NotNull Locale locale);

    /**
     * Get display item for specific view locale.
     * This method only get called when item is specifically requested by a view.
     *
     * @param view target view
     *
     * @return item stack to display
     *
     * @see #getItemStack(Locale)
     */
    default ItemStack getItemStack(@NotNull GuiView view) {
        return getItemStack(view.getLocale());
    }

    void executeClick(@NotNull Click click);

    InventoryAction[] getAllowedActions();

    ClickType[] getAllowedClickTypes();

    List<GuiView> getViews();

    int getViewCount();

    int getItemsInView(@NotNull GuiView view);

    void updateViews();

    @ApiStatus.Internal
    void subscribe(@NotNull GuiView gui);

    @ApiStatus.Internal
    void unsubscribe(@NotNull GuiView gui);

    @Contract(value = " -> new", pure = true)
    static @NotNull GuiItemBuilder.BasicBuilder basic() {
        return BasicGuiItem.builder();
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull GuiItemBuilder.ButtonBuilder button() {
        return BasicButton.builder();
    }

    @Contract(value = " -> new", pure = true)
    static <T extends Gui> @NotNull GuiItemBuilder.ControlButtonBuilder<T> control() {
        return ControlButton.builder();
    }

}
