package dev.spoocy.adapter.gui.items;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.icon.Icon;
import dev.spoocy.adapter.gui.items.types.AwareGuiItem;
import dev.spoocy.adapter.gui.items.types.BasicGuiItem;
import dev.spoocy.adapter.gui.items.types.DisplayItem;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.types.ListGui;
import dev.spoocy.adapter.gui.types.PageGui;
import dev.spoocy.adapter.gui.types.ScrollGui;
import dev.spoocy.adapter.gui.view.GuiView;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Item {

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Item display(@NotNull Icon icon) {
        return new DisplayItem(icon);
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull Builder<?> builder() {
        return BasicGuiItem.builder();
    }

    @Contract(value = "_ -> new", pure = true)
    static <T extends Gui> @NotNull AwareItem.Builder<T> aware(@NotNull Class<T> type) {
        return AwareGuiItem.builder(type);
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull AwareItem.Builder<ListGui> list() {
        return aware(ListGui.class);
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull AwareItem.Builder<PageGui> page() {
        return aware(PageGui.class);
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull AwareItem.Builder<ScrollGui> scroll() {
        return aware(ScrollGui.class);
    }

    /**
     * Item should not be updated on a specific tick.
     */
    int NO_UPDATE = -1;

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
     * This method gets called when item is specifically requested by a view.
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

    /**
     * Gets the allowed {@link InventoryAction actions}.
     */
    InventoryAction[] getAllowedActions();

    /**
     * Gets the allowed {@link ClickType click types}.
     */
    ClickType[] getAllowedClickTypes();

    /**
     * Gets the ticks after which the item should be updated
     */
    int getUpdateTick();

    /**
     * Gets all views this item is displayed in
     */
    List<GuiView> getViews();

    /**
     * @return number of views this item is displayed in
     */
    default int getViewCount() {
        return getViews().size();
    }

    /**
     * Gets the number of items displayed in the given view
     *
     * @param view the {@link GuiView view}
     *
     * @return number of views
     */
    int getItemsInView(@NotNull GuiView view);

    /**
     * Updates this item in all views it is displayed in
     */
    void updateViews();

    @ApiStatus.Internal
    void subscribe(@NotNull GuiView gui);

    @ApiStatus.Internal
    void unsubscribe(@NotNull GuiView gui);

    interface Builder<B extends Builder<B>> {

        @NotNull
        B item(@NotNull ItemProvider provider);

        @NotNull
        default B item(@NotNull Icon icon) {
            return item(icon::decode);
        }

        @NotNull
        default B item(@NotNull ItemStack item) {
            return item(locale -> item);
        }

        @NotNull
        B onClick(@NotNull BiConsumer<Item, Click> action);

        @NotNull
        B allowActions(@NotNull InventoryAction[] actions);

        @NotNull
        B allowClickTypes(@NotNull ClickType[] types);

        @NotNull
        B updateOnTick();

        @NotNull
        B updateAfter(int ticks);

        @NotNull
        B addModifier(@NotNull Consumer<? super Item> modifier);

        @NotNull
        Item build();
    }

}
