package dev.spoocy.adapter.dependencies;

import org.jetbrains.annotations.NotNull;

/**
 * Some object that other objects may depend on.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Dependency<T> {

    /**
     * Checks if the type of this {@code dependency} is of the given type.
     *
     * @param type the type
     *
     * @return {@code true} if the type is assignable, {@code false} otherwise
     */
    boolean is(@NotNull Class<?> type);

    /**
     * Gets the type of this {@code dependency}.
     *
     * @return the type
     */
    @NotNull
    Class<T> type();

    /**
     * Gets the instance of this {@code dependency}.
     *
     * @return the instance
     */
    @NotNull
    T dependency();
}
