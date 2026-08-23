package dev.spoocy.adapter.gui.content;

import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.types.ContentGui;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ContentUpdateEvent {

    ContentGui getGui();

    int getX();

    int getY();

    @Nullable
    Item getNewItem();

    default boolean isEmpty() {
        return getNewItem() == null;
    }

}
