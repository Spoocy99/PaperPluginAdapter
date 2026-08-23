package dev.spoocy.adapter.gui.layout.slot;

import dev.spoocy.adapter.gui.items.Item;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface GuiChangeSubscriber {

    void onUpdate(int x, int y, @Nullable Item item, @Nullable Item previousItem);

}
