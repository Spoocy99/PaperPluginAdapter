package dev.spoocy.adapter.gui.view;

import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.slot.GuiChangeSubscriber;
import dev.spoocy.adapter.gui.saveable.ViewProvider;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.impl.DropperViewImpl;
import dev.spoocy.adapter.gui.view.impl.NormalViewImpl;
import dev.spoocy.adapter.message.LocalizedComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;

/**
 * View used to display a GUI to a single player.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface GuiView extends GuiChangeSubscriber {

    @Contract(" -> new")
    static @NotNull NormalView.Builder normal() {
        return new NormalViewImpl.Builder();
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull DropperView.Builder dropper() {
        return new DropperViewImpl.BuilderImpl();
    }

    /**
     * @return the {@link Player Viewer} for this view
     */
    @NotNull
    Player getViewer();

    /**
     * @return whether this view is open
     */
    boolean isOpen();

    /**
     * Opens this view for the {@link Player Viewer}.
     */
    void open();

    /**
     * Closes this view for the {@link Player Viewer}.
     */
    default void close() {
        close(InventoryCloseReason.EXIT);
    }

    @ApiStatus.Internal
    void close(@NotNull InventoryCloseReason reason);

    /**
     * @return the view to open when exiting this view
     *
     * @see #setExitView(Function)
     * @see #exit()
     */
    @Nullable
    GuiView getExitView();

    /**
     * Sets the view to open when exiting this view.
     *
     * @param view function that takes the current view and returns the view to open
     */
    void setExitView(@Nullable Function<GuiView, GuiView> view);

    /**
     * Exits this view by closing it and opening the view returned by {@link #getExitView()}.
     *
     * @return the view that was opened
     */
    GuiView exit();

    /**
     * @return the title of this view
     */
    @NotNull
    LocalizedComponent getTitle();

    /**
     * @return the {@link Locale language} of the view
     */
    Locale getLocale();

    /**
     * Sets the {@link Locale language} of the view.
     *
     * @param glocale the locale to set for this view
     */
    void setLocale(@NotNull Locale glocale);

    /**
     * Sets the {@link Locale language} of the view to the viewer's current locale.
     */
    void setPlayerLocale();

    /**
     * @return {@code true} if this view can be closed, {@code false} otherwise.
     */
    boolean isCloseable();

    /**
     * Sets whether this view can be closed by the player.
     *
     * @param closeable {@code true} if the view should be closeable, {@code false} otherwise
     */
    void setCloseable(boolean closeable);

    /**
     * If the displayed gui should be reset when this view is closed.
     *
     * @return {@code true} if the gui will be reset, {@code false} otherwise.
     */
    boolean isResetOnClose();

    /**
     * If the displayed gui should be reset when this view is switched from to another view.
     *
     * @return {@code true} if the gui will be reset, {@code false} otherwise.
     */
    boolean isResetOnSwitch();

    /**
     * Sets whether to reset the view when it is closed or switched to another view.
     *
     * @param onClose  whether to reset the view is closed
     * @param onSwitch whether to reset the view is closed due to switching to another view
     */
    void setResetWhen(boolean onClose, boolean onSwitch);

    /**
     * Resets the displayed gui.
     */
    void resetDisplayedGui();

    /**
     * Gets the {@link Gui} at a certain position.
     *
     * @param x the x position of the gui
     * @param y the y position of the gui
     *
     * @return the gui at the given position or {@code null} if no gui is at the given position
     */
    @Nullable
    Gui getGuiAt(int x, int y);

    /**
     * Notifies this view that an item has changed and should be redrawn.
     *
     * @param item the item to redraw
     */
    void notifyChanges(@NotNull Item item);

    /**
     * Manually Redraws the entire view.
     */
    void redraw();

    /**
     * Manually redraws the {@link Item} at a certain position
     *
     * @param x the x position of the item
     * @param y the y position of the item
     */
    void redraw(int x, int y);

    interface Builder<B extends Builder<B, G>, G extends GuiView> {

        default B title(@NotNull Component title) {
            return title(LocalizedComponent.of(title));
        }

        B title(@NotNull LocalizedComponent title);

        /**
         * Sets whether the view is closeable.
         *
         * @param closeable {@link true} if the view should be closeable, {@link false} otherwise
         *
         * @return the builder instance
         */
        B closeable(boolean closeable);

        /**
         * Sets whether to reset the view when it is closed or switched to another view.
         *
         * @param onClose  whether to reset the gui when the view is closed
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
        B locale(@NotNull Locale locale);

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

        @NotNull
        G build(@NotNull Player viewer);

        @NotNull
        default G open(@NotNull Player viewer) {
            G guiView = build(viewer);
            guiView.open();
            return guiView;
        }
    }


}
