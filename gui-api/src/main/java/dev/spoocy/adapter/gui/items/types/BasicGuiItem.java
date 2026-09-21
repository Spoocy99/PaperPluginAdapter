package dev.spoocy.adapter.gui.items.types;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.items.ItemProvider;
import dev.spoocy.adapter.gui.layout.slot.Slot;
import dev.spoocy.utils.common.misc.Args;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Basic Item to be put into the gui.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class BasicGuiItem extends GuiItem {

    @Contract(value = " -> new", pure = true)
    public static @NotNull Builder builder() {
        return new Builder();
    }

    private final ItemProvider itemProvider;
    private final BiConsumer<Item, Click> action;
    private final InventoryAction[] allowedActions;
    private final ClickType[] allowedClickTypes;
    private final int updateTick;

    public BasicGuiItem(
            @NotNull ItemProvider itemProvider,
            @NotNull BiConsumer<Item, Click> action,
            @NotNull InventoryAction[] allowedActions,
            @NotNull ClickType[] allowedClickTypes,
            int updateTicks
    ) {
        this.itemProvider = Args.notNull(itemProvider, "itemProvider");
        this.action = Args.notNull(action, "action");
        this.allowedActions = Args.notNull(allowedActions, "allowedActions");
        this.allowedClickTypes = Args.notNull(allowedClickTypes, "allowedClickTypes");
        this.updateTick = updateTicks;
    }

    @Override
    public ItemStack getItemStack(@NotNull Locale locale) {
        return this.itemProvider.provide(locale);
    }

    @Override
    public void executeClick(@NotNull Click click) {
        this.action.accept(this, click);
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
    public int getUpdateTick() {
        return this.updateTick;
    }

    @Override
    public String toString() {
        return "BasicGuiItem{" +
                "action=" + action +
                ", itemProvider=" + itemProvider +
                ", allowedActions=" + Arrays.toString(allowedActions) +
                ", allowedClickTypes=" + Arrays.toString(allowedClickTypes) +
                ", updateTick=" + updateTick +
                '}';
    }

    public static class Builder implements Item.Builder<Builder> {

        private ItemProvider itemProvider;
        private BiConsumer<Item, Click> action;
        private InventoryAction[] allowedActions = Slot.NO_ACTIONS;
        private ClickType[] allowedClickTypes = Slot.NO_CLICK_TYPES;
        private boolean updateOnTick = false;
        private int updateTicks = Item.NO_UPDATE;
        private Consumer<Item> modifier = item -> {};

        @Override
        public @NotNull Builder item(@NotNull ItemProvider provider) {
            this.itemProvider = provider;
            return this;
        }

        @Override
        public @NotNull Builder onClick(@NotNull BiConsumer<Item, Click> action) {
            this.action = action;
            return this;
        }

        @Override
        public @NotNull Builder allowActions(@NotNull InventoryAction[] actions) {
            this.allowedActions = actions;
            return this;
        }

        @Override
        public @NotNull Builder allowClickTypes(@NotNull ClickType[] types) {
            this.allowedClickTypes = types;
            return this;
        }

        @Override
        public @NotNull Builder updateOnTick() {
            this.updateOnTick = true;
            return this;
        }

        @Override
        public @NotNull Builder updateAfter(int ticks) {
            this.updateTicks = ticks;
            return this;
        }

        @Override
        public @NotNull Builder addModifier(@NotNull Consumer<? super Item> modifier) {
            this.modifier = this.modifier.andThen(modifier);
            return this;
        }

        @Override
        public @NotNull Item build() {
            BiConsumer<Item, Click> clickHandler = this.action;

            if (this.updateOnTick) {
                clickHandler = clickHandler.andThen((item, click) -> item.updateViews());
            }

            BasicGuiItem item = new BasicGuiItem(
                    this.itemProvider,
                    clickHandler,
                    this.allowedActions,
                    this.allowedClickTypes,
                    this.updateTicks
            );

            this.modifier.accept(item);

            return item;
        }
    }

}
