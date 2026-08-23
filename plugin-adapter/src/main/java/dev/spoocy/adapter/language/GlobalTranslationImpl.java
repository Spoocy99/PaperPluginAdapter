package dev.spoocy.adapter.language;

import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.utils.common.misc.FileUtils;
import dev.spoocy.utils.common.text.StringUtils;
import dev.spoocy.utils.config.Config;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GlobalTranslationImpl implements GlobalTranslation {

    private final Map<String, Localization> languages = new HashMap<>();
    private Localization defaultLanguage;
    private boolean forceDefault = false;

    public GlobalTranslationImpl() { }

    @Override
    public boolean isForceDefaultLanguage() {
        return forceDefault;
    }

    @Override
    public void setForceDefaultLanguage(boolean forceDefault) {
        this.forceDefault = forceDefault;
    }

    @Override
    public Localization getDefault() {
        checkLanguages();
        return this.defaultLanguage;
    }

    @Override
    public boolean setDefaultLocale(@NotNull String locale) {
        if (!this.languages.containsKey(locale)) {
            return false;
        }
        Localization lang = this.languages.get(locale);
        this.setDefaultLocale(lang);
        return true;
    }

    @Override
    public void setDefaultLocale(@NotNull Localization defaultLocale) {
        this.defaultLanguage = defaultLocale;
        BukkitLogger.info("Set default language to <h>" + defaultLocale.name() + "</h>");
        this.updateFallbackLanguage();
    }

    @Override
    public void addLocale(@NotNull Config file, @NotNull String name, String... aliases) {
        Localization locale = this.registerLocale(file, name, aliases);
        if (this.defaultLanguage == null) {
            this.setDefaultLocale(locale);
        }
    }

    @Override
    public Set<String> getLocales() {
        return Collections.unmodifiableSet(this.languages.keySet());
    }

    @Override
    public @Nullable Localization get(@NotNull String locale) {
        checkLanguages();
        return this.languages.get(locale);
    }

    @Override
    public @NotNull Localization getOrDefault(@NotNull String locale) {
        checkLanguages();

        if (this.forceDefault) {
            return this.defaultLanguage;
        }

        Localization language = this.languages.get(locale);
        if (language == null) {
            BukkitLogger.trace("Language " + locale + " not found! Using default language instead.");
            return this.defaultLanguage;
        }

        return language;
    }

    @Override
    public @NotNull Localization playerLocale(@NotNull Player player) {
        checkLanguages();

        if (this.forceDefault) {
            return this.defaultLanguage;
        }

        String locale;
        try {
            locale = GlobalTranslation.getLangName(player.locale());
        } catch (Throwable e) {
            locale = player.getLocale();
        }

        return this.getOrDefault(locale);
    }

    private LocalizationImpl registerLocale(@NotNull Config file, @NotNull String name, String... aliases) {
        LocalizationImpl locale = new LocalizationImpl(name, file);
        this.languages.put(name, locale);

        for (String alias : aliases) {
            if (!StringUtils.isNullOrEmpty(alias)) {
                this.languages.put(alias, locale);
            }
        }

        BukkitLogger.info("Loaded language: <h>" + locale.name() + "</h>" + (aliases.length > 0 ? "(aliases: " + String.join(", ", aliases) + ")" : ""));
        return locale;
    }

    private boolean isValid(@NotNull File file) {
        if (!file.exists()) {
            BukkitLogger.warn("Language file <red>" + file.getName() + "</red> does not exist! Skipping...");
            return false;
        }

        if (!FileUtils.getFileExtension(file).equals("yml")) {
            BukkitLogger.warn("File <red>" + file.getName() + "</red> is not a valid yml file! Skipping...");
            return false;
        }

        return true;
    }

    private void checkLanguages() {
        if (this.languages.isEmpty()) {
            throw new IllegalStateException("No languages were loaded!");
        }
    }

    private void updateFallbackLanguage() {
        String name = this.getDefault().name();
        BukkitLogger.debug("Setting fallback language to " + name);

        for (Localization language : this.languages.values()) {
            if (!language.name().equals(name)) {
                language.setFallback(this.getDefault());
            } else {
                language.setFallback(null);
            }
        }
    }

}
