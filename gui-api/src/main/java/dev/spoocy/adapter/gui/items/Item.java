package dev.spoocy.adapter.gui.items;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.icon.Icon;
import dev.spoocy.adapter.gui.items.builder.BasicButton;
import dev.spoocy.adapter.gui.items.builder.BasicGuiItem;
import dev.spoocy.adapter.gui.items.builder.ControlButton;
import dev.spoocy.adapter.gui.items.builder.GuiItemBuilder;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.GuiView;
import dev.spoocy.adapter.messages.Localization;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
    ItemStack getItemStack(@NotNull Localization locale);

    /**
     * Get display item for specific view locale.
     * This method only get called when item is specifically requested by a view.
     *
     * @param view target view
     *
     * @return item stack to display
     *
     * @see #getItemStack(Localization)
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

    void subscribe(@NotNull GuiView gui);

    void unsubscribe(@NotNull GuiView gui);

    static GuiItemBuilder.BasicBuilder basic() {
        return BasicGuiItem.builder();
    }

    static GuiItemBuilder.ButtonBuilder button() {
        return BasicButton.builder();
    }

    static <T extends Gui> GuiItemBuilder.ControlButtonBuilder<T> control(@NotNull Class<T> guiType) {
        return ControlButton.builder();
    }

    static <G extends Gui> Item createNavItem(
            @NotNull Class<G> clazz,
            @NotNull Function<G, Icon> icon,
            @NotNull Function<G, Boolean> predicate,
            @NotNull Consumer<G> action
            ) {
        return control(clazz)
                .icon( icon)
                .sound(PluginConfig.clickSound())
                .errorSound(PluginConfig.errorSound())
                .run( (gui, click) -> {
                    if(predicate.apply(gui)) {
                        action.accept(gui);
                        return true;
                    }
                    return false;
                })
                .build();
    }

}
