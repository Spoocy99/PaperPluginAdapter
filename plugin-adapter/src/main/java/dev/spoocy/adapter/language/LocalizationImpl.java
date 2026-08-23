package dev.spoocy.adapter.language;

import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class LocalizationImpl implements Localization {
    private final String name;
    private final Config file;
    private Localization fallback;

    public LocalizationImpl(
            @NotNull String name,
            @NotNull Config file
    ) {
        this.name = name;
        this.file = file;
    }


    @Override
    public @NotNull String name() {
        return this.name;
    }

    @Override
    public @NotNull Config file() {
        return this.file;
    }

    @Override
    public @NotNull Locale getLocale() {
        try {
            return Locale.forLanguageTag(this.name);
        } catch (Exception e) {
            return Locale.ENGLISH; // Fallback to English if the locale is invalid
        }
    }

    @Override
    public void setFallback(@Nullable Localization fallback) {
        this.fallback = fallback;
    }

    @Override
    public String msg(@NotNull String key) {
        if(fallback != null && !file.isSet(key)) {
            BukkitLogger.debug("Translation key {} is missing in {}. Using default...", key, this.name);
            return this.fallback.msg(key);
        }

        return this.file.getString(key, "<red>" + key + "</red>");
    }

    @Override
    public List<String> msgList(@NotNull String key) {
        if(fallback != null && !file.isSet(key)) {
            BukkitLogger.debug("Translation key {} is missing in {}. Using default...", key, this.name);
            return this.fallback.msgList(key);
        }

        return file.isSet(key) ? file.getStringList(key) : List.of("<red>" + key + "</red>");
    }

}
