package dev.spoocy.adapter.language;

import dev.spoocy.utils.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Translation extends Translator {

    @NotNull
    MiniMessage miniMessageSerializer();

    /**
     * Get the default {@link Locale}
     *
     * @return the default locale of this translation
     */
    @Nullable
    Locale getDefaultLocale();

    /**
     * Set the default locale of this translation by its key
     * <p>
     * This locale will be used for players who do not have a specific locale
     * and as a fallback if a translation is missing in the player's locale
     *
     * @param locale the key of the localization to set as default
     */
    void setDefaultLocale(@Nullable Locale locale);

    /**
     * Get the {@link Localization} for the given key
     *
     * @param key the translation key
     *
     * @return the localization for the given key, or {@code null} if it does not exist
     */
    @Nullable
    Localization getLocalization(@Nullable String key);

    /**
     * Checks if this translator has any translations.
     *
     * @return {@link TriState#TRUE} if any, {@link TriState#NOT_SET} if unknown, or {@link TriState#FALSE} if none
     */
    @NotNull
    TriState hasAnyTranslations();

    /**
     * Checks if this translator can translate the given key and locale pair.
     *
     * @param key    the key
     * @param locale the locale
     *
     * @return {@code true} if this translator will return a non-null value
     * for either of the two {@code translate} methods
     *
     * @see Translator#translate(String, Locale)
     * @see Translator#translate(TranslatableComponent, Locale)
     */
    boolean canTranslate(@NotNull String key, @NotNull Locale locale);

    @NotNull
    List<String> getTranslation(@NotNull String key, @NotNull Locale locale);

    @NotNull
    String getTranslation(@NotNull String name, @NotNull Locale locale, @NotNull String newLineJoin);

    /**
     * @see Translator#translate(String, Locale)
     */
    @Nullable
    MessageFormat translate(@NotNull String key, @NotNull Locale locale);

    /**
     * @see Translator#translate(TranslatableComponent, Locale)
     */
    @Nullable
    Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale);

    void add(@NotNull Locale locale, @NotNull String key, @NotNull String[] values);

    void addAll(@NotNull Locale locale, @NotNull Set<String> keys, @NotNull Function<String, String[]> function);

    void addAll(@NotNull Locale locale, @NotNull Config config);

    void addAll(@NotNull Locale locale, @NotNull ResourceBundle bundle);

    void addAll(@NotNull Locale locale, @NotNull Map<String, String[]> values);

    void remove(@NotNull String key, @NotNull Locale locale);

    void removeAll(@NotNull Locale locale);

}
