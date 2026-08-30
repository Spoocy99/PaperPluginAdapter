package dev.spoocy.adapter.gui.types;

import dev.spoocy.adapter.gui.animation.Animation;
import dev.spoocy.adapter.gui.implementations.ListGuiImpl;
import dev.spoocy.adapter.gui.implementations.PageGuiImpl;
import dev.spoocy.adapter.gui.implementations.ScrollGuiImpl;
import dev.spoocy.adapter.gui.implementations.SimpleGuiImpl;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.layout.builder.Layout;
import dev.spoocy.adapter.gui.layout.slot.GuiChangeSubscriber;
import dev.spoocy.adapter.gui.view.GuiView;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Gui {

    /**
     * {@link SimpleGuiImpl}
     * <br>
     * A simple gui is a static gui that does not support
     * scrolling or pagination.
     */
    static SimpleGuiImpl.Builder simple(int width, int height) {
        return new SimpleGuiImpl.Builder(width, height);
    }

    static SimpleGuiImpl.Builder simple(int width, int height, char placeholder) {
        return new SimpleGuiImpl.Builder(width, height, placeholder);
    }

    static SimpleGuiImpl.Builder simple(@NotNull String structure, @NotNull String... additional) {
        return new SimpleGuiImpl.Builder(structure, additional);
    }

    /**
     * {@link ScrollGuiImpl}
     * <br>
     * A scroll gui is a gui that supports scrolling through a
     * list of items.
     */
    static ScrollGuiImpl.Builder scroll(int width, int height) {
        return new ScrollGuiImpl.Builder(width, height);
    }

    static ScrollGuiImpl.Builder scroll(int width, int height, char placeholder) {
        return new ScrollGuiImpl.Builder(width, height, placeholder);
    }

    static ScrollGuiImpl.Builder scroll(@NotNull String structure, @NotNull String... additional) {
        return new ScrollGuiImpl.Builder(structure, additional);
    }

    /**
     * {@link ListGuiImpl}
     * <br>
     * A list gui is a gui that displays a list of items, allowing
     * for easy navigation and selection through the items.
     */
    static ListGuiImpl.Builder list(int width, int height) {
        return new ListGuiImpl.Builder(width, height);
    }

    static ListGuiImpl.Builder list(int width, int height, char placeholder) {
        return new ListGuiImpl.Builder(width, height, placeholder);
    }

    static ListGuiImpl.Builder list(@NotNull String structure, @NotNull String... additional) {
        return new ListGuiImpl.Builder(structure, additional);
    }

    /**
     * {@link PageGuiImpl}
     * <br>
     * A page gui is a gui that supports pagination.
     */
    static PageGuiImpl.Builder page(int width, int height) {
        return new PageGuiImpl.Builder(width, height);
    }

    static PageGuiImpl.Builder page(int width, int height, char placeholder) {
        return new PageGuiImpl.Builder(width, height, placeholder);
    }

    static PageGuiImpl.Builder page(@NotNull String structure, @NotNull String... additional) {
        return new PageGuiImpl.Builder(structure, additional);
    }

    int getWidth();

    int getHeight();

    Item getBackground();

    void setFrozen(boolean frozen);

    boolean isFrozen();

    void applyLayout(@NotNull Layout layout);

    void fill(@NotNull Item item, boolean replaceExisting);

    void fillRow(int row, @NotNull Item item, boolean replaceExisting);

    void fillColumn(int column, @NotNull Item item, boolean replaceExisting);

    default void fillBorders(@NotNull Item item, boolean replaceExisting) {
        fillRectangle(0, 0, getWidth(), getHeight(), item, replaceExisting);
    }

    void fillRectangle(int x, int y, int width, int height, @NotNull Item item, boolean replaceExisting);

    void setBackground(@Nullable Item backgroundItem);

    boolean containsItem(@NotNull Item item);

    /**
     * Get the item at the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate (0-indexed)
     *
     * @return the item at the given coordinates, or null if there is no item
     */
    @Nullable
    Item getItem(int x, int y);

    /**
     * Set the item at the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate (0-indexed)
     * @param item the item to set, or null to remove the item
     */
    void setItem(int x, int y, @Nullable Item item);

    /**
     * Remove the item at the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate (0-indexed)
     */
    void removeItem(int x, int y);

    boolean isInteractionAllowed(int x, int y, @NotNull ClickType clickType, @NotNull InventoryAction action);

    boolean handleClick(int x, int y, @NotNull ClickType type, @NotNull InventoryAction action, @NotNull Player player, @Nullable GuiView executor);

    default boolean handleClick(int x, int y, @NotNull InventoryClickEvent event, @Nullable GuiView executor) {
        return handleClick(x, y, event.getClick(), event.getAction(), (Player) event.getWhoClicked(), executor);
    }

    Coordinate[] retrieveCoordinates(@NotNull Item item);

    Collection<GuiChangeSubscriber> getSubscribers();

    Collection<Item> retrieveAllPresentItems();

    boolean isAnimationPlaying();

    @Nullable
    Animation<?, ?> getCurrentAnimation();

    void cancelAnimation();

    @ApiStatus.Internal
    void subscribe(@NotNull GuiChangeSubscriber view);

    @ApiStatus.Internal
    void unsubscribe(@NotNull GuiChangeSubscriber view);

    /**
     * Get all {@link Player Players} that are currently viewing this gui.
     *
     * @return a collection of all viewers.
     */
    Collection<GuiView> getViews();

    /**
     * Get all {@link Player Players} that are currently viewing this gui.
     *
     * @return an array of all viewers.
     */
    default Player[] getViewers() {
        return getViews().stream()
                .map(GuiView::getViewer)
                .toArray(Player[]::new);
    }

    /**
     * Close this gui for all {@link Player Players} that are currently viewing it.
     */
    default void closeAll() {
        for (GuiView view : getViews()) {
            view.close();
        }
    }

}
