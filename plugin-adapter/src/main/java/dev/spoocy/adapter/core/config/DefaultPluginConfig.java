package dev.spoocy.adapter.core.config;

import dev.spoocy.adapter.compatibility.CompatibilityProvider;
import dev.spoocy.adapter.config.BukkitConstructor;
import dev.spoocy.adapter.config.BukkitRepresenter;
import dev.spoocy.adapter.config.SerializationStrategy;
import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.language.GlobalTranslation;
import dev.spoocy.adapter.message.ActionbarHandler;
import dev.spoocy.adapter.message.color.Color;
import dev.spoocy.adapter.message.font.Fonts;
import dev.spoocy.adapter.sound.PSound;
import dev.spoocy.utils.common.misc.Args;
import dev.spoocy.utils.config.constructor.Constructor;
import dev.spoocy.utils.config.representer.Representer;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class DefaultPluginConfig implements PluginSetup {

    private static final SerializationStrategy DEFAULT_SERIALIZATION_STRATEGY = SerializationStrategy.LOG_ONLY;

    public static DefaultPluginConfig create(@NotNull PluginAdapter adapter) {
        return new DefaultPluginConfig(adapter);
    }

    protected final PluginAdapter adapter;
    protected CompatibilityProvider compatibilityProvider;
    protected GlobalTranslation globalTranslation;
    protected int spigotResourceId = -1;
    protected Constructor configConstructor = new BukkitConstructor(DEFAULT_SERIALIZATION_STRATEGY);
    protected Representer configRepresenter = new BukkitRepresenter(DEFAULT_SERIALIZATION_STRATEGY);

    protected ActionbarHandler actionbarHandler = (p, m) -> {
        throw new UnsupportedOperationException("ActionbarHandler not provided in config");
    };


    private DefaultPluginConfig(@NotNull PluginAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void setCompatibilityProvider(@NotNull CompatibilityProvider compatibilityProvider) {
        this.compatibilityProvider = Args.notNull(compatibilityProvider, "CompatibilityProvider");
    }

    @Override
    public void setGlobalTranslation(@NotNull GlobalTranslation translation) {
        this.globalTranslation = Args.notNull(translation, "GlobalTranslation");
    }

    @Override
    public void addSpigotUpdateChecker(int spigotResourceId) {
        this.spigotResourceId = Args.notNegative(spigotResourceId, "SpigotResourceId");
    }

    @Override
    public void setConfigConstructor(@NotNull Constructor constructor) {
        this.configConstructor = Args.notNull(constructor, "ConfigConstructor");
    }

    @Override
    public void setConfigRepresenter(@NotNull Representer representer) {
        this.configRepresenter = Args.notNull(representer, "ConfigRepresenter");
    }

    @Override
    public void setActionbarHandler(@NotNull ActionbarHandler handler) {
        this.actionbarHandler = Args.notNull(handler, "ActionbarHandler");
    }

    public @NotNull PluginConfig build() throws IllegalArgumentException {
        Args.notNull(this.compatibilityProvider, "AudienceProvider");
        Args.notNull(this.globalTranslation, "GlobalTranslation");

        PluginConfig config = new Registry();
        config.write(PluginConfig.Keys.COMPATIBILITY, this.compatibilityProvider);
        config.write(PluginConfig.Keys.GLOBAL_TRANSLATION, this.globalTranslation);
        config.write(PluginConfig.Keys.SPIGOT_RESOURCE_ID, this.spigotResourceId);
        config.write(PluginConfig.Keys.CONFIG_CONSTRUCTOR, this.configConstructor);
        config.write(PluginConfig.Keys.CONFIG_REPRESENTER, this.configRepresenter);
        config.write(PluginConfig.Keys.ACTIONBAR_HANDLER, this.actionbarHandler);
        return config;
    }

    private static class Registry implements PluginConfig {

        private final HashMap<NamespacedKey, Object> registry = new HashMap<>();

        private Registry() {
            write(Keys.BASE_COLOR, Color.GRAY);
            write(Keys.PRIMARY_COLOR, Color.BLUE);
            write(Keys.ERROR_COLOR, Color.RED);
            write(Keys.CLICK_SOUND, PSound.BASS);
            write(Keys.ERROR_SOUND, PSound.ANVIL);
            write(Keys.DEFAULT_FONT, Fonts.DEFAULT);
        }

        @Override
        @NotNull
        public <T> T read(@NotNull NamespacedKey key, @NotNull Class<T> type) {
            Object value = this.registry.getOrDefault(key, null);
            if (!type.isInstance(value)) {
                throw new IllegalArgumentException("No value registered for key " + key + " of type " + type.getName());
            }
            return type.cast(value);
        }

        @Override
        public void write(@NotNull NamespacedKey key, @NotNull Object value) {
            this.registry.put(key, value);
        }
    }

}
