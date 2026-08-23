package dev.spoocy.adapter.spigot;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.common.version.Version;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

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

    public SpigotUpdateChecker retrieveNewestVersion(Consumer<Version> consumer) {
        getVersion(version -> {
            consumer.accept(newestRelease);
        });
        return this;
    }

    public SpigotUpdateChecker whenNewest(final Consumer<Version> consumer) {
        getVersion(version -> {
            if(this.currentVersion.equals(this.newestRelease)) {
                consumer.accept(newestRelease);
            }
        });
        return this;
    }

    public SpigotUpdateChecker whenNewer(final Consumer<Version> consumer) {
        getVersion(version -> {
            if(this.currentVersion.isNewerThan(this.newestRelease)) {
                consumer.accept(newestRelease);
            }
        });
        return this;
    }

    public SpigotUpdateChecker whenOlder(final Consumer<Version> consumer) {
        getVersion(version -> {
            if(this.currentVersion.isOlderThan(this.newestRelease)) {
                consumer.accept(newestRelease);
            }
        });
        return this;
    }

    private void getVersion(final Consumer<Version> consumer) {
        if(newestRelease == null) {
            fetchVersion(version -> {
                this.newestRelease = Version.parse(version);
                consumer.accept(newestRelease);
            });
            return;
        }

        consumer.accept(newestRelease);
    }

    private void fetchVersion(final Consumer<String> consumer) {
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
