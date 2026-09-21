package dev.spoocy.adapter.gui.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class IncompatibleGuiException extends RuntimeException {

    public IncompatibleGuiException(@NotNull String message) {
        super(message);
    }

    public IncompatibleGuiException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
