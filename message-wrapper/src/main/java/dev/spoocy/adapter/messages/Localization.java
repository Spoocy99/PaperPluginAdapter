package dev.spoocy.adapter.messages;

import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Localization {

    /**
     * The name of the localization.
     *
     * @return The name of the localization.
     */
    @NotNull
    String name();

    /**
     * The config file of the localization.
     *
     * @return The config file.
     */
    @NotNull
    Config file();

    /**
     * The {@link Locale} corresponding to this localization.
     *
     * @return The locale.
     */
    @NotNull
    Locale getLocale();

    /**
     * Sets the fallback localization.
     *
     * @param fallback The fallback localization.
     */
    void setFallback(@Nullable Localization fallback);

    /**
     * Gets a message from the localization file.
     *
     * @param key The key of the message.
     *
     * @return The message.
     */
    String msg(@NotNull String key);

    /**
     * Gets a list of messages from the localization file.
     *
     * @param key The key of the message list.
     *
     * @return The message list.
     */
    List<String> msgList(@NotNull String key);

    default String msgTrue() {
        return msg("general.true");
    }

    default String msgFalse() {
        return msg("general.false");
    }

    default String msgForward() {
        return msg("general.forward");
    }

    default String msgBackward() {
        return msg("general.backward");
    }

    default String msgUp() {
        return msg("general.up");
    }

    default String msgDown() {
        return msg("general.down");
    }

    default String msgConfirm() {
        return msg("general.confirm");
    }

    default String msgCancel() {
        return msg("general.cancel");
    }

}
