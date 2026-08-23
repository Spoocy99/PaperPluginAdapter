package dev.spoocy.adapter.language;

import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.utils.common.text.StringUtils;
import dev.spoocy.utils.config.Config;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Set;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface GlobalTranslation {

    static GlobalTranslation createDefault() {
        return new GlobalTranslationImpl();
    }

    /**
     * Whether to force the default language for all players, ignoring their locale
     *
     * @return true if the plugin should always use the default language
     */
    boolean isForceDefaultLanguage();

    /**
     * Whether to force the default language for all players, ignoring their locale
     *
     * @param forceDefaultLanguage true if the plugin should always use the default language
     */
    void setForceDefaultLanguage(boolean forceDefaultLanguage);

    /**
     * Get the default {@link Localization} of the plugin
     * <p>
     * This locale will be used for players who do not have a specific locale
     * and as a fallback if a translation is missing in the player's locale
     *
     * @return the default locale of the plugin
     */
    Localization getDefault();

    /**
     * Set the default locale of the plugin by its locale string
     * <p>
     * This locale will be used for players who do not have a specific locale
     * and as a fallback if a translation is missing in the player's locale
     *
     * @param locale the locale name to set as default
     *
     * @return true if the locale was found and set, false otherwise
     */
    boolean setDefaultLocale(@NotNull String locale);

    /**
     * Set the default locale of the plugin
     * <p>
     * This locale will be used for players who do not have a specific locale
     * and as a fallback if a translation is missing in the player's locale
     *
     * @param defaultLocale the locale to set as default
     */
    void setDefaultLocale(@NotNull Localization defaultLocale);

    /**
     * Add a new language to the {@link GlobalTranslation}
     *
     * @param name the name of the language (locale string)
     * @param file the config file containing the translations
     * @param aliases optional aliases for the language (e.g. "en_uk" and "en_us")
     */
    void addLocale(@NotNull Config file, @NotNull String name, String... aliases);

    /**
     * Get the current locales registered in the {@link GlobalTranslation}
     *
     * @return a set of locale name
     */
    Set<String> getLocales();

    /**
     * Get a {@link Localization} by its locale string
     *
     * @param locale the locale string (e.g. "EN_en")
     *
     * @return the localization or null if not found
     */
    @Nullable
    Localization get(@NotNull String locale);

    /**
     * Get a {@link Localization} or the Default if it
     * is not found by its locale string.
     * <p>
     * If {@link GlobalTranslation#isForceDefaultLanguage()} is true,
     * the default locale will always be returned.
     *
     * @param locale the name of the locale
     *
     * @return Localization
     */
    @NotNull
    Localization getOrDefault(@NotNull String locale);

    /**
     * Get a {@link Localization} by a {@link Locale}.
     *
     * @param locale the locale
     *
     * @return Localization
     */
    @NotNull
    default Localization getOrDefault(@NotNull Locale locale) {
        return getOrDefault(getLangName(locale));
    }

      /**
     * Get a {@link Localization} or the Default if it
     * is not found by its name of the player's language.
     * <p>
     * If {@link GlobalTranslation#isForceDefaultLanguage()} is true,
     * the default locale will always be returned.
     *
     * @param player the player to get the locale from
     *
     * @return Localization
     */
    @NotNull
    Localization playerLocale(@NotNull Player player);

    static String getLangName(@NotNull Locale locale) {
        return locale.getLanguage() + (StringUtils.isNullOrEmpty(locale.getCountry()) ? "_" + locale.getCountry() : "");
    }

}
