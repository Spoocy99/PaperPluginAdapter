package dev.spoocy.adapter.dependencies.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class UnresolvedDependencyException extends IllegalStateException {

    private final String dependency;

    public UnresolvedDependencyException(@NotNull String dependency) {
        super("Dependency '" + dependency + "' is required but not present.");
        this.dependency = dependency;
    }

    public String getDependency() {
        return this.dependency;
    }
}
