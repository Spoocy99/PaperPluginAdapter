package dev.spoocy.adapter.dependencies;

import dev.spoocy.adapter.dependencies.exceptions.UnresolvedDependencyException;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Represents a loader for managing, registering, and loading dependencies.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public interface DependencyLoader {

    /**
     * Adds a new {@link Dependency}.
     *
     * @param type     The type of the dependency
     * @param instance The instance of the dependency
     *
     * @return The created {@link Dependency}
     *
     * @throws IllegalArgumentException if the dependency is already loaded
     */
    <T> Dependency<T> add(@NotNull Class<T> type, @NonNull T instance);

    /**
     * Registers a new {@link Service} to be loaded.
     *
     * @param service The service to register
     *
     * @throws IllegalArgumentException if the service is already loaded
     */
    <T extends Service> void register(@NotNull T service) throws IllegalArgumentException;

    /**
     * Registers a new {@link PluginDependency} to be loaded.
     *
     * @param pluginDependency The plugin dependency to register
     *
     * @throws IllegalArgumentException if the dependency is already loaded
     */
    <T extends PluginDependency> void register(@NotNull T pluginDependency) throws IllegalArgumentException;

    /**
     * Get a loaded {@code dependency instance} by its type.
     *
     * @param type The dependency type
     * @param <T>  The type of the {@code dependency}
     *
     * @return the instance
     *
     * @throws NoSuchElementException if no instance was found
     */
    <T> T get(@NotNull Class<T> type);

    /**
     * Get a loaded {@code dependency instance} by its type.
     *
     * @param type The dependency type
     * @param <T>  The type of the {@code dependency}
     *
     * @return an {@link Optional} containing the loaded instance or an empty optional if no instance was found
     */
    <T> Optional<T> getOptional(@NotNull Class<T> type);

    /**
     * Get all loaded {@code dependency instances} by its type.
     *
     * @param type The dependency type
     * @param <T>  The type of the {@code dependency}
     *
     * @return the dependencies
     */
    <T> Collection<? extends T> getAll(@NotNull Class<T> type);

    /**
     * Get all loaded dependencies.
     */
    Collection<Dependency<?>> dependencies();

    /**
     * Loads all registered {@link Service services}.
     *
     * @throws IllegalStateException         in case of any errors
     * @throws UnresolvedDependencyException in case of missing dependencies
     */
    void load();

    /**
     * Clear all loaded dependencies.
     */
    void cleanup();

}
