package dev.spoocy.adapter.language;

import dev.spoocy.utils.common.misc.Args;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class LocalizationImpl implements Localization {

    private final String key;
    private final Map<Locale, String[]> translations;

    public LocalizationImpl(@NotNull String key) {
        this.key = Args.notNullOrEmpty(key, "key");
        this.translations = new HashMap<>();
    }

    @Override
    public @NotNull String key() {
        return this.key;
    }

    @Override
    public boolean canTranslate(@NotNull Locale locale) {
        return this.translations.containsKey(locale);
    }

    @Override
    public void register(@NotNull Locale locale, @NotNull String[] translation) {
        this.translations.put(locale, translation);
    }

    @Override
    public void unregister(@NotNull Locale locale) {
        this.translations.remove(locale);
    }

    @Override
    public @NotNull List<String> getLines(
            @NotNull Locale locale,
            @Nullable Locale fallback,
            @Nullable String fallbackString
    ) {
        String[] args = this.translations.get(locale);

        if ((args == null || args.length < 1)
                && fallback != null) {
            args = this.translations.get(fallback);
        }

        if ((args == null || args.length < 1)
                && fallbackString != null) {
            args = new String[]{fallbackString};
        }

        if (args == null || args.length < 1) {
            args = new String[]{ Localization.notFound(this.key) };
        }

        return Arrays.asList(args);
    }

}
