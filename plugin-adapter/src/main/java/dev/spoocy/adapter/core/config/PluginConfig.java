package dev.spoocy.adapter.core.config;

import dev.spoocy.adapter.compatibility.CompatibilityProvider;
import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.compatibility.AudienceProvider;
import dev.spoocy.adapter.language.GlobalTranslation;
import dev.spoocy.adapter.message.ActionbarHandler;
import dev.spoocy.adapter.messages.font.Font;
import dev.spoocy.adapter.sound.PSound;
import dev.spoocy.utils.config.constructor.Constructor;
import dev.spoocy.utils.config.representer.Representer;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface PluginConfig {

    static TextColor baseColor() {
        return forKey(Keys.BASE_COLOR, TextColor.class);
    }

    static TextColor primaryColor() {
        return forKey(Keys.PRIMARY_COLOR, TextColor.class);
    }

    static TextColor errorColor() {
        return forKey(Keys.ERROR_COLOR, TextColor.class);
    }

    static PSound clickSound() {
        return forKey(Keys.CLICK_SOUND, PSound.class);
    }

    static PSound errorSound() {
        return forKey(Keys.ERROR_SOUND, PSound.class);
    }

    static Font defaultFont() {
        return forKey(Keys.DEFAULT_FONT, Font.class);
    }

    static CompatibilityProvider compatibilityProvider() {
        return forKey(Keys.COMPATIBILITY, CompatibilityProvider.class);
    }

    static AudienceProvider audiences() {
        return compatibilityProvider().getAudienceProvider();
    }

    static GlobalTranslation globalTranslation() {
        return forKey(Keys.GLOBAL_TRANSLATION, GlobalTranslation.class);
    }

    static int spigotResourceId() {
        return forKey(Keys.SPIGOT_RESOURCE_ID, Integer.class);
    }

    static Constructor configConstructor() {
        return forKey(Keys.CONFIG_CONSTRUCTOR, Constructor.class);
    }

    static Representer configRepresenter() {
        return forKey(Keys.CONFIG_REPRESENTER, Representer.class);
    }

    static ActionbarHandler actionbarHandler() {
        return forKey(Keys.ACTIONBAR_HANDLER, ActionbarHandler.class);
    }

    @NotNull
    static <T> T forKey(@NotNull NamespacedKey key, @NotNull Class<T> type) {
        PluginConfig config = PluginAdapter.getInstance().getConfiguration();
        return config.read(key, type);
    }

    @NotNull
    static <T> T forName(@NotNull String name, @NotNull Class<T> type) {
        return forKey(key(name), type);
    }

    private static NamespacedKey key(@NotNull String name) {
        return new NamespacedKey(PluginAdapter.getInstance(), name);
    }

    @NotNull
    <T> T read(@NotNull NamespacedKey key, @NotNull Class<T> type);

    void write(@NotNull NamespacedKey key, @NotNull Object value);

    class Keys {
        public static final NamespacedKey BASE_COLOR = key("base_color");
        public static final NamespacedKey PRIMARY_COLOR = key("primary_color");
        public static final NamespacedKey ERROR_COLOR = key("error_color");
        public static final NamespacedKey CLICK_SOUND = key("click_sound");
        public static final NamespacedKey ERROR_SOUND = key("error_sound");
        public static final NamespacedKey DEFAULT_FONT = key("default_font");
        public static final NamespacedKey COMPATIBILITY = key("compatibility_provider");
        public static final NamespacedKey GLOBAL_TRANSLATION = key("global_translation");
        public static final NamespacedKey SPIGOT_RESOURCE_ID = key("spigot_resource_id");
        public static final NamespacedKey CONFIG_CONSTRUCTOR = key("config_constructor");
        public static final NamespacedKey CONFIG_REPRESENTER = key("config_representer");
        public static final NamespacedKey ACTIONBAR_HANDLER = key("actionbar_handler");
    }
}
