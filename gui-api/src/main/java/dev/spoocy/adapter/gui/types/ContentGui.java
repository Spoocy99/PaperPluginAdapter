package dev.spoocy.adapter.gui.types;

import dev.spoocy.adapter.gui.content.ContentUpdateEvent;
import dev.spoocy.adapter.gui.content.GuiItemProvider;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.Coordinate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ContentGui extends Gui {

    boolean hasContentSlots();

    Coordinate[] getContentSlots();

    boolean hasContentPairSlots();

    Coordinate[] getContentPairSlots();

    boolean hasContent();

    boolean isContentHidden();

    void setContentHidden(boolean hidden);

    void updateContent();

    GuiItemProvider getContentProvider();

    void setContentFilter(@Nullable Pattern filter) throws IllegalStateException;

    void setContentItemUntilUpdate(int x, int y, @NotNull Item content);

    @Nullable
    Pattern getContentFilter();

    void setContentBackground(@Nullable Item backgroundItem);

    void onContentChange(@NotNull Consumer<ContentUpdateEvent> action);

}
