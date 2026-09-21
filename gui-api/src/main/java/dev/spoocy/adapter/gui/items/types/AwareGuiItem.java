package dev.spoocy.adapter.gui.items.types;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.items.AwareItem;
import dev.spoocy.adapter.gui.items.AwareItemProvider;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.slot.Slot;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.utils.TriConsumer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * Basic Button to be put into the gui.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class AwareGuiItem<G extends Gui> extends AbstractAwareItem<G> {

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull <G extends Gui> Builder<G> builder(@NotNull Class<G> guiClass) {
        return new Builder<>(guiClass);
    }

    private final AwareItemProvider<G> itemProvider;
    private final TriConsumer<AwareItem, Click, G> action;
    private final InventoryAction[] allowedActions;
    private final ClickType[] allowedClickTypes;
    private final int updateTicks;

    public AwareGuiItem(
            @NotNull Class<G> guiClass,
            @NotNull AwareItemProvider<G> item,
            @NotNull TriConsumer<AwareItem, Click, G> action,
            @NotNull InventoryAction[] allowedActions,
            @NotNull ClickType[] allowedClickTypes,
            int updateTicks
    ) {
        super(guiClass);
        this.itemProvider = item;
        this.action = action;
        this.allowedActions = allowedActions;
        this.allowedClickTypes = allowedClickTypes;
        this.updateTicks = updateTicks;
    }

    @Override
    public ItemStack getItemStack(@NotNull Locale locale) {
        return this.itemProvider.provide(this.getGui(), locale);
    }

    @Override
    public void executeClick(@NotNull Click click) {
        this.action.accept(this, click, this.getGui());
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
        return this.updateTicks;
    }

    public static class Builder<G extends Gui> implements AwareItem.Builder<G> {

        private final Class<G> guiClass;

        private AwareItemProvider<G> itemProvider;
        private TriConsumer<AwareItem, Click, G> action;
        private InventoryAction[] allowedActions = Slot.NO_ACTIONS;
        private ClickType[] allowedClickTypes = Slot.NO_CLICK_TYPES;
        private boolean updateOnTick = false;
        private int updateTicks = Item.NO_UPDATE;
        private Consumer<AwareItem> modifier = item -> {
        };

        public Builder(@NotNull Class<G> guiClass) {
            this.guiClass = guiClass;
        }

        @Override
        public AwareItem.@NonNull Builder<G> item(@NotNull AwareItemProvider<G> provider) {
            this.itemProvider = provider;
            return this;
        }

        @Override
        public AwareItem.@NonNull Builder<G> onClick(@NotNull TriConsumer<AwareItem, Click, G> action) {
            this.action = action;
            return this;
        }

        @Override
        public @NotNull AwareItem.Builder<G> allowActions(@NotNull InventoryAction[] actions) {
            this.allowedActions = actions;
            return this;
        }

        @Override
        public @NotNull AwareItem.Builder<G> allowClickTypes(@NotNull ClickType[] types) {
            this.allowedClickTypes = types;
            return this;
        }

        @Override
        public @NotNull AwareItem.Builder<G> updateOnTick() {
            this.updateOnTick = true;
            return this;
        }

        @Override
        public @NotNull AwareItem.Builder<G> updateAfter(int ticks) {
            this.updateTicks = ticks;
            return this;
        }

        @Override
        public @NotNull AwareItem.Builder<G> addModifier(@NotNull Consumer<? super Item> modifier) {
            this.modifier = this.modifier.andThen(modifier);
            return this;
        }

        @Override
        public @NotNull AwareItem build() {
            TriConsumer<AwareItem, Click, G> clickHandler = this.action;

            if (this.updateOnTick) {
                clickHandler = clickHandler.andThen((item, click, gui) -> item.updateViews());
            }

            AwareGuiItem<G> item = new AwareGuiItem<>(
                    this.guiClass,
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
