package dev.spoocy.adapter.core.config;

import dev.spoocy.adapter.compatibility.CompatibilityProvider;
import dev.spoocy.adapter.language.GlobalTranslation;
import dev.spoocy.adapter.message.ActionbarHandler;
import dev.spoocy.utils.config.constructor.Constructor;
import dev.spoocy.utils.config.representer.Representer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface PluginSetup {

    void setCompatibilityProvider(@NotNull CompatibilityProvider compatibilityProvider);

    void setGlobalTranslation(@NotNull GlobalTranslation translation);

    void addSpigotUpdateChecker(int spigotResourceId);

    void setConfigConstructor(@NotNull Constructor constructor);

    void setConfigRepresenter(@NotNull Representer representer);

    void setActionbarHandler(@NotNull ActionbarHandler handler);
}
