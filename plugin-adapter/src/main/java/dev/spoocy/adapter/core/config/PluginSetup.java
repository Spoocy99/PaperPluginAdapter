package dev.spoocy.adapter.core.config;

import dev.spoocy.utils.config.constructor.Constructor;
import dev.spoocy.utils.config.representer.Representer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface PluginSetup {

    void addSpigotUpdateChecker(int spigotResourceId);

    void setConfigConstructor(@NotNull Constructor constructor);

    void setConfigRepresenter(@NotNull Representer representer);

}
