package dev.spoocy.adapter.gui.content;

import dev.spoocy.adapter.gui.items.Item;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import java.util.List;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface GuiItemProvider {

    GuiItemProvider EMPTY = new GuiItemProvider() {
        @Override
        public int getTotalItems() {
            return 0;
        }

        @Override
        public @NotNull List<Item> getItemList(int from, int to) {
            return List.of();
        }

        @Override
        public String toString() {
            return "GuiItemProvider.EMPTY";
        }
    };

    static GuiItemProvider of(@NotNull List<? extends Item> items) {
        return new IterableGuiItemProvider(items);
    }

    static GuiItemProvider of(@NotNull Iterable<? extends Item> items) {
        return new IterableGuiItemProvider(items);
    }

    static GuiItemProvider of(@NotNull Item first, @NotNull Item... items) {
        return new IterableGuiItemProvider(first, items);
    }

    static <T> GuiItemProvider of(@NotNull Iterable<T> items, @NotNull Function<T, Item> mapper) {
        return new IterableGuiItemProvider(items, mapper);
    }

    /**
     * @return the total number of items that this provider can provide.
     */
    @Nonnegative
    int getTotalItems();

    default boolean isEmpty() {
        return getTotalItems() == 0;
    }

    /**
     * Returns a list of {@link Item} that should be displayed in the content slots of a GUI.
     *
     * @param from the starting index (inclusive, 0 is the first item)
     * @param to   the ending index (exclusive)
     * @return a list of items
     */
    @NotNull
    List<Item> getItemList(@Nonnegative int from, @Nonnegative int to);

}
