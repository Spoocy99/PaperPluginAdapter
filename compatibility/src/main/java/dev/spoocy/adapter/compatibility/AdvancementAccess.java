package dev.spoocy.adapter.compatibility;

import net.kyori.adventure.text.Component;
import org.bukkit.advancement.AdvancementRequirements;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface AdvancementAccess {

    /**
     * Get all the criteria present in this advancement.
     *
     * @return a unmodifiable copy of all criteria
     */
    @NotNull
    Collection<String> getCriteria();

    /**
     * Returns the requirements for this advancement.
     *
     * @return an AdvancementRequirements object.
     */
    @NotNull
    AdvancementRequirements getRequirements();

    Component displayName();

    String plainDisplayName();

    /**
     * Returns whether this advancement has a display.
     *
     * @return true if it has a display, otherwise false
     */
    boolean hasDisplay();

    /**
     * Get the title of this advancement.
     *
     * @return the title component
     * @throws UnsupportedOperationException if the advancement has no display
     */
    Component title();

    String plainTitle();

    /**
     * Get the description of this advancement.
     *
     * @return the description component
     * @throws UnsupportedOperationException if the advancement has no display
     */
    Component description();

    String plainDescription();

    /**
     * Get the icon of this advancement.
     *
     * @return the icon item stack
     * @throws UnsupportedOperationException if the advancement has no display
     */
    ItemStack icon();

    /**
     * Gets whether a toast should be displayed.
     * <p>
     * A toast is a notification that will be displayed in the top right corner
     * of the screen.
     *
     * @return {@code true} if a toast should be shown
     * @throws UnsupportedOperationException if the advancement has no display
     */
    boolean shouldShowToast();

    /**
     * Gets whether a message should be sent in the chat.
     *
     * @return {@code true} if a message should be sent
     * @throws UnsupportedOperationException if the advancement has no display
     */
    boolean shouldAnnounceChat();

    /**
     * Gets whether this advancement is hidden.
     * <p>
     * Hidden advancements cannot be viewed by the player until they have been
     * unlocked.
     *
     * @return {@code true} if hidden
     * @throws UnsupportedOperationException if the advancement has no display
     */
    boolean isHidden();

}
