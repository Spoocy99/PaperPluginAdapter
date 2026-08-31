package dev.spoocy.adapter.config;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.common.misc.Args;
import dev.spoocy.utils.config.*;
import dev.spoocy.utils.config.bean.ConfigBeanLoader;
import dev.spoocy.utils.config.bean.LoadStrategy;
import dev.spoocy.utils.config.constructor.Constructor;
import dev.spoocy.utils.config.io.Resource;
import dev.spoocy.utils.config.io.WriteableResource;
import dev.spoocy.utils.config.loader.ConfigLoader;
import dev.spoocy.utils.config.loader.JsonConfigLoader;
import dev.spoocy.utils.config.loader.YamlConfigLoader;
import dev.spoocy.utils.config.representer.Representer;
import dev.spoocy.utils.config.update.ConfigUpdater;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ConfigManager extends BaseResourceResolver {

    private static final String PLUGIN_DIR_PREFIX = "plugin:";

    private final PluginAdapter plugin;

    private final Constructor constructor;
    private final Representer representer;
    private final ConfigBeanLoader configLoader;

    public ConfigManager(@NotNull PluginAdapter plugin) {
        super(plugin.getClass().getClassLoader());
        this.plugin = plugin;
        this.constructor = PluginConfig.configConstructor();
        this.representer = PluginConfig.configRepresenter();
        this.configLoader = new ConfigBeanLoader(this, this.representer, this.constructor);

        registerLoader(YamlConfigLoader.INSTANCE);
        registerLoader(JsonConfigLoader.INSTANCE);
    }

    @NotNull
    public Constructor getConstructor() {
        return this.constructor;
    }

    @NotNull
    public Representer getRepresenter() {
        return this.representer;
    }

    @NotNull
    public ConfigBeanLoader getConfigLoader() {
        return this.configLoader;
    }

    /**
     * Resolves and returns the file path within the plugin's data directory.
     *
     * @param path  The primary path segment, must not be null.
     * @param paths Additional optional path segments to be combined with the primary segment.
     *
     * @return The resolved {@code Path} object representing the full file path within the plugin's data directory.
     */
    @NotNull
    public Path getDataFilePath(@NotNull String path, String... paths) {
        String[] combined = Args.combineArgs(path, paths);
        return Path.of(this.plugin.getDataFolder().getPath(), combined);
    }

    /**
     * Retrieves a {@code Resource} representation of the file path located within the plugin's data directory.
     *
     * @param path  The primary path segment to locate the resource, must not be null.
     * @param paths Additional optional path segments to be combined with the primary segment.
     *
     * @return The corresponding {@code Resource} object for the resolved file path.
     */
    @NotNull
    public Resource getDataResource(@NotNull String path, String... paths) {
        return Resources.fromPath(getDataFilePath(path, paths));
    }

    @NotNull
    public Resource getClassPathResource(@NotNull String path, String... paths) {
        String fullPath = Path.of(path, paths)
                .toString()
                .replace('\\', '/');
        return Resources.fromJar(fullPath, this.plugin.getClass().getClassLoader());
    }

    @Override
    public @NotNull Resource resolve(@NotNull String location) {
        if (location.startsWith(PLUGIN_DIR_PREFIX)) {
            String relativePath = location.substring(PLUGIN_DIR_PREFIX.length());
            return Resources.fromPath(getDataFilePath(relativePath));
        }

        return super.resolve(location);
    }

    @NotNull
    public <T> T loadConfig(@NotNull Class<T> config) {
        return this.configLoader.load(config, LoadStrategy.SAVE_DEFAULTS_AND_RESOURCE);
    }

    public void saveConfig(@NotNull Object config) throws IOException {
        this.configLoader.save(config);
    }

    @NotNull
    public Document loadConfigFile(@NotNull Resource resource, boolean requireExists) throws IOException {
        return loadConfigFile(resource, requireExists, (ConfigUpdater) null);
    }

    @NotNull
    public Document loadConfigFile(@NotNull Resource resource, boolean requireExists, @Nullable ConfigUpdater updater) throws IOException {
        Args.notNull(resource, "resource");

        ConfigLoader<? extends Config, ?> loader = requireLoader(resource);

        String name = resource.getFilename();
        if (name == null) {
            name = resource.toString();
        }

        Config config;

        if (!resource.exists()) {

            if (requireExists) {
                throw new FileNotFoundException(resource.getFilename());
            }

            BukkitLogger.debug("Resource {} does not exist. Creating new...", resource.getFilename());
            config = loader.createEmpty();

        } else {
            BukkitLogger.debug("Resource {} exists. Reading...", resource.getFilename());
            config = loader.load(resource, this.constructor);
        }

        Document document = config.withRelation(resource);

        if (updater != null) {
            BukkitLogger.debug("Running Updater: {}", name);

            if (updater.run(document) > 0) {

                BukkitLogger.info("Updated config <h>{}</h>", name);
                Resource relation = document.getRelation();

                if (relation instanceof WriteableResource) {
                    document.save((WriteableResource) relation, this.representer);
                }

            }

        }

        return document;
    }

    public void saveConfigFile(@NotNull Config config, @NotNull WriteableResource location) throws IOException {
        Args.notNull(config, "config");
        Args.notNull(location, "location");
        config.save(location, this.representer);
    }

    public void saveConfigFile(@NotNull Document document) throws IOException {
        Args.notNull(document, "document");

        document.save(this.representer);
    }

}
