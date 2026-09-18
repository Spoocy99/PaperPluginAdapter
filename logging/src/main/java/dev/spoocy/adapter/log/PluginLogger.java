package dev.spoocy.adapter.log;

import dev.spoocy.utils.common.log.LogLevel;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Marker;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface PluginLogger {

    LogLevel getLevel();

    void setLevel(@NotNull LogLevel level);

    boolean isColor();

    void setColor(boolean color);

    Component getLoggingPrefix();

    void setLoggingPrefix(@NotNull Component prefix);

    boolean isLocationAware();

    void setLocationAware(boolean locationAware);

    void sendToListeningConsoles(@NotNull Component component);

    void trace(@NotNull Marker marker, @NotNull Component message, final Object... args);

    void trace(@NotNull Marker marker, final @NotNull String message, final Object... args);

    void trace(@NotNull Class<?> location, final @NotNull Component message, final Object... args);

    void trace(@NotNull Class<?> location, final @NotNull String message, final Object... args);


    void debug(@NotNull Marker marker, @NotNull Component message, final Object... args);

    void debug(@NotNull Marker marker, @NotNull String message, final Object... args);

    void debug(@NotNull Class<?> location, @NotNull Component message, final Object... args);

    void debug(@NotNull Class<?> location, @NotNull String message, final Object... args);


    void info(@NotNull Marker marker, @NotNull Component message, final Object... args);

    void info(@NotNull Marker marker, @NotNull String message, final Object... args);

    void info(@NotNull Class<?> location, @NotNull Component message, final Object... args);

    void info(@NotNull Class<?> location, @NotNull String message, final Object... args);


    void warn(@NotNull Marker marker, @NotNull Component message, final Object... args);

    void warn(@NotNull Marker marker, @NotNull String message, final Object... args);

    void warn(@NotNull Class<?> location, @NotNull Component message, final Object... args);

    void warn(@NotNull Class<?> location, @NotNull String message, final Object... args);


    void error(@NotNull Marker marker, @NotNull Component message, final Object... args);

    void error(@NotNull Marker marker, @NotNull String message, final Object... args);

    void error(@NotNull Class<?> location, @NotNull Component message, final Object... args);

    void error(@NotNull Class<?> location, @NotNull String message, final Object... args);

}
