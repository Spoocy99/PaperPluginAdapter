package dev.spoocy.adapter.dependencies.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class UnresolvedDependencyException extends IllegalStateException {

    public UnresolvedDependencyException(@NotNull String message) {
        super(message);
    }

    public UnresolvedDependencyException(@NotNull Throwable cause) {
        super(cause);
    }

    public UnresolvedDependencyException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
