package dev.spoocy.adapter.config;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.common.version.Version;
import dev.spoocy.utils.config.*;
import dev.spoocy.utils.config.io.Resource;
import dev.spoocy.utils.config.update.*;
import dev.spoocy.utils.config.update.migrations.MissingFieldsMigration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BukkitConfigUpdater implements ConfigUpdater, ConfigProvider {

    private static final VersionResolver DEFAULT_RESOLVER = new PathVersionResolver("config-version", Version.ZERO);

    private final PluginAdapter plugin;
    private final VersionResolver resolver;
    private final Resource resource;
    private final MissingFieldsMigration migration;

    public BukkitConfigUpdater(@NotNull PluginAdapter plugin, @NotNull String classPath) {
        this(plugin, classPath, DEFAULT_RESOLVER, null);
    }

    public BukkitConfigUpdater(@NotNull PluginAdapter plugin, @NotNull String classPath, @NotNull VersionResolver resolver, @Nullable VersionMatcher matcher) {
        this.plugin = plugin;
        this.resolver = resolver;
        this.resource = Resources.fromJar(classPath, PluginAdapter.class.getClassLoader());
        this.migration = new MissingFieldsMigration(this, matcher, resolver);
    }

    @Override
    public @NotNull Config provide() {
        try {
            return this.plugin.loadConfig(this.resource);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public @NotNull Collection<ConfigMigration> getPossibleMigrations() {
        return List.of(this.migration);
    }

    @Override
    public int run(@NotNull ConfigSection configSection) {
        String name = null;

        if (configSection instanceof Document) {
            name = ((Document) configSection).getRelation()
                    .getFilename();
        }

        if (name == null) {
            name = "Memory[" + configSection.getName() + "]";
        }

        BukkitLogger.debug("Validating config: {}", name);
        Version current = null;

        try {
            current = resolver.resolve(configSection);
        } catch (Exception e) {
            BukkitLogger.warn("Failed to resolve current config version for {}. Migrating...", name, e);
        }

        VersionMatcher check = this.migration.fromVersion();
        if(check != null && current != null && !check.matches(current)) {
            BukkitLogger.debug("Config version '{}' for {} is not available for migration.", current, name);
            return 0;
        }

        int appliedMigrations = 0;

        if(current == null || this.migration.toVersion().isNewerThan(current)) {
            BukkitLogger.debug("Migrating config for {}", name);
            this.migration.apply(configSection);
            current = this.migration.toVersion();
            appliedMigrations++;
        }

         if (appliedMigrations > 0) {
            BukkitLogger.info("Config {} has been updated to Version {}.", name,  current);
        } else {
            BukkitLogger.debug("Config {} is up to date.", name);
        }

        return appliedMigrations;
    }
}
