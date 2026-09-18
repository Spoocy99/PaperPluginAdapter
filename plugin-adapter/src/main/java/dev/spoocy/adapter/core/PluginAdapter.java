package dev.spoocy.adapter.core;

import dev.spoocy.adapter.compatibility.AudienceProvider;
import dev.spoocy.adapter.compatibility.CompatibilityProvider;
import dev.spoocy.adapter.config.ConfigManager;
import dev.spoocy.adapter.core.config.DefaultPluginConfig;
import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.core.config.PluginSetup;
import dev.spoocy.adapter.dependencies.DependencyLoader;
import dev.spoocy.adapter.dependencies.service.DefaultDependencyLoader;
import dev.spoocy.adapter.inventory.InventoryManager;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.log.LoggingPlugin;
import dev.spoocy.adapter.log.PluginLogger;
import dev.spoocy.adapter.log.PluginLoggerImpl;
import dev.spoocy.adapter.message.ActionbarHandler;
import dev.spoocy.adapter.message.GlobalTranslation;
import dev.spoocy.adapter.spigot.SpigotUpdateChecker;
import dev.spoocy.utils.common.misc.FileUtils;
import dev.spoocy.utils.common.text.StringUtils;
import dev.spoocy.utils.common.version.Version;
import dev.spoocy.utils.config.Config;
import dev.spoocy.utils.config.Document;
import dev.spoocy.utils.config.Resources;
import dev.spoocy.utils.config.io.Resource;
import dev.spoocy.utils.config.io.WriteableResource;
import dev.spoocy.utils.config.update.ConfigUpdater;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Replaces the old {@link JavaPlugin} class with a more feature-rich version.
 * Make sure to shade this dependency into your plugin!
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class PluginAdapter extends JavaPlugin implements LoggingPlugin {

    /**
     * Global instance access
     */
    private static PluginAdapter INSTANCE;

    public static PluginAdapter getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Instance call before init!");
        }

        return INSTANCE;
    }

    private void setInstance() {
        if (INSTANCE != null) {
            throw new IllegalStateException("PluginAdapter instance already set!");
        }

        INSTANCE = this;
    }

    /**
     * To-be registered listeners
     */
    @Nullable
    private List<Runnable> listeners;

    /**
     * Dependencies
     */
    @NotNull
    private final DependencyLoader dependencies;

    /**
     * Current state
     */
    @NotNull
    private State state;

    /**
     * Plugin version
     */
    private Version pluginVersion;

    /**
     * Files and config manager
     */
    private ConfigManager configManager;

    //private final SecurityManager securityManager = new SecurityManager();

    private PluginConfig configuration;
    //private SpigotUpdateChecker spigotUpdateChecker;

    /**
     * State variables
     */
    private boolean wasShutdownBefore = false;
    private boolean firstLaunch = false;
    private boolean reload = false;
    private BootError bootError;

    public PluginAdapter() {
        super();
        this.setInstance();
        this.state = State.LOAD;
        this.dependencies = new DefaultDependencyLoader(this);
    }

    @Contract(pure = true)
    @NotNull
    public final PluginConfig getConfiguration() {
        if (this.configuration == null) {
            throw new IllegalStateException(
                    "Configuration accessed before it was initialized! Make sure to only access the configuration after it was set in createPlugin() method.");
        }
        return this.configuration;
    }

    @Contract(pure = true)
    public final boolean isFirstLaunch() {
        return this.firstLaunch;
    }

    @Contract(pure = true)
    public final boolean isReload() {
        return this.reload;
    }

    @Contract(pure = true)
    @NotNull
    public final State getState() {
        return this.state;
    }

    @Contract(pure = true)
    @NotNull
    public final Version getPluginVersion() {
        if (this.pluginVersion == null) {
            throw new IllegalStateException("Call before onLoad()");
        }
        return this.pluginVersion;
    }

    @Contract(pure = true)
    @NotNull
    public final ConfigManager getConfigManager() {
        if (this.configManager == null) {
            throw new IllegalStateException("Call before onLoad()");
        }
        return this.configManager;
    }

    @Contract(pure = true)
    @NotNull
    public final DependencyLoader getDependencies() {
        return this.dependencies;
    }

    @NotNull
    public final AudienceProvider audiences() {
        return getCompatibilityProvider().getAudienceProvider();
    }

    @NotNull
    public final CompatibilityProvider getCompatibilityProvider() {
        return this.dependencies.getOptional(CompatibilityProvider.class)
                .orElseThrow(() -> new NoSuchElementException("No CompatibilityProvider set. Use dependencies#add()"));
    }

    @NotNull
    public final GlobalTranslation getGlobalTranslation() {
        return this.dependencies.getOptional(GlobalTranslation.class)
                .orElseThrow(() -> new NoSuchElementException("No GlobalTranslation set. Use dependencies#add()"));
    }

    @NotNull
    public final ActionbarHandler getActionbarHandler() {
        return this.dependencies.getOptional(ActionbarHandler.class)
                .orElseThrow(() -> new NoSuchElementException("No ActionbarHandler set. Use dependencies#add()"));
    }

    @Override
    public void onLoad() {
        checkRelocation();

        //allow for listener registration
        this.listeners = new ArrayList<>();

        // was reload?
        if (this.wasShutdownBefore) {
            this.reload = true;
        }

        // Create data folder
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            this.firstLaunch = true;
            BukkitLogger.info("First launch of plugin detected.");
        }

        // parse version from description
        String version = getDescription().getVersion();
        try {
            this.pluginVersion = Version.parse(version);
        } catch (Throwable e) {
            BukkitLogger.error("Failed to parse plugin version ({})! Plugin will shut down.", version, e);
            setError("Failed to parse plugin version: " + e.getMessage());
            return;
        }

        // Configuration
        DefaultPluginConfig config = DefaultPluginConfig.create(this);
        try {
            this.createPlugin(config);
            this.configuration = config.build();

        } catch (Exception e) {
            BukkitLogger.error("Error during plugin configuration. Plugin will shut down.", e);
            setError("Configuration Error: " + e.getMessage());
            return;
        }

        // Configuration: Files
        this.configManager = new ConfigManager(
                this,
                PluginConfig.configRepresenter(),
                PluginConfig.configConstructor()
        );

        // Configuration: Spigot Updater
        int spigotResourceId = PluginConfig.spigotResourceId();
        if (spigotResourceId > 0) {
            SpigotUpdateChecker checker = new SpigotUpdateChecker(this, spigotResourceId);
            this.dependencies.add(SpigotUpdateChecker.class, checker);
            BukkitLogger.debug("Enabled Spigot Update Checker for this plugin. Resource ID: {}", spigotResourceId);
        }

        try {
            this.handleLoad(this.dependencies);
        } catch (Exception e) {
            BukkitLogger.error("Error during plugin load phase. Plugin will shut down.", e);
            setError(e.getMessage());
        }

        try {
            this.dependencies.load();
        } catch (Exception e) {
            BukkitLogger.error("Error while loading: " + e.getMessage(), e);
            setError(e.getMessage());
            return;
        }
    }

    @Override
    public void onEnable() {
        this.state = State.ENABLED;

        if (startFailed()) {
            disablePlugin();
            return;
        }

        // enable compatibility & gui api
        getCompatibilityProvider().onEnable();
        InventoryManager.onEnable(this, this::getCompatibilityProvider);

        // Listener registration
        if (this.listeners == null) {

            // should not happen
            throw new IllegalStateException("Listeners not initialized");
        }
        this.listeners.forEach(Runnable::run);

        // no more indirect registration
        this.listeners = null;

        try {
            this.handleEnable();
        } catch (Exception e) {
            BukkitLogger.error("Error during plugin enable phase. Plugin will shut down.", e);
            setError(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        this.state = State.DISABLED;
        this.wasShutdownBefore = true;

        try {
            this.handleDisable();
        } catch (Exception e) {
            BukkitLogger.error("Error during plugin disable phase.", e);
        }

        // disable compatibility & gui api
        InventoryManager.onDisable();

        // safe cleanup
        this.dependencies.getOptional(CompatibilityProvider.class)
                        .ifPresent(CompatibilityProvider::onDisable);

        this.dependencies.cleanup();
    }

    /**
     * Called to create the plugin configuration.
     * <br> This is called before {@link #handleLoad(DependencyLoader)}.
     */
    public abstract void createPlugin(@NotNull PluginSetup config);

    /**
     * Will be called on load of the plugin.
     * <br> Most methods will return {@code null} at this point!
     */
    public abstract void handleLoad(@NotNull DependencyLoader dependencies) throws Exception;

    /**
     * Will be called when the plugin is enabled.
     */
    public abstract void handleEnable() throws Exception;

    /**
     * Will be called when the plugin is disabled.
     * <br> After this, nothing will happen until the plugin is enabled again (server restart etc.)
     */
    public abstract void handleDisable() throws Exception;

    /**
     * Checks if the plugin failed to start.
     *
     * @return {@code true} if the plugin failed to start, {@code false} otherwise.
     */
    public boolean startFailed() {
        return this.bootError != null;
    }

    /**
     * @see #setError(BootError)
     */
    public void setError(@NotNull String... error) {
        setError(new BootError(error));
    }

    /**
     * Sets a boot error for the plugin.
     * <br> If the plugin is already enabled, it will be disabled immediately.
     *
     * @param error The boot error to set.
     */
    public void setError(@NotNull BootError error) {

        if (error != this.bootError) {

            if (this.bootError != null) {
                this.bootError = BootError.combine(this.bootError, error);
            } else {
                this.bootError = error;
            }

        }

        disablePlugin();
    }

    public void disablePlugin() {
        if (this.state == State.LOAD) {
            return;
        }

        if (this.bootError != null) {
            this.bootError.printErrors(
                    Bukkit.getConsoleSender(),
                    this.getDescription()
                            .getName()
            );
        }

        getServer().getPluginManager()
                .disablePlugin(this);
    }

    private PluginLogger logger;

    @Override
    public @NotNull PluginLogger logger() {
        if (this.logger == null) {

            String prefix = this.getDescription().getPrefix();
            if (prefix == null) {
                prefix = this.getDescription().getName();
            }

            this.logger = new PluginLoggerImpl(
                    this.getLogger(),
                    prefix,
                    MiniMessage.miniMessage(),
                    PluginLoggerImpl.DEFAULT_PLAIN_SERIALIZER
            );
        }

        return this.logger;
    }

    @Override
    public @Nullable InputStream getResource(@NotNull String filename) {
        return super.getResource(filename);
    }

    @NotNull
    public Resource resource(@NotNull String path) {
        return this.configManager.resolve(path);
    }

    @NotNull
    public Resource getDataDirectory() {
        return Resources.fromPath(this.getDataFolder().getPath());
    }

    @NotNull
    public Resource getDataResource(@NotNull String path, @NotNull String... paths) {
        return this.configManager.getDataResource(path, paths);
    }

    @NotNull
    public Resource getClassPathResource(@NotNull String path, @NotNull String... paths) {
        return this.configManager.getClassPathResource(path, paths);
    }

    @NotNull
    public <T> T loadConfig(@NotNull Class<T> config) throws IOException {
        return this.configManager.loadConfig(config);
    }

    public void saveConfig(@NotNull Object config) throws IOException {
        this.configManager.saveConfig(config);
    }

    @NotNull
    public Document loadConfig(@NotNull Resource resource) throws IOException {
        return this.configManager.loadConfigFile(resource, true);
    }

    @NotNull
    public Document loadConfig(@NotNull Resource resource, boolean requireExists) throws IOException {
        return this.configManager.loadConfigFile(resource, requireExists);
    }

    @NotNull
    public Document loadConfig(@NotNull Resource resource, boolean requireExists, @Nullable ConfigUpdater updater)
            throws IOException {
        return this.configManager.loadConfigFile(resource, requireExists, updater);
    }

    public void saveConfig(@NotNull Document document) throws IOException {
        this.configManager.saveConfigFile(document);
    }

    public void saveConfig(@NotNull Config config, @NotNull WriteableResource location) throws IOException {
        this.configManager.saveConfigFile(config, location);
    }

    @Override
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        this.saveResource(resourcePath, resourcePath, replace);
    }

    public void saveResource(@NotNull String resourcePath, @NotNull String outPath, boolean replace) {
        if (StringUtils.isNullOrEmpty(resourcePath) || StringUtils.isNullOrEmpty(outPath)) {
            throw new IllegalArgumentException("Resource and out path cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = FileUtils.getResource(this.getClass(), resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + FileUtils.getJarFile(
                    this.getClass()));
        }

        File outFile = new File(this.getDataFolder(), outPath);
        int lastIndex = outPath.lastIndexOf('/');
        File outDir = new File(this.getDataFolder(), outPath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {

            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                BukkitLogger.warn("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }

        } catch (IOException ex) {
            BukkitLogger.error("Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    public void registerListener(@NotNull Listener... listeners) {
        for (Listener listener : listeners) {
            registerListener(listener);
        }
    }

    public void registerListener(@NotNull Listener listener) {
        if (!isEnabled()) {
            // safe action for later
            this.listeners.add(() -> registerListener(listener));
            return;
        }

        getServer().getPluginManager().registerEvents(listener, this);
        BukkitLogger.trace("Registered Listener Class '{}'.", listener.getClass().getSimpleName());
    }

    public void registerListener(
            @NotNull Class<? extends Event> event,
            @NotNull Listener listener,
            @NotNull EventPriority priority,
            @NotNull EventExecutor executor,
            boolean ignoreCancelled
    ) {
        if (!isEnabled()) {
            // safe action for later
            this.listeners.add(() -> registerListener(event, listener, priority, executor, ignoreCancelled));
            return;
        }

        getServer().getPluginManager().registerEvent(
                event, listener, priority, executor, this, ignoreCancelled
        );
        BukkitLogger.trace("Registered Listener Event '{}' <-- {}.", listener.getClass().getSimpleName(), event.getSimpleName());
    }

    public void unregisterListener(@NotNull Listener listener) {
        HandlerList.unregisterAll(listener);
        BukkitLogger.trace("Unregistered Listener Class '{}'.", listener.getClass().getSimpleName());
    }

    public void callEvent(@NotNull Event event) {
        try {
            getServer().getPluginManager().callEvent(event);
        } catch (IllegalPluginAccessException e) {
            BukkitLogger.error("Error while handling event: {}.", event.getClass().getSimpleName(), e);
        }
    }

    public void callEventSync(@NotNull Event event) {
        Bukkit.getScheduler().runTask(this, () -> callEvent(event));
    }

    private void checkRelocation() {
        String property = System.getProperty("pluginadapter.relocatecheck");
        if (property != null && property.equals("false")) {
            return;
        }

        String defaultPackage = new String(new byte[]{100, 101, 118, 46, 115, 112, 111, 111, 99, 121, 46, 97, 100, 97, 112, 116, 101, 114});
        String current = PluginAdapter.class.getPackage()
                .getName();
        if (current.equals(defaultPackage) || current.startsWith(defaultPackage + ".")) {
            throw new IllegalStateException(
                    "Paper Adapter has not been relocated correctly! Make sure this library is shaded and relocated in your plugin's build configuration. (Currently: " + current + ")");
        }
    }

    public enum State {
        LOAD,
        ENABLED,
        DISABLED
    }

}
