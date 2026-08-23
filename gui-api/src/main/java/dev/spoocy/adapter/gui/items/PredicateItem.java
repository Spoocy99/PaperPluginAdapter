package dev.spoocy.adapter.gui.items;

import dev.spoocy.adapter.gui.click.Click;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface PredicateItem extends Item {

    /**
     * Tests the given click against a condition and executes {@link Item#executeClick(Click)} if the condition is met.
     * Otherwise, {@link #onTestFailed(Click)} is called.
     *
     * @param click The click to test.
     *
     * @return true if the condition is met, false otherwise.
     */
    boolean test(@NotNull Click click);

    /**
     * Called when the test fails.
     *
     * @param click The click that failed the test.
     */
    void onTestFailed(@NotNull Click click);

}
