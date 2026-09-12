package dev.spoocy.adapter.log;

import dev.spoocy.utils.common.misc.ClassFinder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Marker;


/**
 * Utility class to handle logging.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class BukkitLogger {

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
        Plugin plugin = null;

        try {
            plugin = JavaPlugin.getProvidingPlugin(caller);
        } catch (Exception ignored) { }

        if(plugin == null) {
            return new PluginLoggerImpl(Bukkit.getServer().getLogger());
        }

        if(plugin instanceof LoggingPlugin) {
            return ((LoggingPlugin) plugin).logger();
        }

        return new PluginLoggerImpl(plugin.getLogger());
    }

    private BukkitLogger() {
        throw new UnsupportedOperationException("Cannot instantiate.");
    }

}
