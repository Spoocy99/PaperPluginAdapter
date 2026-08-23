package dev.spoocy.adapter.gui.view;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.slot.GuiChangeSubscriber;
import dev.spoocy.adapter.gui.saveable.ViewProvider;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.impl.DropperViewImpl;
import dev.spoocy.adapter.gui.view.impl.NormalViewImpl;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.messages.PluginMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * View used to display a GUI to a single player.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface GuiView extends GuiChangeSubscriber {

    static NormalBuilder normal() {
        return new NormalViewImpl.Builder();
    }

    static DropperBuilder dropper() {
        return new DropperViewImpl.Builder();
    }

    @NotNull
    Player getViewer();

    boolean isOpen();

    void open();

    default void close() {
        close(InventoryCloseReason.EXIT);
    }

    void close(@NotNull InventoryCloseReason reason);

    @Nullable
    GuiView getExitView();

    /**
     * Sets the view to open when exiting this view.
     *
     * @param view function that takes the current view and returns the view to open
     */
    void setExitView(@Nullable Function<GuiView, GuiView> view);

    GuiView exit();

    @NotNull
    Component getTitle();

    Localization getLocale();

    void setLocale(@NotNull Localization locale);

    default void setPlayerLocale() {
        setLocale(PluginConfig.globalTranslation().playerLocale(getViewer()));
    }

    boolean isCloseable();

    void setCloseable(boolean closeable);

    boolean isResetOnClose();

    boolean isResetOnSwitch();

    /**
     * Sets whether to reset the view when it is closed or switched to another view.
     *
     * @param onClose whether to reset the view is closed
     * @param onSwitch whether to reset the view is closed due to switching to another view
     */
    void setResetWhen(boolean onClose, boolean onSwitch);

    void resetDisplayedGui();

    void redraw();

    void redraw(@NotNull Item item);

    void redraw(int x, int y);

    void onOpen(@NotNull Runnable runnable);

    void onClose(@NotNull Runnable runnable);

    @ApiStatus.Internal
    void simulateClick(@NotNull InventoryClickEvent event);

    interface SingleGuiView extends GuiView {

        @NotNull
        Gui getDisplayedGui();

        void setGui(@NotNull Gui gui);

    }

    interface NormalView extends SingleGuiView {

    }

    interface DropperView extends SingleGuiView {

    }

    interface Builder<B extends Builder<B, G>, G extends GuiView> {

        default B title(@NotNull Component title) {
            return title(localization -> title);
        }

        default B title(@NotNull PluginMessage title) {
            return title(title::cmp);
        }

        B title(@NotNull Function<Localization, Component> title);

        B closeable(boolean closeable);

        /**
         * Sets whether to reset the view when it is closed or switched to another view.
         *
         * @param onClose whether to reset the gui when the view is closed
         * @param onSwitch whether to reset the gui when the view is closed due to switching to another view
         *
         * @return builder
         */
        B resetWhen(boolean onClose, boolean onSwitch);

        /**
         * Sets the view to reset when it is closed by any means.
         *
         * @return builder
         *
         * @see #resetWhen(boolean, boolean)
         */
        default B resetOnExit() {
            return resetWhen(true, true);
        }

        /**
         * Sets the locale to use for this view.
         * This overrides the player locale set by default.
         *
         * @param locale the locale to use for this view
         *
         * @return builder
         */
        B locale(@NotNull Localization locale);

        /**
         * Sets the view to open when exiting this view.
         *
         * @param view the view to open
         *
         * @return builder
         */
        B exitView(@Nullable GuiView view);

        /**
         * Sets the view provider to use when exiting this view.
         * The provider will be used to supply the correct view.
         *
         * @param viewProvider the view provider
         *
         * @return builder
         */
        B exit(@Nullable ViewProvider<Player, ?> viewProvider);

        /**
         * Sets the view to open when exiting this view.
         *
         * @param view function that takes the current view and returns the view to open
         *
         * @return builder
         */
        B exit(@NotNull Function<GuiView, GuiView> view);

        B onOpenAction(@NotNull Runnable runnable);

        B onCloseAction(@NotNull Runnable runnable);

        @NotNull
        G build(@NotNull Player viewer);

        @NotNull
        default G open(@NotNull Player viewer) {
            G guiView = build(viewer);
            guiView.open();
            return guiView;
        }
    }

    interface SingleGuiBuilder<B extends SingleGuiBuilder<B, G>, G extends SingleGuiView> extends Builder<B, G> {

        B gui(@NotNull Gui gui);

    }

    interface NormalBuilder extends SingleGuiBuilder<NormalBuilder, NormalView> {

    }

    interface DropperBuilder extends SingleGuiBuilder<DropperBuilder, DropperView> {

    }

}
