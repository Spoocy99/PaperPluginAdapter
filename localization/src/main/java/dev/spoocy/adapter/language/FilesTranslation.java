package dev.spoocy.adapter.language;

import dev.spoocy.utils.common.misc.Args;
import dev.spoocy.utils.config.Config;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslator;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class FilesTranslation extends MiniMessageTranslator implements Translation {

    @NotNull
    private final Key key;

    @NotNull
    private final Map<String, Localization> translations = new HashMap<>();

    @NotNull
    private final MiniMessage miniMessageSerializer;

    @Nullable
    private Locale defaultLanguage = Localization.DEFAULT_LOCALE;

    public FilesTranslation(
            @NotNull Key key,
            @NotNull MiniMessage miniMessageSerializer
    ) {
        super(miniMessageSerializer);
        this.key = Args.notNull(key, "key");
        this.miniMessageSerializer = Args.notNull(miniMessageSerializer, "miniMessageSerializer");

        GlobalTranslator.translator().addSource(this);
    }

    @Override
    public @NotNull Key name() {
        return this.key;
    }

    @Override
    public @NotNull MiniMessage miniMessageSerializer() {
        return this.miniMessageSerializer;
    }

    @Override
    public Locale getDefaultLocale() {
        return this.defaultLanguage;
    }

    @Override
    public void setDefaultLocale(@Nullable Locale locale) {
        this.defaultLanguage = locale;
    }

    @Override
    public @Nullable Localization getLocalization(@Nullable String key) {
        return this.translations.get(key);
    }

    @Override
    public @NotNull TriState hasAnyTranslations() {
        if(this.translations.isEmpty()) {
            return TriState.FALSE;
        }

        return TriState.TRUE;
    }

    @Override
    public boolean canTranslate(@NotNull String key, @NotNull Locale locale) {
        Localization localization = getLocalization(key);

        if(localization == null) {
            return false;
        }

        return localization.canTranslate(locale);
    }

    @Nullable
    private List<String> getTranslationLines(@NotNull String key, @NotNull Locale locale) {
        Localization lang = getLocalization(key);

        if(lang == null) {
            return null;
        }

        return lang.getLines(locale, this.defaultLanguage, Localization.notFound(key));
    }

    @Override
    public @NotNull List<String> getTranslation(@NotNull String key, @NotNull Locale locale) {
        List<String> lines = getTranslationLines(key, locale);

        if(lines == null) {
            return List.of(Localization.notFound(key));
        }

        return lines;
    }

    @Override
    public @NotNull String getTranslation(@NotNull String key, @NotNull Locale locale, @NotNull String newLineJoin) {
        List<String> lines = getTranslationLines(key, locale);

        if(lines == null) {
            return Localization.notFound(key);
        }

        return String.join(newLineJoin, lines);
    }

    @Override
    protected @Nullable String getMiniMessageString(@NotNull String key, @NotNull Locale locale) {
        List<String> lines = getTranslationLines(key, locale);

        if(lines == null) {
            return Localization.notFound(key);
        }

        return String.join("<newline>", lines);
    }

    private Localization getOrCreate(@NotNull String key) {
        return this.translations.computeIfAbsent(key, LocalizationImpl::new);
    }

    @Override
    public void add(@NotNull Locale locale, @NotNull String key, @NotNull String[] values) {
        getOrCreate(key).register(locale, values);
    }

    @Override
    public void addAll(@NotNull Locale locale, @NotNull Set<String> keys, @NotNull Function<String, String[]> function) {
        for(String key : keys) {
            add(locale, key, function.apply(key));
        }
    }

    @Override
    public void addAll(@NotNull Locale locale, @NotNull Config config) {
        Set<String> keys = new HashSet<>(config.keys(true));

        addAll(locale, keys, key -> {

            if(config.isIterable(key)) {
                return config.getStringList(key).toArray(String[]::new);
            }

            return new String[] {config.getString(key)};
        });
    }

    @Override
    public void addAll(@NotNull Locale locale, @NotNull ResourceBundle bundle) {
        addAll(locale, bundle.keySet(), key -> new String[] {bundle.getString(key)});
    }

    @Override
    public void addAll(@NotNull Locale locale, @NotNull Map<String, String[]> values) {

        for (String key : values.keySet()) {
            Localization lang = getOrCreate(key);
            lang.register(locale, values.get(key));
        }

    }


    @Override
    public void remove(@NotNull String key, @NotNull Locale locale) {
        Localization lang = getLocalization(key);

        if(lang == null) {
            return;
        }

        lang.unregister(locale);
    }

    @Override
    public void removeAll(@NotNull Locale locale) {
        for(Localization translation : this.translations.values()) {
            translation.unregister(locale);
        }
    }
}
