package dev.spoocy.adapter.dependencies;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility for resolving dependency graphs and ordering nodes topologically.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public final class Dependencies {

    /**
     * Resolves and returns the topological order of dependency nodes.
     *
     * @param nodes  the nodes to order
     * @param loaded pre-existing loaded dependencies
     * @param <T>    the node type
     *
     * @return a list of nodes sorted in executable dependency order
     *
     * @throws IllegalStateException if a cycle is detected or dependencies are missing
     */
    @NotNull
    public static <T extends DependencyNode> List<T> topologicalOrder(
            @NotNull Collection<T> nodes,
            @NotNull Collection<Dependency<?>> loaded
    ) {
        if (nodes.isEmpty()) {
            return List.of();
        }

        List<T> ordered = new ArrayList<>(nodes.size());
        DependencyGraph<T> graph = buildDependencyGraph(nodes, loaded);
        Queue<T> queue = new ArrayDeque<>(graph.getNodesWithZeroInDegree());

        Set<Class<?>> currentlyLoadedDependencies = loaded.stream()
                .map(Dependency::type)
                .collect(Collectors.toCollection(HashSet::new));

        while (!queue.isEmpty()) {
            T current = queue.poll();

            Set<Class<?>> missingDependencies = remainingDependencyClasses(
                    current.requires(),
                    currentlyLoadedDependencies
            );

            if (!missingDependencies.isEmpty()) {
                String missingNames = missingDependencies.stream()
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(", "));

                throw new IllegalStateException("Node '"
                        + current.getClass().getSimpleName()
                        + "' is missing dependencies: ["
                        + missingNames
                        + "]"
                );
            }

            ordered.add(current);
            currentlyLoadedDependencies.addAll(current.provides());

            Set<T> dependents = graph.dependents().get(current);
            if (dependents != null) {
                for (T dependent : dependents) {
                    int remainingInDegree = graph.decrementAndGetInDegree(dependent);
                    if (remainingInDegree == 0) {
                        queue.offer(dependent);
                    }
                }
            }
        }

        if (ordered.size() != nodes.size()) {
            List<T> unresolvedNodes = graph.getNodesWithPositiveInDegree();
            String unresolvedNames = unresolvedNodes.stream()
                    .map(node -> node.getClass().getSimpleName())
                    .collect(Collectors.joining(", "));

            throw new IllegalStateException(
                    "Cyclic or unresolvable dependency detected: [" + unresolvedNames + "]"
            );
        }

        return ordered;
    }

    /**
     * Builds a dependency graph indicating in-degrees and dependent relationships.
     */
    @Contract("_, _ -> new")
    @NotNull
    public static <T extends DependencyNode> DependencyGraph<T> buildDependencyGraph(
            @NotNull Collection<T> nodes,
            @NotNull Collection<Dependency<?>> loaded
    ) {
        Map<T, Integer> inDegree = new HashMap<>(nodes.size());
        Map<T, Set<T>> dependents = new HashMap<>(nodes.size());

        for (T node : nodes) {
            inDegree.put(node, 0);
            dependents.put(node, new HashSet<>());
        }

        for (T node : nodes) {
            Set<Class<?>> remainingRequirements = remainingDependencies(node.requires(), loaded);
            if (remainingRequirements.isEmpty()) {
                continue;
            }

            for (T potentialDependency : nodes) {
                if (node.equals(potentialDependency)) {
                    continue;
                }

                boolean providesRequirement = potentialDependency.provides().stream()
                        .anyMatch(provided -> remainingRequirements.stream()
                                .anyMatch(req -> req.isAssignableFrom(provided)));

                if (providesRequirement) {
                    dependents.get(potentialDependency).add(node);
                    inDegree.compute(node, (k, currentDegree) -> (currentDegree == null ? 0 : currentDegree) + 1);
                }
            }
        }

        return new DependencyGraph<>(inDegree, dependents);
    }

    @NotNull
    private static Set<Class<?>> remainingDependencies(
            @NotNull Collection<Class<?>> required,
            @NotNull Collection<Dependency<?>> loaded
    ) {
        return required.stream()
                .filter(requirement -> loaded.stream().noneMatch(dependency -> dependency.is(requirement)))
                .collect(Collectors.toSet());
    }

    @NotNull
    private static Set<Class<?>> remainingDependencyClasses(
            @NotNull Collection<Class<?>> required,
            @NotNull Collection<Class<?>> loaded
    ) {
        return required.stream()
                .filter(requirement -> loaded.stream().noneMatch(requirement::isAssignableFrom))
                .collect(Collectors.toSet());
    }

    private Dependencies() {
        throw new UnsupportedOperationException("Utility class");
    }
}
