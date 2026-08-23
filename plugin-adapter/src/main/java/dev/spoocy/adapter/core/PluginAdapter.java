package dev.spoocy.adapter.core;

import dev.spoocy.adapter.config.ConfigManager;
import dev.spoocy.adapter.core.config.DefaultPluginConfig;
import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.core.config.PluginSetup;
import dev.spoocy.adapter.core.load.RequirementsChecker;
import dev.spoocy.adapter.event.EventWaiter;
import dev.spoocy.adapter.log.*;
import dev.spoocy.adapter.scheduler.BukkitScheduler;
import dev.spoocy.adapter.spigot.SpigotUpdateChecker;
import dev.spoocy.utils.common.misc.FileUtils;
import dev.spoocy.utils.common.scheduler.Scheduler;
import dev.spoocy.utils.common.text.StringUtils;
import dev.spoocy.utils.common.version.Version;
import dev.spoocy.utils.config.Config;
import dev.spoocy.utils.config.Document;
import dev.spoocy.utils.config.Resources;
import dev.spoocy.utils.config.io.Resource;
import dev.spoocy.utils.config.io.WriteableResource;
import dev.spoocy.utils.config.update.ConfigUpdater;
import dev.spoocy.utils.security.CheckResult;
import dev.spoocy.utils.security.SecurityManager;
import dev.spoocy.utils.security.SecurityTest;
import dev.spoocy.utils.security.report.SecurityReport;
import dev.spoocy.utils.security.report.TestContext;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Replaces the old {@link JavaPlugin} class with a more feature-rich version.
 * Make sure to shade this dependency into your plugin!
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class PluginAdapter extends JavaPlugin {

    private static PluginAdapter INSTANCE;

    public static PluginAdapter getInstance() {
        return INSTANCE;
    }

    private void setInstance() {
        if (INSTANCE != null) {
            throw new IllegalStateException("PluginAdapter instance already set!");
        }
        INSTANCE = this;
    }

    private final List<Listener> listeners = new ArrayList<>();
    private final SecurityManager securityManager = new SecurityManager();

    private PluginConfig configuration;
    private Version pluginVersion;
    private ConfigManager configManager;
    private Scheduler bukkitScheduler;
    private EventWaiter eventWaiter;
    private SpigotUpdateChecker spigotUpdateChecker;

    private State state;
    private boolean firstLaunch = false;
    private boolean reload = false;
    private BootError bootError;
    private boolean wasShutdownBefore = false;

    public PluginAdapter() {
        super();
        this.setInstance();
        this.state = State.LOAD;
    }

    @NotNull
    public SecurityManager getSecurityManager() {
        return this.securityManager;
    }

    @NotNull
    public PluginConfig getConfiguration() {
        if (this.configuration == null) {
            throw new IllegalStateException("Configuration accessed before it was initialized! Make sure to only access the configuration after it was set in createPlugin() method.");
        }
        return this.configuration;
    }

    public boolean isFirstLaunch() {
        return this.firstLaunch;
    }

    public boolean isReload() {
        return this.reload;
    }

    public State getState() {
        return this.state;
    }

    public Version getPluginVersion() {
        return this.pluginVersion;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public Scheduler getScheduler() {
        return this.bukkitScheduler;
    }

    public EventWaiter getEventWaiter() {
        return this.eventWaiter;
    }

    @NotNull
    public SpigotUpdateChecker getSpigotUpdateChecker() {
        if (this.spigotUpdateChecker == null) {
            throw new IllegalStateException("Spigot Update Checker was not configured via the Plugin Configuration!");
        }
        return spigotUpdateChecker;
    }

    @Override
    public void onLoad() {
        checkRelocation();
        this.bukkitScheduler = new BukkitScheduler(this);

        DefaultPluginConfig config = DefaultPluginConfig.create(this);

        try {
            this.createPlugin(config);
        } catch (Exception e) {
            BukkitLogger.error("Error during plugin configuration. Plugin will shut down.", e);
            setError("Configuration Error: " + e.getMessage());
        }


        this.configuration = config.build();
        this.configManager = new ConfigManager(this);
        this.securityManager.registerTests(new RequirementsChecker(this));

        if (this.bootError != null) {
            // plugin will shutdown on enable
            return;
        }

        if (this.wasShutdownBefore) {
            this.reload = true;
        }

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            this.firstLaunch = true;
            BukkitLogger.info("First launch of plugin detected.");
        }

        String version = getDescription().getVersion();
        try {
            this.pluginVersion = Version.parse(version);
        } catch (Throwable e) {
            BukkitLogger.error("Failed to parse plugin version ({})! Plugin will shut down.", version, e);
            setError("Failed to parse plugin version: " + e.getMessage());
            return;
        }

        if (!runSecurityTests(SecurityTest.Stage.INIT)) {
            return;
        }

        this.eventWaiter = new EventWaiter(this);

        PluginConfig.compatibilityProvider().onLoad();

        int spigotResourceId = PluginConfig.spigotResourceId();
        if (spigotResourceId > 0) {
            this.spigotUpdateChecker = new SpigotUpdateChecker(this, spigotResourceId);
            BukkitLogger.debug("Enabled Spigot Update Checker for this plugin. Resource ID: {}", spigotResourceId);
        }

        try {
            this.handleLoad();
        } catch (Exception e) {
            BukkitLogger.error("Error during plugin load phase. Plugin will shut down.", e);
            setError("Loading Error: " + e.getMessage());
        }

        if (this.startFailed()) {
            return;
        }

        runSecurityTests(SecurityTest.Stage.FINISHED_LOADING);
    }

    @Override
    public void onEnable() {
        this.state = State.ENABLED;

        if (startFailed()) {
            disablePlugin();
            return;
        }

        if (!runSecurityTests(SecurityTest.Stage.READY)) {
            return;
        }

        PluginConfig.compatibilityProvider()
                .onEnable();
        this.listeners.forEach(this::registerListener);

        try {
            this.handleEnable();
        } catch (Exception e) {
            BukkitLogger.error("Error during plugin enable phase. Plugin will shut down.", e);
            setError("Enable Error: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        this.state = State.DISABLED;
        this.wasShutdownBefore = true;
        this.listeners.clear();

        if (!runSecurityTests(SecurityTest.Stage.SHUTDOWN) || startFailed()) {
            return;
        }

        try {
            this.handleDisable();
        } catch (Exception e) {
            BukkitLogger.error("Error during plugin disable phase.", e);
        }

        PluginConfig.compatibilityProvider()
                .onDisable();
    }

    /**
     * Called to create the plugin configuration.
     * <br> This is called before {@link #handleLoad()}.
     */
    public abstract void createPlugin(@NotNull PluginSetup config);

    /**
     * Will be called on load of the plugin.
     * <br> Most methods will return {@code null} at this point!
     */
    public abstract void handleLoad() throws Exception;

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
     * Logs the results of the security tests to the console.
     *
     * @param report The generated security report.
     */
    protected void logSecurityTestResult(@NotNull SecurityReport report) {
        for (TestContext context : report.getTests()) {
            switch (context.getResult()) {
                case PASSED:
                    BukkitLogger.debug(context.toString());
                    break;
                case SKIPPED:
                case WARNING:
                    BukkitLogger.warn(context.toString());
                    for (String detail : context.getDetails()) {
                        BukkitLogger.warn(" - {}", detail);
                    }
                    break;
                case ERROR:
                case KILL_PROGRAM:
                    BukkitLogger.error(context.toString());
                    for (String detail : context.getDetails()) {
                        BukkitLogger.error(" - {}", detail);
                    }
            }
        }
    }

    private boolean runSecurityTests(@NotNull SecurityTest.Stage stage) {
        SecurityReport report = this.securityManager.runTests(stage);

        logSecurityTestResult(report);

        for (TestContext context : report.getTests()) {

            if (context.getResult() == CheckResult.KILL_PROGRAM) {
                setError("Security Test Failed: " + context.getName());
                return false;
            }

        }

        return true;
    }

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

    @NotNull
    public PluginLogger logger() {
        if (this.logger == null) {
            this.logger = new PluginLoggerImpl(this);
        }
        return this.logger;
    }

    @Nullable
    public Config getRequirementsYML() {
        Resource resource = getClassPathResource("env-requirements.yml");
        if (!resource.exists()) {
            return null;
        }

        try {
            return this.loadConfig(resource, true);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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
    public Document loadConfig(@NotNull Resource resource, boolean requireExists, @NotNull String defaultsPath) throws IOException {
        return this.configManager.loadConfigFile(resource, requireExists, defaultsPath);
    }

    @NotNull
    public Document loadConfig(@NotNull Resource resource, boolean requireExists, @Nullable ConfigUpdater updater) throws IOException {
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
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + FileUtils.getJarFile(this.getClass()));
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
            this.listeners.add(listener);
            return;
        }
        getServer().getPluginManager()
                .registerEvents(listener, this);
        BukkitLogger.trace("Registered Listener Class '{}'.", listener.getClass()
                .getSimpleName());
    }

    public void unregisterListener(@NotNull Listener listener) {
        HandlerList.unregisterAll(listener);
        BukkitLogger.trace("Unregistered Listener Class '{}'.", listener.getClass()
                .getSimpleName());
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

    @Nullable
    public static PluginAdapter getProvider(@NotNull Class<?> clazz) {
        JavaPlugin provider = JavaPlugin.getProvidingPlugin(clazz);
        return provider instanceof PluginAdapter ? (PluginAdapter) provider : null;
    }

    private void checkRelocation() {
        String defaultPackage = new String(new byte[]{100, 101, 118, 46, 115, 112, 111, 111, 99, 121, 46, 97, 100, 97, 112, 116, 101, 114});
        String current = PluginAdapter.class.getPackage()
                .getName();
        if (current.equals(defaultPackage) || current.startsWith(defaultPackage + ".")) {
            throw new IllegalStateException("Paper Adapter has not been relocated correctly! Make sure this library is shaded and relocated in your plugin's build configuration. (Currently: " + current + ")");
        }
    }

    public enum State {
        LOAD,
        ENABLED,
        DISABLED
    }

}
