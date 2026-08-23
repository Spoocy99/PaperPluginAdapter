package dev.spoocy.adapter.messages;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface SerializableComponent {

    /**
     * Serializes the message to a list of {@link Component Components}.
     * <p>
     * Each line in the message will be a separate component in the list.
     * <br>
     * This is useful for the lore on {@link org.bukkit.inventory.ItemStack}s as Stringified NBT does not support \n characters.
     *
     * @param locale localization to use
     * @param reset if true, the component will be reset to default color and decorations before applying any styles
     *
     * @return list of components for each line in the message
     */
    List<Component> cmpList(@NotNull Localization locale, boolean reset);

    default List<Component> cmpList(@NotNull Localization locale) {
        return cmpList(locale, true);
    }

    /**
     * Serializes the message to a single {@link Component}.
     *
     * @param locale localization to use
     * @param reset if true, the component will be reset to default color and decorations before applying any styles
     *
     * @return the component for the message
     */
    Component cmp(@NotNull Localization locale, boolean reset);

    default Component cmp(@NotNull Localization locale) {
        return cmp(locale, true);
    }

    Component cmp(@NotNull Locale locale);

    Component cmp(@NotNull Player player);

    Component cmp(@NotNull PlayerLike player);

    Component cmpDefault();

    String legacy(@NotNull Localization locale);

    String legacy(@NotNull Locale locale);

    String legacyDefault();

    String text(@NotNull Localization locale);

    String text(@NotNull Locale locale);

    String textDefault();

    String miniMessageText(@NotNull Localization locale);

    String miniMessageText(@NotNull Locale locale);

    String miniMessageTextDefault();

    /*
     * direct sending for a serialized component
     */

    void send(@NotNull Player player);

    void send(@NotNull Audience audience, @NotNull Localization locale);

    void send(@NotNull CommandSender sender);

    void sendActionbar(@NotNull Player player);

    void sendToConsole();

    void broadcast();

    void broadcastActionbar();


}
