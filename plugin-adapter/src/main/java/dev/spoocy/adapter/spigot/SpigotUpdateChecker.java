package dev.spoocy.adapter.spigot;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.common.version.Version;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SpigotUpdateChecker {

    private final Version currentVersion;
    private final Plugin plugin;
    private final int resourceId;
    private Version newestRelease;

    public SpigotUpdateChecker(@NotNull PluginAdapter plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
        this.currentVersion = Version.parse(plugin.getDescription().getVersion());
    }

    @NotNull
    public Version getCurrentVersion() {
        return this.currentVersion;
    }

    /**
     * Determines whether the newest version information has been fetched.
     *
     * @return {@code true} if the newest version has been successfully fetched; {@code false} otherwise.
     */
    public boolean wasNewestFetched() {
        return this.newestRelease != null;
    }

    /**
     * Fetches the newest available version if it has already been retrieved.
     * If the newest version has not been fetched yet, this method returns null.
     * <p>
     * You should probably first call {@link #retrieveNewestVersion}
     *
     * @return The newest fetched version or null.
     */
    @Nullable
    public Version getNewestVersionIfFetched() {
        if(this.newestRelease == null) {
            return null;
        }

        return this.newestRelease;
    }

    public SpigotUpdateChecker retrieveNewestVersion(@NotNull Consumer<Version> consumer) {
        getVersion(version -> {
            consumer.accept(newestRelease);
        });
        return this;
    }

    public SpigotUpdateChecker whenNewest(@NotNull Consumer<Version> consumer) {
        getVersion(version -> {
            if(this.currentVersion.equals(this.newestRelease)) {
                consumer.accept(newestRelease);
            }
        });
        return this;
    }

    public SpigotUpdateChecker whenNewer(@NotNull Consumer<Version> consumer) {
        getVersion(version -> {
            if(this.currentVersion.isNewerThan(this.newestRelease)) {
                consumer.accept(newestRelease);
            }
        });
        return this;
    }

    public SpigotUpdateChecker whenOlder(@NotNull Consumer<Version> consumer) {
        getVersion(version -> {
            if(this.currentVersion.isOlderThan(this.newestRelease)) {
                consumer.accept(newestRelease);
            }
        });
        return this;
    }

    private void getVersion(@NotNull Consumer<Version> consumer) {
        if(newestRelease == null) {
            fetchVersion(version -> {
                this.newestRelease = Version.parse(version);
                consumer.accept(newestRelease);
            });
            return;
        }

        consumer.accept(this.newestRelease);
    }

    private void fetchVersion(@NotNull Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                BukkitLogger.error("Spigot Updater failed to fetch latest version: " + exception.getMessage());
            }
        });
    }

}
