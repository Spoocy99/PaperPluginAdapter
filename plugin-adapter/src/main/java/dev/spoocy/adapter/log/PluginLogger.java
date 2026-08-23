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

    void trace(final @NotNull Marker marker, final @NotNull Component message, final @NotNull Object... args);

    void trace(final @NotNull Marker marker, final @NotNull String message, final @NotNull Object... args);

    void trace(final @NotNull  Class<?> location, final @NotNull  Component message, final @NotNull Object... args);

    void trace(final @NotNull  Class<?> location, final @NotNull  String message, final @NotNull Object... args);


    void debug(final @NotNull Marker marker, final @NotNull Component message, final @NotNull Object... args);

    void debug(final @NotNull Marker marker, final @NotNull String message, final @NotNull Object... args);

    void debug(final @NotNull  Class<?> location, final @NotNull  Component message, final @NotNull Object... args);

    void debug(final @NotNull  Class<?> location, final @NotNull  String message, final @NotNull Object... args);


    void info(final @NotNull Marker marker, final @NotNull Component message, final @NotNull Object... args);

    void info(final @NotNull Marker marker, final @NotNull String message, final @NotNull Object... args);

    void info(final @NotNull  Class<?> location, final @NotNull  Component message, final @NotNull Object... args);

    void info(final @NotNull  Class<?> location, final @NotNull  String message, final @NotNull Object... args);


    void warn(final @NotNull Marker marker, final @NotNull Component message, final @NotNull Object... args);

    void warn(final @NotNull Marker marker, final @NotNull String message, final @NotNull Object... args);

    void warn(final @NotNull  Class<?> location, final @NotNull  Component message, final @NotNull Object... args);

    void warn(final @NotNull  Class<?> location, final @NotNull  String message, final @NotNull Object... args);


    void error(final @NotNull Marker marker, final @NotNull Component message, final @NotNull Object... args);

    void error(final @NotNull Marker marker, final @NotNull String message, final @NotNull Object... args);

    void error(final @NotNull  Class<?> location, final @NotNull  Component message, final @NotNull Object... args);

    void error(final @NotNull  Class<?> location, final @NotNull  String message, final @NotNull Object... args);

}
