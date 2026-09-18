package dev.spoocy.adapter.dependencies;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface DependencyProvider {

    /**
     * Addes a new (already loaded) dependency.
     *
     * @param type     The type
     * @param instance The instance
     * @param <T>      The type of the dependency
     */
    <T> void add(@NotNull Class<T> type, @NotNull T instance);

}
