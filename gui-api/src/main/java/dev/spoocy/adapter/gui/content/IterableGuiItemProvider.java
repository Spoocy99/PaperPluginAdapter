package dev.spoocy.adapter.gui.content;

import dev.spoocy.adapter.gui.items.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class IterableGuiItemProvider implements GuiItemProvider {

    private final List<Item> items;

    public IterableGuiItemProvider(@NotNull List<? extends Item> items) {
        this.items = new LinkedList<>(items);
    }

    public IterableGuiItemProvider(@NotNull Iterable<? extends Item> items) {
        this.items = new LinkedList<>();
        items.forEach(this.items::add);
    }

    public IterableGuiItemProvider(@NotNull Item first, @NotNull Item... items) {
        this.items = new LinkedList<>();
        this.items.add(first);
        Collections.addAll(this.items, items);
    }

    public <T> IterableGuiItemProvider(@NotNull Iterable<T> items, @NotNull Function<T, Item> mapper) {
        this.items = new LinkedList<>();
        items.forEach(i -> this.items.add(mapper.apply(i)));
    }

    @Override
    public int getTotalItems() {
        return this.items.size();
    }

    @Override
    public @NotNull List<Item> getItemList(int from, int to) {
        return this.items.subList(from, Math.min(to, this.items.size()));
    }


}
