package dev.spoocy.adapter.gui.items.builder;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.items.types.GuiItem;
import dev.spoocy.adapter.gui.layout.slot.Slot;
import dev.spoocy.adapter.items.Head;
import dev.spoocy.adapter.messages.Localization;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Basic Item to be put into the gui.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class BasicGuiItem extends GuiItem {

    public static GuiItemBuilder.BasicBuilder builder() {
        return new Builder();
    }

    private final Function<Localization, ItemStack> item;
    private final Consumer<Click> runnable;
    private final InventoryAction[] allowedActions;
    private final ClickType[] allowedClickTypes;

    public BasicGuiItem(
            @NotNull Function<Localization, ItemStack> item,
            @NotNull Consumer<Click> handle,
            @NotNull InventoryAction[] allowedActions,
            @NotNull ClickType[] allowedClickTypes
    ) {
        this.item = item;
        this.runnable = handle;
        this.allowedActions = allowedActions;
        this.allowedClickTypes = allowedClickTypes;
    }

    @Override
    public boolean isButton() {
        return false;
    }

    @Override
    public ItemStack getItemStack(@NotNull Localization locale) {
        return this.item.apply(locale);
    }

    @Override
    public void executeClick(@NotNull Click click) {
        this.runnable.accept(click);
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
    public String toString() {
        return "BasicGuiItem{" +
                "allowedActions=" + Arrays.toString(allowedActions) +
                ", item=" + item +
                ", runnable=" + runnable +
                ", allowedClickTypes=" + Arrays.toString(allowedClickTypes) +
                '}';
    }

    private static class Builder implements GuiItemBuilder.BasicBuilder {

        private Function<Localization, ItemStack> itemStack;
        private Consumer<Click> runnable;
        private InventoryAction[] allowedActions = Slot.NO_ACTIONS;
        private ClickType[] allowedClickTypes = Slot.NO_CLICK_TYPES;

        public Builder() {
            super();
            this.itemStack = l -> Head.UNKNOWN.builder().build();
        }

        @Override
        public BasicBuilder allowInteraction(@NotNull InventoryAction... actions) {
            this.allowedActions = actions;
            return this;
        }

        @Override
        public BasicBuilder allowInteraction(@NotNull ClickType... clickTypes) {
            this.allowedClickTypes = clickTypes;
            return this;
        }

        @Override
        public BasicBuilder item(@NotNull Function<Localization, ItemStack> itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        @Override
        public BasicBuilder run(@NotNull Consumer<Click> runnable) {
            this.runnable = runnable;
            return this;
        }

        @Override
        public Item build() {
            return new BasicGuiItem(
                    this.itemStack,
                    this.runnable,
                    this.allowedActions,
                    this.allowedClickTypes
            );
        }
    }

}
