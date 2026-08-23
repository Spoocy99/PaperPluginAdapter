package dev.spoocy.adapter.log;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.utils.common.misc.ClassFinder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Marker;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BukkitLogger {

    private BukkitLogger() { }

    public static void trace(@NotNull Marker marker, @NotNull String message, final @NotNull Object... args) {
        getLogger().trace(marker, message, args);
    }

    public static void trace(@NotNull Marker marker, @NotNull Component message, final @NotNull Object... args) {
        getLogger().trace(marker, message, args);
    }

    public static void trace(@NotNull String message, final @NotNull Object... args) {
        getLogger().trace(ClassFinder.caller(), message, args);
    }

    public static void trace(@NotNull Component message, final @NotNull Object... args) {
        getLogger().trace(ClassFinder.caller(), message, args);
    }

    public static void debug(@NotNull Marker marker, @NotNull String message, final @NotNull Object... args) {
        getLogger().debug(marker, message, args);
    }

    public static void debug(@NotNull Marker marker, @NotNull Component message, final @NotNull Object... args) {
        getLogger().debug(marker, message, args);
    }

    public static void debug(@NotNull String message, final @NotNull Object... args) {
        getLogger().debug(ClassFinder.caller(), message, args);
    }

    public static void debug(@NotNull Component message, final @NotNull Object... args) {
        getLogger().debug(ClassFinder.caller(), message, args);
    }

    public static void info(@NotNull Marker marker, @NotNull String message, final @NotNull Object... args) {
        getLogger().info(marker, message, args);
    }

    public static void info(@NotNull Marker marker, @NotNull Component message, final @NotNull Object... args) {
        getLogger().info(marker, message, args);
    }

    public static void info(@NotNull String message, final @NotNull Object... args) {
        getLogger().info(ClassFinder.caller(), message, args);
    }

    public static void info(@NotNull Component message, final @NotNull Object... args) {
        getLogger().info(ClassFinder.caller(), message, args);
    }

    public static void warn(@NotNull Marker marker, @NotNull String message, final @NotNull Object... args) {
        getLogger().warn(marker, message, args);
    }

    public static void warn(@NotNull Marker marker, @NotNull Component message, final @NotNull Object... args) {
        getLogger().warn(marker, message, args);
    }

    public static void warn(@NotNull String message, final @NotNull Object... args) {
        getLogger().warn(ClassFinder.caller(), message, args);
    }

    public static void warn(@NotNull Component message, final @NotNull Object... args) {
        getLogger().warn(ClassFinder.caller(), message, args);
    }

    public static void error(@NotNull Marker marker, @NotNull String message, final @NotNull Object... args) {
        getLogger().error(marker, message, args);
    }

    public static void error(@NotNull Marker marker, @NotNull Component message, final @NotNull Object... args) {
        getLogger().error(marker, message, args);
    }

    public static void error(@NotNull String message, final @NotNull Object... args) {
        getLogger().error(ClassFinder.caller(), message, args);
    }

    public static void error(@NotNull Component message, final @NotNull Object... args) {
        getLogger().error(ClassFinder.caller(), message, args);
    }


    public static PluginLogger getLogger() {
        PluginAdapter plugin = PluginAdapter.getProvider(ClassFinder.caller(3));

        if(plugin == null) {
            return new PluginLoggerImpl(Bukkit.getServer().getLogger());
        }

        return plugin.logger();
    }

}
