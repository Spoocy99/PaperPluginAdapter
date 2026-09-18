package dev.spoocy.adapter.dependencies;

/**
 * Another Plugin as a dependency
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface PluginDependency {

    /**
     * The name of the plugin providing this dependency
     *
     * @return the name of the plugin
     */
    String getName();

    /**
     * If this dependency is required
     *
     * @return {@code true} if required, {@code false} otherwise
     */
    boolean isRequired();

    /**
     * If {@link #download()} can be invoked.
     *
     * @return {@code true} if downloadable, {@code false} otherwise
     */
    boolean isDownloadable();

    /**
     * Tries to download the dependency.
     *
     * @return {@code true} if the download was successful, {@code false} otherwise
     *
     * @throws UnsupportedOperationException if not downloadable
     *
     * @see #isDownloadable()
     */
    default boolean download() {
        throw new UnsupportedOperationException("Not downloadable");
    }

}
