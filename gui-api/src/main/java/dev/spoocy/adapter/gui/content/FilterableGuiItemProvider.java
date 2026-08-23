package dev.spoocy.adapter.gui.content;

import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface FilterableGuiItemProvider extends GuiItemProvider {

    /**
     * Sets the filter pattern for this provider.
     * If the filter is null, no filtering will be applied.
     *
     * @param filter the regex pattern to filter items, or null to disable filtering
     */
    void setFilter(@Nullable Pattern filter);

    @Nullable
    Pattern getFilter();

}
