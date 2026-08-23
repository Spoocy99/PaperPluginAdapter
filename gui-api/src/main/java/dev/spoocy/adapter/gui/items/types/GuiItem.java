package dev.spoocy.adapter.gui.items.types;

import com.google.common.collect.ImmutableList;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.view.GuiView;
import dev.spoocy.adapter.log.BukkitLogger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class GuiItem implements Item {

    // keeping track of how many views are using how many of this item
    private final Map<GuiView, Integer> itemRefCount = new HashMap<>();

    @Override
    public List<GuiView> getViews() {
        return ImmutableList.copyOf(itemRefCount.keySet());
    }

    @Override
    public int getViewCount() {
        return itemRefCount.size();
    }

    @Override
    public int getItemsInView(@NotNull GuiView view) {
        return itemRefCount.getOrDefault(view, 0);
    }

    @Override
    public void subscribe(@NotNull GuiView gui) {
        int count = itemRefCount.getOrDefault(gui, 0);
        itemRefCount.put(gui, count + 1);

        if (count == 0) {
            BukkitLogger.trace("Subscribed view {} to item {}, total views: {}", gui, this, this.getViews().size());
        }
    }

    @Override
    public void unsubscribe(@NotNull GuiView gui) {
        Integer count = itemRefCount.get(gui);
        if (count == null) return;

        if (count == 1) {
            itemRefCount.remove(gui);
            BukkitLogger.trace("Unsubscribed view {} from item {}, total views: {}", gui, this, this.getViews().size());
        } else {
            itemRefCount.put(gui, count - 1);
        }
    }

    @Override
    public void updateViews() {
        for (GuiView gui : this.getViews()) {
            gui.redraw(this);
        }
        BukkitLogger.trace("Updated item in {} views", this.getViews().size());
    }

}
