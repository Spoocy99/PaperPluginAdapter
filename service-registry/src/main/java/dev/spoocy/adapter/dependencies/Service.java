package dev.spoocy.adapter.dependencies;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Service extends DependencyNode {

    @ApiStatus.OverrideOnly
    void load(@NotNull DependencyProvider provider);

}
