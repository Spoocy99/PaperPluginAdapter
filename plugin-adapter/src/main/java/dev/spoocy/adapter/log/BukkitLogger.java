package dev.spoocy.adapter.log;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.utils.common.misc.ClassFinder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Marker;


/**
 * Utility class to handle logging.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class BukkitLogger {

    private static Mode MODE = Mode.PLUGIN;

    public static Mode getMode() {
        return MODE;
    }

    public static void setMode(@NotNull Mode mode) {
        BukkitLogger.MODE = mode;
    }

    public static void trace(@NotNull Marker marker, @NotNull String message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).trace(marker, message, args);
    }

    public static void trace(@NotNull Marker marker, @NotNull Component message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).trace(marker, message, args);
    }

    public static void trace(@NotNull String message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).trace(caller, message, args);
    }

    public static void trace(@NotNull Component message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).trace(caller, message, args);
    }

    public static void debug(@NotNull Marker marker, @NotNull String message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).debug(marker, message, args);
    }

    public static void debug(@NotNull Marker marker, @NotNull Component message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).debug(marker, message, args);
    }

    public static void debug(@NotNull String message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).debug(caller, message, args);
    }

    public static void debug(@NotNull Component message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).debug(caller, message, args);
    }

    public static void info(@NotNull Marker marker, @NotNull String message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).info(marker, message, args);
    }

    public static void info(@NotNull Marker marker, @NotNull Component message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).info(marker, message, args);
    }

    public static void info(@NotNull String message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).info(caller, message, args);
    }

    public static void info(@NotNull Component message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).info(caller, message, args);
    }

    public static void warn(@NotNull Marker marker, @NotNull String message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).warn(marker, message, args);
    }

    public static void warn(@NotNull Marker marker, @NotNull Component message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).warn(marker, message, args);
    }

    public static void warn(@NotNull String message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).warn(caller, message, args);
    }

    public static void warn(@NotNull Component message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).warn(caller, message, args);
    }

    public static void error(@NotNull Marker marker, @NotNull String message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).error(marker, message, args);
    }

    public static void error(@NotNull Marker marker, @NotNull Component message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).error(marker, message, args);
    }

    public static void error(@NotNull String message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).error(caller, message, args);
    }

    public static void error(@NotNull Component message, final @NotNull Object... args) {
        Class<?> caller = ClassFinder.caller();
        getLogger(caller).error(caller, message, args);
    }

    @NotNull
    public static PluginLogger getLogger(@NotNull Class<?> caller) {
        if(MODE == Mode.PLUGIN) {
            return PluginAdapter.getInstance().logger();
        }

        PluginAdapter plugin = null;

        try {
            plugin = PluginAdapter.getProvider(caller);
        } catch (Exception ignored) { }

        if(plugin == null) {
            return new PluginLoggerImpl(Bukkit.getServer().getLogger());
        }

        return plugin.logger();
    }

    public enum Mode {

        /**
         * Logger will be the providing logger of the class
         */
        PROVIDER,

        /**
         * Logger will always be the logger
         * of the current {@link PluginAdapter} instance
         */
        PLUGIN
    }

    private BukkitLogger() {
        throw new UnsupportedOperationException("Cannot instantiate.");
    }

}
