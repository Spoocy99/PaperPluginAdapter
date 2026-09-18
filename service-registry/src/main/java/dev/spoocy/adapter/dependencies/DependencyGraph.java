package dev.spoocy.adapter.dependencies;

import dev.spoocy.utils.common.misc.Args;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class DependencyGraph<T extends DependencyNode> {

    private final Map<T, Integer> inDegree;
    private final Map<T, Set<T>> dependents;

    public DependencyGraph(@NotNull Map<T, Integer> inDegree, @NotNull Map<T, Set<T>> dependents) {
        this.inDegree = Args.notNull(inDegree, "inDegree");
        this.dependents = Args.notNull(dependents, "dependents");
    }

    @NotNull
    public Map<T, Set<T>> dependents() {
        return this.dependents;
    }

    @NotNull
    public Map<T, Integer> inDegree() {
        return this.inDegree;
    }

    @NotNull
    public List<T> getNodesWithZeroInDegree() {
        return this.inDegree.entrySet().stream()
                .filter(entry -> entry.getValue() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @NotNull
    public List<T> getNodesWithPositiveInDegree() {
        return this.inDegree.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public int decrementAndGetInDegree(@NotNull T node) {
        return this.inDegree.compute(
                node, (k, v) -> {

                    if (v == null || v <= 0) {
                        throw new IllegalStateException("Cannot decrement in-degree for node: " + k);
                    }

                    return v - 1;
                }
        );
    }
}
