package dev.spoocy.adapter.dependencies;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface DependencyNode {

    /**
     * Specifies a set of types that this node depends on.
     *
     * @return all dependencies of this node
     */
    @Unmodifiable
    @NotNull
    Set<Class<?>> requires();

    /**
     * Specifies a set of types that this will be provided by this node.
     *
     * @return all dependencies of this node
     */
    @Unmodifiable
    @NotNull
    Set<Class<?>> provides();

}
