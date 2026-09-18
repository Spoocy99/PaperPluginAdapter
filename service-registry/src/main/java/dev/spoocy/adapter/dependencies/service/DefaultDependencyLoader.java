package dev.spoocy.adapter.dependencies.service;

import dev.spoocy.adapter.dependencies.*;
import dev.spoocy.adapter.dependencies.exceptions.UnresolvedDependencyException;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.log.LogAs;
import dev.spoocy.utils.common.misc.Args;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@LogAs("Dependencies")
public class DefaultDependencyLoader implements DependencyLoader {

    private final Plugin plugin;

    /**
     * All {@link Service Services} that are to beloaded via {@link #load()}.
     */
    @Nullable
    private Set<Service> services = new LinkedHashSet<>();

    /**
     * All {@link PluginDependency Plugins} that are to beloaded via {@link #load()}.
     */
    @Nullable
    private Set<PluginDependency> plugins = new LinkedHashSet<>();

    /**
     * All currently loaded {@link Dependency Dependencies}
     */
    private final Set<Dependency<?>> dependencies = ConcurrentHashMap.newKeySet();

    /**
     * Creates a new {@link DefaultDependencyLoader} instance.
     *
     * @param plugin the plugin instance
     */
    public DefaultDependencyLoader(@NotNull Plugin plugin) {
        this.plugin = Args.notNull(plugin, "plugin");
    }

    @Override
    public <T> Dependency<T> add(@NotNull Class<T> type, @NonNull T instance) {

        for (Dependency<?> dep : dependencies) {
            if (dep.type().isAssignableFrom(type) || type.isAssignableFrom(dep.type())) {
                throw new IllegalStateException("Tried to add dependency with same type as another. "
                        + "new "
                        + type.getSimpleName()
                        + " ~ "
                        + dep.type().getSimpleName());
            }
        }

        Dependency<T> loaded = new DefaultLoadedDependency<>(type, instance);
        this.dependencies.add(loaded);
        return loaded;
    }

    @Override
    public <T extends Service> void register(@NotNull T service) throws IllegalArgumentException {
        if (this.services == null) {
            throw new IllegalStateException("Dependencies already loaded.");
        }

        if (this.services.contains(service)) {
            throw new IllegalStateException("Tried to register service that is already registered. "
                    + service.getClass().getSimpleName());
        }

        this.services.add(service);
    }

    @Override
    public <D extends PluginDependency> void register(@NotNull D pluginDependency) throws IllegalArgumentException {
        if (this.plugins == null) {
            throw new IllegalStateException("Dependencies already loaded.");
        }

        if (this.plugins.contains(pluginDependency)) {
            throw new IllegalStateException("Tried to register plugin dependency that is already registered. "
                    + pluginDependency.getClass().getSimpleName());
        }

        this.plugins.add(pluginDependency);
    }

    @Override
    public <T> T get(@NotNull Class<T> type) {
        return getOptional(type)
                .orElseThrow(() -> new NoSuchElementException("No Dependency of type " + type.getSimpleName() + " exists."));
    }

    @Override
    public <T> Optional<T> getOptional(@NotNull Class<T> type) {
        return this.dependencies.stream()
                .filter(dep -> dep.is(type))
                .map(Dependency::dependency)
                .map(type::cast)
                .findFirst();
    }

    @Override
    public <T> Collection<? extends T> getAll(@NotNull Class<T> type) {
        return this.dependencies.stream()
                .filter(dep -> dep.is(type))
                .map(Dependency::dependency)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Dependency<?>> dependencies() {
        return List.copyOf(this.dependencies);
    }

    @Override
    public void load() {
        if (this.services == null || this.plugins == null) {
            throw new IllegalStateException("Dependencies already loaded.");
        }

        if(!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Dependencies need to be loaded on the main thread.");
        }

        long millis = System.currentTimeMillis();

        // first load plugins because services may depend on them
        // Order should not matter
        BukkitLogger.debug("Loading " + this.plugins.size() + " Plugin dependencies...");
        for (PluginDependency pluginDependency : this.plugins) {
            loadPlugin(pluginDependency);
        }
        this.plugins = null;
        BukkitLogger.debug("All Plugin dependencies loaded.");

        // load dependencies
        // Order matters because of dependents
        BukkitLogger.debug("Loading " + this.services.size() + " Services...");
        List<Service> ordered = Dependencies.topologicalOrder(this.services, this.dependencies);
        for (Service service : ordered) {
            loadService(service);
        }
        this.services = null;
        BukkitLogger.debug("All Services loaded.");

        BukkitLogger.debug("Dependency loading took {}ms", System.currentTimeMillis() - millis);
    }

    @Override
    public void cleanup() {
        this.dependencies.clear();

        // allow for usage again e.g. in main plugin class
        this.services = new LinkedHashSet<>();
        this.plugins = new LinkedHashSet<>();
    }

    private <T extends PluginDependency> void loadPlugin(@NotNull T dependency) {
        long millis = System.currentTimeMillis();

        String pluginName = dependency.getName();

        Plugin found = this.plugin.getServer().getPluginManager().getPlugin(pluginName);

        if (found != null) {
            // dependency found so add it

            @SuppressWarnings("unchecked")
            Class<T> type = (Class<T>) dependency.getClass();
            this.add(type, dependency);

            BukkitLogger.debug(
                    "Loaded plugin dependency '{}' in {}ms",
                    type.getSimpleName(),
                    System.currentTimeMillis() - millis
            );
            return;
        }

        if (dependency.isDownloadable()) {
            dependency.download();
        }

        if (dependency.isRequired()) {
            throw new UnresolvedDependencyException("Dependency '" + dependency.getName() + "' not found.");
        }

        // not required but not present so dont register
        BukkitLogger.debug(
                "Dependency '{}' not found.",
                dependency.getName()
        );
    }

    private void loadService(@NotNull Service service) {
        long millis = System.currentTimeMillis();

        List<Class<?>> shouldProvide = new ArrayList<>(service.provides());

        // service adds any dependencies?
        DependencyProvider provider = createDependencyProvider(
                service, dep -> {
                    shouldProvide.removeIf(expectedProvidedClass -> expectedProvidedClass.isAssignableFrom(dep.type()));
                }
        );


        // actually load the service
        service.load(provider);

        // service did not provide all expected dependencies?
        if (!shouldProvide.isEmpty()) {
            throw new UnresolvedDependencyException("Service "
                    + service.getClass().getSimpleName()
                    + " did not provide all expected dependencies, missing: "
                    + shouldProvide.stream().map(Class::getSimpleName).collect(Collectors.joining(","))
            );
        }

        BukkitLogger.debug(
                "Loaded service '{}' in {}ms",
                service.getClass().getSimpleName(),
                System.currentTimeMillis() - millis
        );
    }


    @Contract(value = "_, _ -> new", pure = true)
    private @NotNull DependencyProvider createDependencyProvider(
            @NotNull Service service,
            @NotNull Consumer<Dependency<?>> handleNewDependency
    ) {
        return new DependencyProvider() {

            final Collection<Class<?>> provides = service.provides();

            @Override
            public <T> void add(@NotNull Class<T> type, @NonNull T instance) {

                if (!provides.contains(type)) {
                    throw new IllegalStateException("Service "
                            + service.getClass().getSimpleName()
                            + " attempted to inject a dependency that is not supposed to be provided by it: "
                            + type.getSimpleName()
                            + " not in "
                            + "("
                            + provides.stream().map(Class::getSimpleName).collect(Collectors.joining(","))
                            + ")"
                    );
                }

                Dependency<?> added = DefaultDependencyLoader.this.add(type, instance);
                handleNewDependency.accept(added);
            }
        };
    }

}
