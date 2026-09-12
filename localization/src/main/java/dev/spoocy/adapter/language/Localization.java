package dev.spoocy.adapter.language;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Localization {

    Locale DEFAULT_LOCALE = Locale.US;

    @NotNull
    static Locale parseLocale(@NotNull final Player player) {

        try {
            return player.locale();
        } catch (NoSuchMethodError ignored) {
            // not on paper
            return parseLocale(player.getLocale());
        }

    }

    @NotNull
    static Locale parseLocale(@NotNull final String string) {
        final String[] segments = string.split("_", 3); // language_country_variant
        final int length = segments.length;

        if (length == 1) {
            return new Locale(string); // language
        }

        if (length == 2) {
            return new Locale(segments[0], segments[1]); // language + country
        }

        if (length == 3) {
            return new Locale(segments[0], segments[1], segments[2]); // language + country + variant
        }

        throw new IllegalArgumentException("Invalid locale string: " + string);
    }

    /**
     * The key of this localization.
     *
     * @return The key.
     */
    @NotNull
    String key();

    default void register(@NotNull Locale locale, @NotNull String translation) {
        register(locale, new String[] {translation});
    }

    void register(@NotNull Locale locale, @NotNull String[] translation);

    void unregister(@NotNull Locale locale);

    /**
     * Checks if the localization contains a translation for the given language.
     *
     * @param locale The language to check for translation.
     *
     * @return {@code true} if the localization contains a translation for the key, {@code false} otherwise.
     */
    boolean canTranslate(@NotNull Locale locale);

    @NotNull
    default List<String> getLines(@NotNull Locale locale) {
        return getLines(locale, null, null);
    }

    @NotNull
    default List<String> getLines(@NotNull Locale locale, @Nullable Locale fallback) {
        return getLines(locale, fallback, null);
    }

    @NotNull
    List<String> getLines(@NotNull Locale locale, @Nullable Locale fallback, @Nullable String fallbackString);

    /**
     * Formats the provided key to indicate that it was not found.
     *
     * @param key The key that could not be found.
     *
     * @return A formatted string in red indicating the key was not found.
     */
    @Contract(pure = true)
    static @NotNull String notFound(@NotNull String key) {
        return "<red>" + key + "</red>";
    }

}
